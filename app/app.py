import os
import sqlite3
import time
from pathlib import Path

from authlib.integrations.flask_client import OAuth
from flask import Flask, g, make_response, redirect, render_template, request, url_for
from itsdangerous import BadSignature, URLSafeTimedSerializer


SESSION_COOKIE_NAME = "app_session"
SESSION_TTL_SECONDS = 24 * 60 * 60
LOGIN_ERROR_MESSAGE = "Đăng nhập Google không thành công. Vui lòng thử lại."


def create_app(test_config=None):
    app = Flask(__name__, instance_relative_config=True)
    app.config.from_mapping(
        DATABASE=str(Path(app.instance_path) / "todo-demo.sqlite3"),
        GOOGLE_CLIENT_ID=os.getenv("GOOGLE_CLIENT_ID"),
        GOOGLE_CLIENT_SECRET=os.getenv("GOOGLE_CLIENT_SECRET"),
        SESSION_SECRET=os.getenv("SESSION_SECRET"),
    )
    if test_config:
        app.config.update(test_config)

    Path(app.instance_path).mkdir(parents=True, exist_ok=True)
    _validate_config(app)
    app.secret_key = app.config["SESSION_SECRET"]
    _init_database(app)

    oauth = OAuth(app)
    oauth.register(
        name="google",
        server_metadata_url="https://accounts.google.com/.well-known/openid-configuration",
        client_kwargs={"scope": "openid"},
    )
    app.extensions["google_oauth_client"] = oauth.google

    @app.before_request
    def load_current_user():
        g.user_id = _read_session_cookie(app)

    @app.get("/")
    def login():
        if g.user_id is not None:
            return redirect(url_for("todo_list"))
        error = LOGIN_ERROR_MESSAGE if request.args.get("error") == "google_login_failed" else None
        return render_template("login.html", error=error)

    @app.get("/auth/google/start")
    def google_start():
        try:
            callback_url = url_for("google_callback", _external=True)
            return app.extensions["google_oauth_client"].authorize_redirect(callback_url)
        except Exception:
            app.logger.exception("Không thể bắt đầu đăng nhập Google")
            return _login_error_redirect()

    @app.get("/auth/google/callback")
    def google_callback():
        if request.args.get("error"):
            return _login_error_redirect()

        try:
            token = app.extensions["google_oauth_client"].authorize_access_token()
            claims = token.get("userinfo") or {}
            google_sub = claims.get("sub")
            if not isinstance(google_sub, str) or not google_sub.strip():
                return _login_error_redirect()
            user_id = _find_or_create_user(app, google_sub.strip())
        except Exception:
            app.logger.exception("Đăng nhập Google không thành công")
            return _login_error_redirect()

        response = make_response(redirect(url_for("todo_list")))
        _set_session_cookie(app, response, user_id)
        return response

    @app.get("/todos")
    def todo_list():
        if g.user_id is None:
            return redirect(url_for("login"))
        return render_template("todos.html")

    @app.post("/auth/logout")
    def logout():
        response = make_response(redirect(url_for("login")))
        response.delete_cookie(
            SESSION_COOKIE_NAME,
            httponly=True,
            secure=request.is_secure,
            samesite="Lax",
        )
        return response

    return app


def _validate_config(app):
    if app.config.get("TESTING"):
        return
    missing = [
        key
        for key in ("GOOGLE_CLIENT_ID", "GOOGLE_CLIENT_SECRET", "SESSION_SECRET")
        if not app.config.get(key)
    ]
    if missing:
        raise RuntimeError(f"Thiếu cấu hình bắt buộc: {', '.join(missing)}")


def _connect_database(app):
    connection = sqlite3.connect(app.config["DATABASE"])
    connection.row_factory = sqlite3.Row
    return connection


def _init_database(app):
    with _connect_database(app) as connection:
        connection.execute(
            """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                google_sub TEXT NOT NULL UNIQUE
            )
            """
        )


def _find_or_create_user(app, google_sub):
    with _connect_database(app) as connection:
        connection.execute(
            "INSERT OR IGNORE INTO users (google_sub) VALUES (?)",
            (google_sub,),
        )
        row = connection.execute(
            "SELECT id FROM users WHERE google_sub = ?",
            (google_sub,),
        ).fetchone()
    return row["id"]


def _serializer(app):
    return URLSafeTimedSerializer(app.config["SESSION_SECRET"], salt="app-session")


def _set_session_cookie(app, response, user_id):
    expires_at = int(time.time()) + SESSION_TTL_SECONDS
    value = _serializer(app).dumps({"user_id": user_id, "expires_at": expires_at})
    response.set_cookie(
        SESSION_COOKIE_NAME,
        value,
        max_age=SESSION_TTL_SECONDS,
        httponly=True,
        secure=request.is_secure,
        samesite="Lax",
    )


def _read_session_cookie(app):
    value = request.cookies.get(SESSION_COOKIE_NAME)
    if not value:
        return None
    try:
        payload = _serializer(app).loads(value, max_age=SESSION_TTL_SECONDS)
        user_id = payload.get("user_id")
        expires_at = payload.get("expires_at")
        if not isinstance(user_id, int) or not isinstance(expires_at, int):
            return None
        if expires_at <= int(time.time()):
            return None
        return user_id
    except BadSignature:
        return None


def _login_error_redirect():
    return redirect(url_for("login", error="google_login_failed"))


