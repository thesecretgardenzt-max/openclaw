# Báo cáo triển khai IMPL-US-001 — Đăng nhập bằng Google

## Thông tin artifact

| Trường schema | Giá trị |
|---|---|
| `schema_version` | `1.0.0` |
| `artifact_type` | `implementation_report` |
| `artifact_id` | `IMPL-US-001` |
| `project_id` | `TODO-LIST-DEMO` |
| `source_architecture_artifact.artifact_id` | `ARCH-US-001` |
| `source_architecture_artifact.artifact_type` | `technical_architecture` |
| `source_architecture_artifact.schema_version` | `1.0.0` |

## `implementation_summary`

Đã tạo Python web app tối thiểu cho US-001 bằng Flask và Authlib. App có Login Page chỉ cung cấp Google, các route `GET /auth/google/start`, `GET /auth/google/callback`, `POST /auth/logout`, user nhận diện bằng `google_sub`, SQLite lưu `google_sub` với ràng buộc `UNIQUE NOT NULL`, signed cookie chứa `user_id` và `expires_at` với TTL 24 giờ, route Todo List được bảo vệ, và fail/cancel quay lại Login Page với message ngắn.

## `implementation_status`

`completed` — implementation đã sẵn sàng để QA/Reviewer kiểm tra; trạng thái này không phải kết luận QA PASS.

## `change_set`

| path | operation | summary | component_id | linked_requirement_ids |
|---|---|---|---|---|
| `workspace-shared/app/.gitignore` | `create` | Bỏ qua virtualenv, cache Python, pytest và database runtime. | `CMP-001` | `US-001` |
| `workspace-shared/app/app.py` | `create` | App factory, Authlib Google flow, user persistence, signed cookie 24 giờ, route guard và logout. | `CMP-001` | `US-001`, `AC-001`, `AC-002`, `AC-003` |
| `workspace-shared/app/requirements.txt` | `create` | Khai báo dependency runtime được pin. | `CMP-001` | `US-001` |
| `workspace-shared/app/templates/login.html` | `create` | Login Page có tên app, nút Google và vùng thông báo lỗi accessible. | `CMP-001` | `AC-001`, `AC-003` |
| `workspace-shared/app/templates/todos.html` | `create` | Trang Todo List tối thiểu sau đăng nhập và form logout POST. | `CMP-001` | `AC-002` |
| `workspace-shared/app/tests/test_auth.py` | `create` | Test UI, route, callback success/fail/cancel, user unique, cookie, expiry, guard và logout. | `CMP-001` | `US-001`, `AC-001`, `AC-002`, `AC-003` |
| `workspace-shared/app/README.md` | `create` | Hướng dẫn setup, cấu hình redacted, chạy app và test. | `CMP-001` | `US-001` |
| `workspace-shared/implementation/IMPL-US-001-dang-nhap-bang-google.md` | `create` | Báo cáo triển khai, change note và hướng dẫn QA. | `CMP-001` | `US-001` |

## `dependencies`

| name | version | scope | reason | license_reviewed |
|---|---|---|---|---:|
| `Authlib` | `1.6.4` | `runtime` | Thực hiện Google OAuth/OIDC theo Architecture. | `false` |
| `Flask` | `3.1.2` | `runtime` | Framework Python tối thiểu vì shared workspace chưa có source/framework hiện hữu. | `false` |
| `requests` | `2.32.5` | `runtime` | HTTP client cần cho Authlib Flask integration. | `false` |
| `pytest` | `9.1.1` | `test` | Chạy test tự động; được cài trong virtualenv kiểm thử, không nằm trong runtime requirements. | `false` |

## `database_changes`

| id | kind | description | migration_path | rollback_path | data_loss_risk |
|---|---|---|---|---|---|
| `DBCHG-001` | `schema` | App tự tạo bảng SQLite `users(id, google_sub UNIQUE NOT NULL)` khi khởi tạo. | `workspace-shared/app/app.py::_init_database` | Xóa database runtime trong `workspace-shared/app/instance/` đối với demo local. | `low` |

## `configuration_changes`

| key | environment_scope | required | secret | description | default_behavior |
|---|---|---:|---:|---|---|
| `GOOGLE_CLIENT_ID` | `local`, `demo` | `true` | `true` | Client ID Google OAuth; chỉ cấu hình ngoài source dưới dạng secret. | App từ chối khởi động ngoài test nếu thiếu. |
| `GOOGLE_CLIENT_SECRET` | `local`, `demo` | `true` | `true` | Client secret Google OAuth; chỉ cấu hình ngoài source dưới dạng secret. | App từ chối khởi động ngoài test nếu thiếu. |
| `SESSION_SECRET` | `local`, `demo` | `true` | `true` | Khóa ký app session cookie và OAuth state session. | App từ chối khởi động ngoài test nếu thiếu. |

## `implementation_tasks`

| id | title | status | component_ids | linked_requirement_ids | notes |
|---|---|---|---|---|---|
| `TASK-001` | Login Page và user identity | `completed` | `CMP-001` | `AC-001`, `AC-003` | UI không có email/password; `google_sub` là `UNIQUE NOT NULL`. |
| `TASK-002` | Google login và signed session | `completed` | `CMP-001` | `AC-002` | Authlib start/callback; cookie ký có `user_id`, `expires_at`, TTL 24 giờ. |
| `TASK-003` | Logout và test | `completed` | `CMP-001` | `US-001`, `AC-001`, `AC-002`, `AC-003` | Logout POST xóa app cookie; test suite liên quan đã chạy. |

## `automated_tests`

| id | level | name | path | linked_requirement_ids | result |
|---|---|---|---|---|---|
| `TEST-001` | `integration` | Login Page chỉ cung cấp Google | `workspace-shared/app/tests/test_auth.py` | `AC-001`, `AC-003` | `passed` |
| `TEST-002` | `integration` | Route start dùng callback cố định | `workspace-shared/app/tests/test_auth.py` | `AC-002` | `passed` |
| `TEST-003` | `integration` | Callback success tạo/reuse user và signed cookie | `workspace-shared/app/tests/test_auth.py` | `AC-002` | `passed` |
| `TEST-004` | `integration` | Callback fail/cancel không tạo session và hiện message | `workspace-shared/app/tests/test_auth.py` | `AC-002` | `passed` |
| `TEST-005` | `security` | Cookie sai chữ ký hoặc hết hạn không cấp quyền | `workspace-shared/app/tests/test_auth.py` | `AC-002` | `passed` |
| `TEST-006` | `integration` | Logout xóa app session | `workspace-shared/app/tests/test_auth.py` | `US-001` | `passed` |

## `security_review`

- **input_validation:** Callback chỉ chấp nhận `userinfo.sub` là chuỗi không rỗng sau khi Authlib xử lý token; SQL dùng parameter binding; cookie payload kiểm tra type và expiry.
- **authorization:** `/todos` yêu cầu app session cookie có chữ ký hợp lệ và chưa hết hạn; cookie giả hoặc hết hạn bị chuyển về Login Page.
- **secret_handling:** Không hardcode hoặc ghi log credential/token/cookie value; source chỉ đọc tên biến môi trường. Test dùng giá trị giả cục bộ.
- **dependency_scan:** `.venv/bin/python -m pip check` trả về `No broken requirements found`; chưa chạy vulnerability scanner hoặc license review riêng.
- **findings:** Cookie có `HttpOnly`, `SameSite=Lax`, và `Secure` khi request dùng HTTPS; OAuth state dùng Flask signed session với cùng secret runtime; chưa xác minh end-to-end với Google thật do không sử dụng credential trong pipeline.

## `verification`

| command | purpose | result | evidence |
|---|---|---|---|
| `cd workspace-shared/app && python3 -m pytest -q` | Kiểm tra test runner sẵn có trước setup. | `failed` | Python hệ thống chưa cài `pytest`; sau đó tạo virtualenv theo README. |
| `cd workspace-shared/app && python3 -m venv .venv && .venv/bin/python -m pip install -r requirements.txt pytest` | Tạo môi trường test và cài dependency. | `passed` | Dependency runtime và pytest được cài thành công. Lần đầu phát hiện thiếu `requests`; đã bổ sung dependency trực tiếp và cài lại. |
| `cd workspace-shared/app && .venv/bin/python -m pytest -q` | Chạy test tự động cuối cùng. | `passed` | `9 passed in 0.20s`. |
| `cd workspace-shared/app && .venv/bin/python -m compileall -q app.py tests` | Kiểm tra Python syntax/bytecode compilation. | `passed` | Command kết thúc với exit code 0, không có lỗi. |
| `cd workspace-shared/app && .venv/bin/python -m pip check` | Kiểm tra dependency consistency. | `passed` | `No broken requirements found.` |
| Google OAuth end-to-end với credential thật | Xác minh redirect, consent và callback với Google Identity thật. | `not_run` | Không dùng credential hoặc cấu hình OAuth thật trong môi trường pipeline; QA cần chạy ở demo environment đã cấu hình. |

## `known_limitations`

- Shared workspace ban đầu không có source app, framework, database hoặc test convention; implementation tạo scaffold Flask + SQLite tối thiểu để đáp ứng mục tiêu demo.
- Chưa chạy end-to-end với Google Identity thật; callback success/fail/cancel được mock ở biên Authlib.
- SQLite schema được khởi tạo trực tiếp khi app start; demo không có migration framework hoặc production deployment design.
- Chưa thực hiện vulnerability scan và license review độc lập cho dependency.

## `requirement_coverage`

| requirement_id | status | implemented_by | verified_by_test_ids |
|---|---|---|---|
| `US-001` | `implemented` | `TASK-001`, `TASK-002`, `TASK-003` | `TEST-001`, `TEST-002`, `TEST-003`, `TEST-004`, `TEST-005`, `TEST-006` |
| `AC-001` | `implemented` | `TASK-001` | `TEST-001` |
| `AC-002` | `implemented` | `TASK-002`, `TASK-003` | `TEST-002`, `TEST-003`, `TEST-004`, `TEST-005`, `TEST-006` |
| `AC-003` | `implemented` | `TASK-001` | `TEST-001` |

## `handoff_to_qa`

- **build_identifier:** `IMPL-US-001`
- **test_entrypoint:** `cd workspace-shared/app && python3 -m venv .venv && .venv/bin/python -m pip install -r requirements.txt pytest && .venv/bin/python -m pytest -q`
- **test_data_requirements:** Không cần dữ liệu thật cho automated tests; manual OAuth cần Google test account và cấu hình secret ngoài source.
- **high_risk_areas:** Redirect URI Google, OAuth state session, cookie flags sau HTTPS proxy, session expiry 24 giờ.
- **known_failures:** Không có failure còn tồn tại trong test suite; Google OAuth end-to-end chưa chạy.
- **environment_requirements:** Python 3, HTTPS cho demo ngoài local, `GOOGLE_CLIENT_ID=[REDACTED]`, `GOOGLE_CLIENT_SECRET=[REDACTED]`, `SESSION_SECRET=[REDACTED]`, redirect URI trỏ tới `/auth/google/callback`.
- **readiness:** `ready_with_known_issues`

# Change Note

## Task

`US-001`

## Summary

Thêm app demo đăng nhập Google tối thiểu, signed cookie 24 giờ, user lookup theo `google_sub`, fail/cancel message, logout local và test tự động.

## Technical Plan Reference

`workspace-shared/architecture/ARCH-US-001-dang-nhap-bang-google.md`, `CMP-001`, `INT-001`–`INT-004`, `ARCH-TASK-001`–`ARCH-TASK-003`.

## Files Changed

- `workspace-shared/app/.gitignore`
- `workspace-shared/app/app.py`
- `workspace-shared/app/requirements.txt`
- `workspace-shared/app/templates/login.html`
- `workspace-shared/app/templates/todos.html`
- `workspace-shared/app/tests/test_auth.py`
- `workspace-shared/app/README.md`
- `workspace-shared/implementation/IMPL-US-001-dang-nhap-bang-google.md`

## Changes

- Thêm Login Page chỉ có Google login.
- Thêm ba auth route đúng contract và Todo List guard tối thiểu.
- Thêm user persistence và signed cookie TTL 24 giờ.
- Thêm fail/cancel behavior, local logout và test suite.

## Reason

Đáp ứng US-001 và kiến trúc `ARCH-US-001` để kiểm thử quy trình điều phối agent.

## Dependencies

Authlib, Flask, requests; SQLite dùng Python standard library.

## Known Limitations

Google OAuth end-to-end chưa được chạy với credential thật.

## Deviations

Architecture giả định có Python codebase/database hiện hữu nhưng shared workspace không có source. Implementation dùng scaffold Flask + SQLite tối thiểu; không thêm service hoặc business rule mới.

## Review Notes

QA cần chú ý callback URL, cookie flags trên HTTPS, fail/cancel không tạo app session, và logout chỉ xóa app session.

# How to Test

## Prerequisites

- Python 3.
- Automated tests không cần credential thật.
- Manual Google flow cần cấu hình secret ngoài source và redirect URI chính xác.

## Setup

```bash
cd workspace-shared/app
python3 -m venv .venv
.venv/bin/python -m pip install -r requirements.txt pytest
```

## Automated Tests

```bash
.venv/bin/python -m pytest -q
```

Expected: 9 test cases hoàn tất thành công.

## Manual Test

### Case 1 — Login Page

1. Cấu hình ba biến môi trường đã redacted theo README.
2. Chạy `flask --app app run`.
3. Mở `/`.

Expected: thấy `Todo List`, nút `Continue with Google`, không có email/password.

### Case 2 — Google login success

1. Chọn `Continue with Google`.
2. Hoàn tất Google consent.

Expected: chuyển tới `/todos`; browser có app session cookie ký, `HttpOnly`, TTL 24 giờ.

### Case 3 — Fail/cancel

1. Bắt đầu Google login.
2. Hủy consent hoặc gây callback lỗi hợp lệ.

Expected: quay lại Login Page, hiển thị message ngắn; không có app session.

### Case 4 — Logout

1. Đăng nhập thành công.
2. Submit form `Đăng xuất`.

Expected: app session cookie bị xóa và quay về Login Page.

## Tests Executed by Developer

- Pytest: executed, `9 passed`.
- Python compileall: executed, exit code 0.
- Pip dependency check: executed, không có broken requirements.
- Google OAuth end-to-end: not executed vì pipeline không sử dụng credential thật.

## Observed Results

Automated test xác nhận UI, route contract, user unique/reuse, callback success/fail/cancel, signed cookie payload/TTL, cookie invalid/expired guard và logout. QA/Reviewer chịu trách nhiệm đánh giá cuối cùng.