# QA-US-001 — Báo cáo QA độc lập lần chạy lại

## `schema_version`

`1.0.0`

## `artifact_type`

`qa_report`

## `artifact_id`

`QA-US-001-RERUN-20260926`

## `project_id`

`TODO-LIST-DEMO`

## `source_ba_artifact`

| Field | Value |
|---|---|
| `artifact_id` | `US-001` |
| `artifact_type` | `business_analysis` |
| `schema_version` | `markdown-user-story` |

Nguồn: `requirements/US-001-dang-nhap-bang-google.md`.

## `source_developer_artifact`

| Field | Value |
|---|---|
| `artifact_id` | `IMPL-US-001` |
| `artifact_type` | `implementation_report` |
| `schema_version` | `1.0.0` |

Nguồn: `implementation/IMPL-US-001-dang-nhap-bang-google.md`.

## `test_summary`

| Field | Value |
|---|---:|
| `executed` | 7 |
| `passed` | 7 |
| `failed` | 0 |
| `blocked` | 0 |
| `skipped` | 1 |
| `overall_status` | `passed_with_risks` |

**Overall theo quy ước QA: `PASS` với residual risk.** Bảy test case credential-free đã chạy đều pass. `TC-US001-008` là `not_run` vì môi trường QA không có cấu hình Google OAuth thật; đây là giới hạn ngoài demo scope theo chỉ dẫn, không được tính là defect hay failed test.

## `test_environment`

| Field | Value |
|---|---|
| `name` | QA local độc lập — rerun 2026-09-26 |
| `build_identifier` | `IMPL-US-001@00d6ec7` |
| `platform` | macOS, Python 3.14.5, Flask 3.1.2, Authlib 1.6.4, pytest 9.1.1, SQLite |
| `configuration` | Flask test client; database tạm; OAuth client được mock tại biên Authlib; HTTPS được mô phỏng để kiểm tra cờ cookie |
| `test_data` | Dữ liệu tổng hợp, không dùng dữ liệu người dùng thật và không lưu giá trị xác thực trong evidence |
| `limitations` | Không có môi trường Google OAuth thật; không đo statement/branch coverage vì module coverage không có trong môi trường hiện hữu; không chạy vulnerability scanner hoặc license review |

## `quality_gates`

| `name` | `criterion` | `result` | `evidence` |
|---|---|---|---|
| Acceptance gate | AC-001–AC-003 có test traceable; mọi test credential-free đã chạy đều pass | `passed` | `qa/QA-US-001-dang-nhap-bang-google/evidence/pytest.log`; `qa/QA-US-001-dang-nhap-bang-google/evidence/independent-verification.log` |
| Build gate | Source compile được và dependency nhất quán | `passed` | `qa/QA-US-001-dang-nhap-bang-google/evidence/build-verification.log` |
| Session/security gate | Session ký, TTL 24 giờ, cờ HttpOnly/SameSite/Secure trên HTTPS; giá trị sai hoặc hết hạn không cấp quyền | `passed` | `qa/QA-US-001-dang-nhap-bang-google/evidence/independent-verification.log` |
| Source-integrity gate | File app/developer không đổi trong lần QA rerun | `passed` | `qa/QA-US-001-dang-nhap-bang-google/evidence/source-integrity.log`; `qa/QA-US-001-dang-nhap-bang-google/evidence/source-before.sha256`; `qa/QA-US-001-dang-nhap-bang-google/evidence/source-after.sha256` |
| Artifact-safety gate | Artifact dùng đường dẫn tương đối và không chứa giá trị nhạy cảm | `passed` | `qa/QA-US-001-dang-nhap-bang-google/evidence/artifact-validation.log` |
| Google OAuth E2E gate | Hoàn tất consent/redirect/callback với Google Identity thật | `not_evaluated` | `TC-US001-008` là `not_run`; không có cấu hình OAuth thật trong môi trường QA |
| Code-coverage gate | Có số đo statement và branch coverage | `not_evaluated` | `qa/QA-US-001-dang-nhap-bang-google/evidence/coverage.log` |

## `requirement_traceability`

| `requirement_id` | `acceptance_criterion_ids` | `test_case_ids` | `coverage_status` |
|---|---|---|---|
| `US-001` | `AC-001`, `AC-002`, `AC-003` | `TC-US001-001`–`TC-US001-008` | `partially_covered` |
| `AC-001` | `AC-001` | `TC-US001-001` | `covered` |
| `AC-002` | `AC-002` | `TC-US001-003`, `TC-US001-004`, `TC-US001-005`, `TC-US001-006`, `TC-US001-007`, `TC-US001-008` | `partially_covered` |
| `AC-003` | `AC-003` | `TC-US001-002` | `covered` |

`AC-002` pass ở mức integration với biên Authlib được mock; E2E Google thật chưa chạy nên coverage tổng thể được ghi trung thực là `partially_covered`.

## `test_cases`

### `TC-US001-001` — Trang đăng nhập cho người dùng chưa xác thực

- `level`: `acceptance`
- `priority`: `critical`
- `preconditions`: Không có app session.
- `steps`:
  1. **Action:** Gửi `GET /`. **Expected result:** HTTP 200, có tên `Todo List` và CTA `Continue with Google`.
- `linked_requirement_ids`: `US-001`, `AC-001`
- `result`: `passed`
- `evidence`: `qa/QA-US-001-dang-nhap-bang-google/evidence/pytest.log`, `qa/QA-US-001-dang-nhap-bang-google/evidence/independent-verification.log`

### `TC-US001-002` — Chỉ cung cấp đăng nhập Google

- `level`: `acceptance`
- `priority`: `high`
- `preconditions`: Người dùng ở Login Page.
- `steps`:
  1. **Action:** Kiểm tra control đăng nhập. **Expected result:** Có Google CTA; không có control đăng nhập email/password.
- `linked_requirement_ids`: `US-001`, `AC-003`
- `result`: `passed`
- `evidence`: `qa/QA-US-001-dang-nhap-bang-google/evidence/pytest.log`, `qa/QA-US-001-dang-nhap-bang-google/evidence/independent-verification.log`

### `TC-US001-003` — Contract route OAuth

- `level`: `contract`
- `priority`: `high`
- `preconditions`: OAuth client dùng test double, không gọi mạng.
- `steps`:
  1. **Action:** Gửi `GET /auth/google/start`. **Expected result:** Callback URL kết thúc bằng `/auth/google/callback`.
  2. **Action:** Kiểm tra route map. **Expected result:** Start/callback dùng GET; logout chỉ dùng POST.
- `linked_requirement_ids`: `US-001`, `AC-002`, `ARCH-US-001/INT-002`, `ARCH-US-001/INT-003`, `ARCH-US-001/INT-004`
- `result`: `passed`
- `evidence`: `qa/QA-US-001-dang-nhap-bang-google/evidence/pytest.log`, `qa/QA-US-001-dang-nhap-bang-google/evidence/independent-verification.log`

### `TC-US001-004` — Callback thành công mở Todo List

- `level`: `integration`
- `priority`: `critical`
- `preconditions`: Authlib callback được mock trả identity tổng hợp hợp lệ.
- `steps`:
  1. **Action:** Gọi callback thành công. **Expected result:** Redirect tới `/todos` và tạo app session.
  2. **Action:** Truy cập `/todos`. **Expected result:** HTTP 200 và hiển thị Todo List.
  3. **Action:** Lặp callback cùng identity. **Expected result:** Tái sử dụng một user, không tạo bản ghi trùng.
- `linked_requirement_ids`: `US-001`, `AC-002`, `ARCH-US-001/INT-003`, `ARCH-US-001/MDL-001`
- `result`: `passed`
- `evidence`: `qa/QA-US-001-dang-nhap-bang-google/evidence/pytest.log`, `qa/QA-US-001-dang-nhap-bang-google/evidence/independent-verification.log`

### `TC-US001-005` — Session cookie và route guard

- `level`: `security`
- `priority`: `critical`
- `preconditions`: Test dùng dữ liệu tổng hợp; evidence không in giá trị cookie.
- `steps`:
  1. **Action:** Kiểm tra metadata cookie trên HTTPS mô phỏng. **Expected result:** Có `HttpOnly`, `Secure`, `SameSite=Lax`, `Max-Age=86400`.
  2. **Action:** Kiểm tra payload trong process test. **Expected result:** Có `user_id`, `expires_at`, TTL 24 giờ.
  3. **Action:** Dùng cookie sai chữ ký hoặc hết hạn. **Expected result:** `/todos` redirect về Login Page.
- `linked_requirement_ids`: `US-001`, `AC-002`, `ARCH-US-001/MDL-002`, `ARCH-US-001/RSK-002`
- `result`: `passed`
- `evidence`: `qa/QA-US-001-dang-nhap-bang-google/evidence/pytest.log`, `qa/QA-US-001-dang-nhap-bang-google/evidence/independent-verification.log`

### `TC-US001-006` — Callback bị hủy/thất bại

- `level`: `integration`
- `priority`: `high`
- `preconditions`: Không có app session; callback báo hủy hoặc thiếu identity.
- `steps`:
  1. **Action:** Gọi callback lỗi/hủy. **Expected result:** Quay lại Login Page, hiện thông báo ngắn và không tạo app session.
- `linked_requirement_ids`: `US-001`, `AC-002`, `ARCH-US-001/ADR-004`
- `result`: `passed`
- `evidence`: `qa/QA-US-001-dang-nhap-bang-google/evidence/pytest.log`, `qa/QA-US-001-dang-nhap-bang-google/evidence/independent-verification.log`

### `TC-US001-007` — Logout cục bộ

- `level`: `integration`
- `priority`: `medium`
- `preconditions`: Client có app session hợp lệ.
- `steps`:
  1. **Action:** Gửi `POST /auth/logout`. **Expected result:** Redirect về `/` và xóa app session.
- `linked_requirement_ids`: `US-001`, `AC-002`, `ARCH-US-001/INT-004`
- `result`: `passed`
- `evidence`: `qa/QA-US-001-dang-nhap-bang-google/evidence/pytest.log`, `qa/QA-US-001-dang-nhap-bang-google/evidence/independent-verification.log`

### `TC-US001-008` — Google OAuth E2E thật

- `level`: `end_to_end`
- `priority`: `high`
- `preconditions`: Demo HTTPS có cấu hình Google OAuth thật, redirect URI chính xác và tài khoản test được phép.
- `steps`:
  1. **Action:** Chọn `Continue with Google` và hoàn tất consent. **Expected result:** Callback được Google xác minh, app redirect tới Todo List và tạo secure session.
- `linked_requirement_ids`: `US-001`, `AC-002`, `ARCH-US-001/RSK-001`
- `result`: `not_run`
- `evidence`: Không có cấu hình OAuth thật trong môi trường QA; residual risk được ghi nhận, không tính là failure trong demo scope.

## `defects`

Không ghi nhận defect có thể reproduce. Danh sách schema: `[]`. Xem `qa/QA-US-001-dang-nhap-bang-google/buglist.md`.

## `coverage`

| Field | Value |
|---|---:|
| `requirements_percent` | 100 |
| `automated_test_percent` | 87.5 |
| `statement_percent` | 0 |
| `branch_percent` | 0 |

`notes`:

- 100% requirement/AC có test case trace; không đồng nghĩa Google E2E thật đã chạy.
- 7/8 test case được chạy bằng kiểm thử tự động/độc lập; một E2E thật là `not_run`.
- `statement_percent=0` và `branch_percent=0` biểu thị **không đo**, không phải source có 0% coverage; môi trường hiện hữu không có module coverage.

## `residual_risks`

### `RISK-QA-US001-001`

- `description`: Chưa xác minh consent, redirect URI, callback, OAuth state và cấu hình client với Google Identity thật.
- `likelihood`: `medium`
- `impact`: `high`
- `mitigation`: Chạy `TC-US001-008` trên demo HTTPS đã cấu hình trước release/demo phụ thuộc Google thật.
- `accepted_by`: Chưa được chấp nhận; Product Owner/DevOps cần quyết định trước release tích hợp thật.

### `RISK-QA-US001-002`

- `description`: Cờ `Secure` phụ thuộc việc ứng dụng nhận diện request HTTPS; reverse proxy thực tế chưa được xác minh.
- `likelihood`: `medium`
- `impact`: `high`
- `mitigation`: Smoke test callback trên URL HTTPS đích và xác nhận header cookie có cờ `Secure`; kiểm tra cấu hình trusted proxy.
- `accepted_by`: Chưa được chấp nhận; DevOps cần xác nhận ở môi trường đích.

### `RISK-QA-US001-003`

- `description`: Chưa có vulnerability scan, license review và số đo statement/branch coverage độc lập.
- `likelihood`: `low`
- `impact`: `medium`
- `mitigation`: Chạy quality gate chuẩn của pipeline nếu release policy yêu cầu.
- `accepted_by`: Chưa được chấp nhận; Product Owner/DevOps quyết định theo release policy.

## `release_recommendation`

| Field | Value |
|---|---|
| `decision` | `go_with_conditions` |
| `rationale` | 9/9 test repository và 12/12 kiểm tra QA độc lập pass; AC-001, AC-003 và AC-002 ở mức integration có bằng chứng; không có defect. Google E2E thật chưa chạy nên chỉ khuyến nghị đi tiếp có điều kiện. |
| `conditions` | Chạy `TC-US001-008` trước release/demo phụ thuộc Google thật; xác minh cờ `Secure` tại HTTPS edge; giữ mọi giá trị nhạy cảm ngoài source/evidence; chạy security/license gates nếu policy yêu cầu. |
| `blocking_defect_ids` | `[]` |

## `handoff_to_devops`

| Field | Value |
|---|---|
| `approved_build_identifier` | `IMPL-US-001@00d6ec7` (phê duyệt có điều kiện) |
| `deployment_conditions` | Cấu hình giá trị xác thực ngoài source; dùng HTTPS; redirect URI đúng `/auth/google/callback`; hoàn tất `TC-US001-008`; QA không tự deploy production |
| `required_smoke_test_ids` | `TC-US001-001`, `TC-US001-002`, `TC-US001-004`, `TC-US001-005`, `TC-US001-007`, `TC-US001-008` |
| `known_operational_risks` | Google OAuth/redirect URI chưa E2E; HTTPS proxy có thể ảnh hưởng nhận diện request secure; SQLite/scaffold chỉ phù hợp demo theo implementation report |
| `required_monitoring` | Theo dõi sự kiện login success/failure không chứa dữ liệu nhạy cảm; theo dõi redirect/callback error; kiểm tra log không ghi giá trị xác thực hoặc session |
| `readiness` | `ready_with_conditions` |
