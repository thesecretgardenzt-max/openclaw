# QA-US-001 — Đăng nhập bằng Google

## `schema_version`

`1.0.0`

## `artifact_type`

`qa_report`

## `artifact_id`

`QA-US-001`

## `project_id`

`TODO-LIST-DEMO`

## `source_ba_artifact`

| Field | Value |
|---|---|
| `artifact_id` | `US-001` |
| `artifact_type` | `business_analysis` |
| `schema_version` | `markdown-user-story` |

Nguồn: `workspace-shared/projects/ToDoApp/requirements/US-001-dang-nhap-bang-google.md`.

## `source_developer_artifact`

| Field | Value |
|---|---|
| `artifact_id` | `IMPL-US-001` |
| `artifact_type` | `implementation_report` |
| `schema_version` | `1.0.0` |

Nguồn: `workspace-shared/projects/ToDoApp/implementation/IMPL-US-001-dang-nhap-bang-google.md`.

## `test_summary`

| Field | Value |
|---|---:|
| `executed` | 7 |
| `passed` | 7 |
| `failed` | 0 |
| `blocked` | 0 |
| `skipped` | 1 |
| `overall_status` | `passed_with_risks` |

**Overall theo quy ước QA:** `PASS` với residual risk. Các kiểm tra mock/integration đáp ứng AC-001–AC-003; E2E với Google Identity thật là `not_run` vì không có credential/configuration thật và không bị coi là lỗi trong scope demo.

## `test_environment`

| Field | Value |
|---|---|
| `name` | QA local độc lập |
| `build_identifier` | `IMPL-US-001@00d6ec7` |
| `platform` | macOS Darwin, Python 3.14.5, Flask 3.1.2, Authlib 1.6.4, pytest 9.1.1, SQLite |
| `configuration` | Flask test client; database tạm; `TESTING=True`; OAuth client được mock tại biên Authlib; HTTPS được mô phỏng bằng `base_url` |
| `test_data` | `google_sub` tổng hợp; session secret tổng hợp chỉ dùng trong process test; không dùng dữ liệu người dùng thật |
| `limitations` | Không có Google OAuth credential/redirect URI thật; không chạy consent/redirect/callback ngoài Google Identity; không có công cụ đo statement/branch coverage; không chạy vulnerability scanner hay license review |

## `quality_gates`

| `name` | `criterion` | `result` | `evidence` |
|---|---|---|---|
| AC functional gate | AC-001–AC-003 có test traceable và tất cả test đã chạy đều pass | `passed` | `workspace-shared/projects/ToDoApp/qa/QA-US-001-dang-nhap-bang-google/evidence/previous-run/pytest.log`; `workspace-shared/projects/ToDoApp/qa/QA-US-001-dang-nhap-bang-google/evidence/previous-run/independent-verification.log` |
| Build/syntax gate | Source compile được và dependency nhất quán | `passed` | `workspace-shared/projects/ToDoApp/qa/QA-US-001-dang-nhap-bang-google/evidence/previous-run/build-verification.log` |
| Session/security gate | Cookie ký, TTL 24 giờ, HttpOnly/SameSite và Secure trên HTTPS; cookie sai/hết hạn không cấp quyền | `passed` | `workspace-shared/projects/ToDoApp/qa/QA-US-001-dang-nhap-bang-google/evidence/previous-run/pytest.log`; `workspace-shared/projects/ToDoApp/qa/QA-US-001-dang-nhap-bang-google/evidence/previous-run/independent-verification.log` |
| Sensitive-data gate | Evidence/source được kiểm tra, không lưu credential, token, cookie/session value, `.env` hoặc private key | `passed` | `workspace-shared/projects/ToDoApp/qa/QA-US-001-dang-nhap-bang-google/evidence/previous-run/source-hygiene.log`; `workspace-shared/projects/ToDoApp/qa/QA-US-001-dang-nhap-bang-google/evidence/previous-run/README.md` |
| Google OAuth E2E gate | Hoàn tất OAuth thật qua Google Identity trong demo environment đã cấu hình | `not_evaluated` | Không có credential thật; `TC-US001-008` là `not_run` |
| Code coverage gate | Có số đo statement và branch coverage | `not_evaluated` | `workspace-shared/projects/ToDoApp/qa/QA-US-001-dang-nhap-bang-google/evidence/previous-run/coverage.log` |

## `requirement_traceability`

| `requirement_id` | `acceptance_criterion_ids` | `test_case_ids` | `coverage_status` |
|---|---|---|---|
| `US-001` | `AC-001`, `AC-002`, `AC-003` | `TC-US001-001`–`TC-US001-008` | `partially_covered` |
| `AC-001` | `AC-001` | `TC-US001-001` | `covered` |
| `AC-002` | `AC-002` | `TC-US001-003`, `TC-US001-004`, `TC-US001-005`, `TC-US001-006`, `TC-US001-008` | `partially_covered` |
| `AC-003` | `AC-003` | `TC-US001-002` | `covered` |

`AC-002` được xác minh ở mức integration bằng OAuth mock; E2E với Google thật chưa chạy nên đánh dấu `partially_covered`, không suy diễn thành pass E2E.

## `test_cases`

### `TC-US001-001` — Trang đăng nhập cho người dùng chưa xác thực

- `level`: `acceptance`
- `priority`: `critical`
- `preconditions`: người dùng không có app session.
- `steps`:
  1. **Action:** Gửi `GET /`. **Expected result:** HTTP 200 và hiển thị tên `Todo List`.
  2. **Action:** Kiểm tra CTA. **Expected result:** Hiển thị `Continue with Google` trỏ tới `/auth/google/start`.
- `linked_requirement_ids`: `US-001`, `AC-001`
- `result`: `passed`
- `evidence`: `workspace-shared/projects/ToDoApp/qa/QA-US-001-dang-nhap-bang-google/evidence/previous-run/pytest.log`; `workspace-shared/projects/ToDoApp/qa/QA-US-001-dang-nhap-bang-google/evidence/previous-run/independent-verification.log`

### `TC-US001-002` — Chỉ cung cấp đăng nhập Google

- `level`: `acceptance`
- `priority`: `high`
- `preconditions`: người dùng ở Login Page.
- `steps`:
  1. **Action:** Kiểm tra các control đăng nhập trong HTML. **Expected result:** Có Google CTA; không có input email/password hoặc phương thức email/password.
- `linked_requirement_ids`: `US-001`, `AC-003`
- `result`: `passed`
- `evidence`: `workspace-shared/projects/ToDoApp/qa/QA-US-001-dang-nhap-bang-google/evidence/previous-run/pytest.log`; `workspace-shared/projects/ToDoApp/qa/QA-US-001-dang-nhap-bang-google/evidence/previous-run/independent-verification.log`

### `TC-US001-003` — Contract route bắt đầu Google OAuth

- `level`: `contract`
- `priority`: `high`
- `preconditions`: OAuth client được thay bằng test double, không gọi mạng.
- `steps`:
  1. **Action:** Gửi `GET /auth/google/start`. **Expected result:** Authlib nhận callback URL `http://localhost/auth/google/callback`.
  2. **Action:** Kiểm tra route map. **Expected result:** Start/callback dùng GET và logout chỉ dùng POST.
- `linked_requirement_ids`: `US-001`, `AC-002`, `ARCH-US-001/INT-002`, `ARCH-US-001/INT-003`
- `result`: `passed`
- `evidence`: `workspace-shared/projects/ToDoApp/qa/QA-US-001-dang-nhap-bang-google/evidence/previous-run/pytest.log`; `workspace-shared/projects/ToDoApp/qa/QA-US-001-dang-nhap-bang-google/evidence/previous-run/independent-verification.log`

### `TC-US001-004` — Callback thành công mở Todo List

- `level`: `integration`
- `priority`: `critical`
- `preconditions`: Authlib callback được mock trả về `userinfo.sub` tổng hợp hợp lệ.
- `steps`:
  1. **Action:** Gửi `GET /auth/google/callback`. **Expected result:** Redirect tới `/todos` và tạo app session.
  2. **Action:** Gửi `GET /todos` với app session. **Expected result:** HTTP 200 và hiển thị Todo List.
  3. **Action:** Lặp callback cùng `google_sub`. **Expected result:** Tái sử dụng một user, không tạo bản ghi trùng.
- `linked_requirement_ids`: `US-001`, `AC-002`, `ARCH-US-001/INT-003`, `ARCH-US-001/MDL-001`
- `result`: `passed`
- `evidence`: `workspace-shared/projects/ToDoApp/qa/QA-US-001-dang-nhap-bang-google/evidence/previous-run/pytest.log`; `workspace-shared/projects/ToDoApp/qa/QA-US-001-dang-nhap-bang-google/evidence/previous-run/independent-verification.log`

### `TC-US001-005` — Session cookie và route guard

- `level`: `security`
- `priority`: `critical`
- `preconditions`: app dùng secret tổng hợp trong test; không in cookie value.
- `steps`:
  1. **Action:** Kiểm tra metadata cookie sau callback. **Expected result:** `HttpOnly`, `SameSite=Lax`, `Max-Age=86400`; có `Secure` khi request HTTPS.
  2. **Action:** Dùng cookie sai chữ ký hoặc payload hết hạn. **Expected result:** `/todos` redirect về Login Page.
  3. **Action:** Giải mã cookie bằng serializer của app trong process test. **Expected result:** Có `user_id`, `expires_at` và TTL 24 giờ.
- `linked_requirement_ids`: `US-001`, `AC-002`, `ARCH-US-001/MDL-002`, `ARCH-US-001/RSK-002`
- `result`: `passed`
- `evidence`: `workspace-shared/projects/ToDoApp/qa/QA-US-001-dang-nhap-bang-google/evidence/previous-run/pytest.log`; `workspace-shared/projects/ToDoApp/qa/QA-US-001-dang-nhap-bang-google/evidence/previous-run/independent-verification.log`

### `TC-US001-006` — Callback bị hủy/thất bại

- `level`: `integration`
- `priority`: `high`
- `preconditions`: không có app session; callback trả `access_denied` hoặc thiếu `userinfo.sub`.
- `steps`:
  1. **Action:** Gọi callback lỗi/hủy. **Expected result:** Quay lại Login Page và hiện thông báo ngắn.
  2. **Action:** Kiểm tra client cookie. **Expected result:** Không tạo app session.
- `linked_requirement_ids`: `US-001`, `AC-002`, `ARCH-US-001/ADR-004`
- `result`: `passed`
- `evidence`: `workspace-shared/projects/ToDoApp/qa/QA-US-001-dang-nhap-bang-google/evidence/previous-run/pytest.log`; `workspace-shared/projects/ToDoApp/qa/QA-US-001-dang-nhap-bang-google/evidence/previous-run/independent-verification.log`

### `TC-US001-007` — Logout cục bộ

- `level`: `integration`
- `priority`: `medium`
- `preconditions`: client có app session hợp lệ.
- `steps`:
  1. **Action:** Gửi `POST /auth/logout`. **Expected result:** Redirect về `/`, xóa `app_session` và không còn truy cập Todo List bằng session đó.
- `linked_requirement_ids`: `US-001`, `ARCH-US-001/INT-004`
- `result`: `passed`
- `evidence`: `workspace-shared/projects/ToDoApp/qa/QA-US-001-dang-nhap-bang-google/evidence/previous-run/pytest.log`

### `TC-US001-008` — Google OAuth E2E thật

- `level`: `end_to_end`
- `priority`: `high`
- `preconditions`: demo environment có HTTPS, Google OAuth client/secret, redirect URI chính xác và test account được phép.
- `steps`:
  1. **Action:** Chọn `Continue with Google`, hoàn tất consent thật. **Expected result:** Google callback được xác minh, app redirect tới Todo List và tạo secure session.
- `linked_requirement_ids`: `US-001`, `AC-002`, `ARCH-US-001/RSK-001`
- `result`: `not_run`
- `evidence`: Không có credential/configuration thật trong môi trường QA; ngoài scope credential-free demo.

## `defects`

Không ghi nhận defect có thể reproduce. Danh sách schema: `[]`.

## `coverage`

| Field | Value |
|---|---:|
| `requirements_percent` | 100 |
| `automated_test_percent` | 87.5 |
| `statement_percent` | 0 |
| `branch_percent` | 0 |

`notes`:

- 100% requirement/AC có test case trace; không đồng nghĩa AC-002 đã được E2E Google thật.
- 7/8 test case được tự động hóa/chạy trong QA; một E2E thật là `not_run`.
- `statement_percent=0` và `branch_percent=0` biểu thị **không đo được**, không phải source có 0% coverage; coverage tool chưa được cài và QA không tự thêm dependency vào build.

## `residual_risks`

### `RISK-QA-US001-001`

- `description`: Chưa xác minh consent, redirect URI, callback, OAuth state và cấu hình client với Google Identity thật.
- `likelihood`: `medium`
- `impact`: `high`
- `mitigation`: Chạy `TC-US001-008` trên demo HTTPS với secret ngoài source trước buổi demo/release có tích hợp Google thật; xác nhận redirect URI kết thúc bằng `/auth/google/callback`.
- `accepted_by`: Chưa được chấp nhận; Product Owner/DevOps cần xác nhận điều kiện trước release.

### `RISK-QA-US001-002`

- `description`: `Secure` phụ thuộc `request.is_secure`; cấu hình reverse proxy/HTTPS forwarding thực tế chưa được xác minh.
- `likelihood`: `medium`
- `impact`: `high`
- `mitigation`: Smoke test response callback qua URL HTTPS của môi trường đích và xác nhận cookie có cờ `Secure`; kiểm tra trusted proxy configuration.
- `accepted_by`: Chưa được chấp nhận; DevOps cần xác nhận trong môi trường đích.

### `RISK-QA-US001-003`

- `description`: Chưa có vulnerability scan, license review và số đo statement/branch coverage độc lập.
- `likelihood`: `low`
- `impact`: `medium`
- `mitigation`: Chạy các quality gate chuẩn của pipeline trước release ngoài scope demo.
- `accepted_by`: Chưa được chấp nhận; Product Owner/DevOps quyết định theo release policy.

## `release_recommendation`

| Field | Value |
|---|---|
| `decision` | `go_with_conditions` |
| `rationale` | 9/9 test repository và 10/10 kiểm tra QA độc lập pass; AC-001, AC-003 và luồng AC-002 ở mức integration có bằng chứng; không có defect. Google E2E thật chưa chạy nên chỉ khuyến nghị đi tiếp có điều kiện. |
| `conditions` | Chạy `TC-US001-008` trước release/demo phụ thuộc Google thật; xác minh `Secure` cookie tại HTTPS edge; giữ credential ngoài source/evidence; chạy security/license gates nếu release policy yêu cầu. |
| `blocking_defect_ids` | `[]` |

## `handoff_to_devops`

| Field | Value |
|---|---|
| `approved_build_identifier` | `IMPL-US-001@00d6ec7` (phê duyệt có điều kiện) |
| `deployment_conditions` | Cấu hình secret ngoài source; HTTPS; Google redirect URI đúng `/auth/google/callback`; hoàn tất `TC-US001-008`; không triển khai production trực tiếp từ QA |
| `required_smoke_test_ids` | `TC-US001-001`, `TC-US001-002`, `TC-US001-004`, `TC-US001-005`, `TC-US001-007`, `TC-US001-008` |
| `known_operational_risks` | Google OAuth/redirect URI chưa E2E; HTTPS proxy có thể ảnh hưởng nhận diện `request.is_secure`; SQLite/scaffold chỉ phù hợp demo theo implementation report |
| `required_monitoring` | Theo dõi sự kiện login success/failure không chứa token/cookie; theo dõi redirect/callback error; xác nhận không log credential hoặc session value |
| `readiness` | `ready_with_conditions` |
