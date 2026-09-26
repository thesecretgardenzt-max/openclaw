import sqlite3
import time

import pytest

from app import LOGIN_ERROR_MESSAGE, SESSION_COOKIE_NAME, SESSION_TTL_SECONDS, _serializer, create_app


@pytest.fixture()
def app(tmp_path):
    return create_app(
        {
            "TESTING": True,
            "DATABASE": str(tmp_path / "test.sqlite3"),
            "GOOGLE_CLIENT_ID": "test-client-id",
            "GOOGLE_CLIENT_SECRET": "test-client-secret",
            "SESSION_SECRET": "test-session-secret",
        }
    )


@pytest.fixture()
def client(app):
    return app.test_client()


def test_login_page_only_offers_google(client):
    response = client.get("/")
    assert response.status_code == 200
    assert b"Todo List" in response.data
    assert b"Continue with Google" in response.data
    assert b"password" not in response.data.lower()
    assert b"email" not in response.data.lower()


def test_google_start_uses_fixed_callback_route(app, client, monkeypatch):
    oauth_client = app.extensions["google_oauth_client"]
    monkeypatch.setattr(oauth_client, "authorize_redirect", lambda callback_url: callback_url)
    response = client.get("/auth/google/start")
    assert response.status_code == 200
    assert response.text == "http://localhost/auth/google/callback"


def test_successful_callback_creates_user_and_signed_cookie(app, client, monkeypatch):
    oauth_client = app.extensions["google_oauth_client"]
    monkeypatch.setattr(
        oauth_client,
        "authorize_access_token",
        lambda: {"userinfo": {"sub": "google-user-123"}},
    )
    issued_at = int(time.time())
    response = client.get("/auth/google/callback")
    assert response.status_code == 302
    assert response.headers["Location"].endswith("/todos")
    cookie = response.headers["Set-Cookie"]
    assert f"{SESSION_COOKIE_NAME}=" in cookie
    assert "HttpOnly" in cookie
    assert "SameSite=Lax" in cookie
    assert f"Max-Age={SESSION_TTL_SECONDS}" in cookie
    cookie_value = client.get_cookie(SESSION_COOKIE_NAME).value
    payload = _serializer(app).loads(cookie_value, max_age=SESSION_TTL_SECONDS)
    assert isinstance(payload["user_id"], int)
    assert issued_at + SESSION_TTL_SECONDS <= payload["expires_at"] <= int(time.time()) + SESSION_TTL_SECONDS
    with sqlite3.connect(app.config["DATABASE"]) as connection:
        rows = connection.execute("SELECT google_sub FROM users").fetchall()
    assert rows == [("google-user-123",)]
    assert client.get("/todos").status_code == 200


@pytest.mark.parametrize(
    "query, token_result",
    [("?error=access_denied", None), ("", {"userinfo": {}})],
)
def test_failed_or_cancelled_callback_returns_to_login_without_session(
    app, client, monkeypatch, query, token_result
):
    oauth_client = app.extensions["google_oauth_client"]
    if token_result is not None:
        monkeypatch.setattr(oauth_client, "authorize_access_token", lambda: token_result)
    response = client.get(f"/auth/google/callback{query}", follow_redirects=True)
    assert response.status_code == 200
    assert LOGIN_ERROR_MESSAGE.encode() in response.data
    assert client.get_cookie(SESSION_COOKIE_NAME) is None


def test_google_sub_is_unique_and_reuses_existing_user(app, client, monkeypatch):
    oauth_client = app.extensions["google_oauth_client"]
    monkeypatch.setattr(
        oauth_client,
        "authorize_access_token",
        lambda: {"userinfo": {"sub": "same-google-user"}},
    )
    client.get("/auth/google/callback")
    client.get("/auth/google/callback")
    with sqlite3.connect(app.config["DATABASE"]) as connection:
        count = connection.execute(
            "SELECT COUNT(*) FROM users WHERE google_sub = ?", ("same-google-user",)
        ).fetchone()[0]
    assert count == 1


def test_invalid_signed_cookie_does_not_grant_access(client):
    client.set_cookie(SESSION_COOKIE_NAME, "invalid-value")
    response = client.get("/todos")
    assert response.status_code == 302
    assert response.headers["Location"].endswith("/")


def test_expired_signed_cookie_does_not_grant_access(app, client):
    value = _serializer(app).dumps(
        {"user_id": 1, "expires_at": int(time.time()) - 1}
    )
    client.set_cookie(SESSION_COOKIE_NAME, value)
    response = client.get("/todos")
    assert response.status_code == 302
    assert response.headers["Location"].endswith("/")


def test_logout_deletes_app_session(app, client):
    value = _serializer(app).dumps(
        {"user_id": 1, "expires_at": int(time.time()) + SESSION_TTL_SECONDS}
    )
    client.set_cookie(SESSION_COOKIE_NAME, value)
    response = client.post("/auth/logout")
    assert response.status_code == 302
    assert response.headers["Location"].endswith("/")
    assert f"{SESSION_COOKIE_NAME}=;" in response.headers["Set-Cookie"]
    assert client.get_cookie(SESSION_COOKIE_NAME) is None

