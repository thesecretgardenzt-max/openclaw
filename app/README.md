# Todo List demo — US-001

Ứng dụng Python tối thiểu để kiểm thử đăng nhập Google theo `ARCH-US-001`.

## Cài đặt và chạy local

```bash
python3 -m venv .venv
. .venv/bin/activate
python -m pip install -r requirements.txt pytest
export GOOGLE_CLIENT_ID='[REDACTED]'
export GOOGLE_CLIENT_SECRET='[REDACTED]'
export SESSION_SECRET='[REDACTED]'
flask --app app run
```

Google OAuth redirect URI cho local: `http://localhost:5000/auth/google/callback`.

## Test

```bash
pytest -q
```
