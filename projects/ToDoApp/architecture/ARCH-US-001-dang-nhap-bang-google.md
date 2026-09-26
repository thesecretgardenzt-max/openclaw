# ARCH-US-001 — Đăng nhập bằng Google

## Thông tin artifact

| Trường schema | Giá trị |
|---|---|
| `schema_version` | `1.0.0` |
| `artifact_type` | `technical_architecture` |
| `artifact_id` | `ARCH-US-001` |
| `project_id` | `TODO-LIST-DEMO` |
| `source_ba_artifact.artifact_id` | `US-001` |
| `source_ba_artifact.artifact_type` | `business_analysis` |
| `source_ba_artifact.schema_version` | `markdown-user-story` |
| Nguồn | `workspace-shared/projects/ToDoApp/requirements/US-001-dang-nhap-bang-google.md` |

## `architecture_summary`

Đây là kiến trúc tối thiểu cho app demo kiểm thử quy trình agent. Python app dùng Authlib để đăng nhập Google qua ba route:

- `GET /auth/google/start`
- `GET /auth/google/callback`
- `POST /auth/logout`

Login thành công: lấy `google_sub`, tìm hoặc tạo user nội bộ, phát hành signed cookie chứa `user_id` và `expires_at`, TTL 24 giờ, rồi chuyển tới Todo List. Login thất bại hoặc bị hủy: quay về Login Page và hiển thị message ngắn. Logout chỉ xóa app session. Không có email/password.

## `architecture_drivers`

- `US-001`, `REQ-001`.
- `AC-001`, `AC-002`, `AC-003`.
- App demo: ưu tiên ít code, ít module và không thêm hạ tầng.
- Quyết định PO: Python, Authlib, routes cố định, `google_sub` unique, signed cookie 24 giờ, fail/cancel message và local logout.

## `system_context`

- **Actors:** Người dùng Todo List.
- **External systems:** Google Identity.
- **Trust boundaries:** Chỉ tin danh tính sau khi Authlib xác minh callback Google; signed cookie phải được Python app kiểm tra trước khi dùng.

## `technology_choices`

| Category | Choice | Version constraint | Rationale | Alternatives considered |
|---|---|---|---|---|
| Backend | Python codebase hiện hữu | Theo project | Đã chốt, không đổi stack | Thêm service mới: không chọn |
| Google login | Authlib | Phiên bản tương thích project | Cách ngắn nhất để tích hợp Google OAuth/OIDC trong Python app | Tự xử lý OAuth: không chọn |
| Session | Signed cookie | Cơ chế ký tương thích Python app | Không cần session store riêng | JWT/server session: không chọn |
| Storage | Database hiện hữu | Theo project | Chỉ cần lưu user và `google_sub` | Database mới: không chọn |

## `components`

### `CMP-001` — Python Web App

- **Responsibility:** Render Login Page, xử lý ba auth routes, tìm/tạo user, phát hành/đọc/xóa signed cookie và điều hướng tới Todo List.
- **Technology:** Python + Authlib + framework hiện hữu.
- **Dependencies:** Google Identity, database hiện hữu.
- **Exposed interfaces:** `INT-001`, `INT-002`, `INT-003`, `INT-004`.
- **Linked requirement IDs:** `US-001`, `AC-001`, `AC-002`, `AC-003`.

> Không tách thêm service, adapter hoặc session store cho demo.

## `data_stores`

### `DST-001` — Database hiện hữu

- **Technology:** Database hiện hữu của Python app.
- **Purpose:** Lưu user nội bộ và `google_sub`.
- **Data classification:** `confidential`.
- **Backup policy:** Dùng policy hiện hữu; demo không thêm policy mới.
- **Retention policy:** Dùng policy hiện hữu; US-001 không yêu cầu xóa user.

### `DST-002` — Signed Cookie

- **Technology:** Signed cookie do Python app phát hành.
- **Purpose:** Giữ app session mà không cần server-side session store.
- **Data classification:** `confidential`.
- **Backup policy:** Không áp dụng.
- **Retention policy:** Hết hạn sau 24 giờ; logout xóa cookie.

## `data_models`

### `MDL-001` — User

- **Store ID:** `DST-001`.
- **Description:** User nội bộ tối thiểu cho Google login.

| Field | Type | Required | Description |
|---|---|---:|---|
| `id` | Kiểu ID hiện hữu | Có | Primary key nội bộ. |
| `google_sub` | string | Có | Google subject; `UNIQUE`, `NOT NULL`. |

- **Relationships:** Một `google_sub` ánh xạ tới một user.

### `MDL-002` — Session Cookie

- **Store ID:** `DST-002`.
- **Description:** Payload signed cookie tối thiểu.

| Field | Type | Required | Description |
|---|---|---:|---|
| `user_id` | Kiểu ID hiện hữu | Có | Tham chiếu `MDL-001.id`. |
| `expires_at` | timestamp | Có | Thời điểm hết hạn, bằng lúc phát hành + 24 giờ. |

- **Relationships:** `user_id` tham chiếu một user.

## `interfaces`

| ID | Kind | Provider | Contract | Authentication | Error strategy | Linked requirements |
|---|---|---|---|---|---|---|
| `INT-001` | `ui` | `CMP-001` | Login Page hiển thị tên app và nút `Continue with Google`; không có email/password | Public | Fail/cancel hiển thị “Đăng nhập Google không thành công. Vui lòng thử lại.” gần nút Google | `AC-001`, `AC-003` |
| `INT-002` | `http_api` | `CMP-001` | `GET /auth/google/start` dùng Authlib để bắt đầu Google login | Public | Lỗi quay về Login Page với message ngắn | `AC-002` |
| `INT-003` | `http_api` | `CMP-001` | `GET /auth/google/callback` dùng Authlib xác minh callback, tìm/tạo user theo `google_sub`, phát hành cookie và chuyển tới Todo List | Google callback được Authlib xác minh | Fail/cancel không tạo session; quay về Login Page | `AC-002` |
| `INT-004` | `http_api` | `CMP-001` | `POST /auth/logout` xóa app session cookie và quay về Login Page | App session nếu có | Xóa cookie theo cách idempotent | `US-001` |

## `cross_cutting_concerns`

- **Security:** Authlib xác minh callback; `google_sub` lấy từ kết quả đã xác minh; cookie được ký, có `HttpOnly`, `Secure` khi chạy HTTPS và `SameSite` phù hợp; không log token/cookie value.
- **Privacy:** Chỉ lưu `google_sub` và dữ liệu session tối thiểu.
- **Observability:** Log success/failure ở mức sự kiện, không log dữ liệu nhạy cảm.
- **Resilience:** Lỗi Google không tạo session và quay về Login Page.
- **Performance:** Không có tối ưu riêng cho demo.
- **Scalability:** Không thêm hạ tầng scale.
- **Accessibility:** Nút Google có accessible name và dùng được bằng bàn phím.
- **Configuration management:** Chỉ dùng tên config hoặc SecretRef redacted, ví dụ `GOOGLE_CLIENT_ID=[REDACTED]`, `GOOGLE_CLIENT_SECRET=[REDACTED]`, `SESSION_SECRET=[REDACTED]`; không lưu giá trị thật trong artifact hoặc source.

## `deployment_topology`

- **Environments:** Environment demo hiện hữu.
- **Nodes:** Một Python app, database hiện hữu và Google Identity.
- **Networking:** HTTPS cho auth flow khi chạy ngoài local test.
- **Scaling strategy:** Không có thay đổi cho demo.
- **Availability strategy:** Không thêm HA/failover.

## `architecture_decisions`

### `ADR-001` — Một Python app, không tách lớp/service không cần thiết

- **Status:** `accepted`.
- **Context:** Đây là app test workflow.
- **Decision:** Login UI, Authlib routes, user lookup và signed cookie nằm trong Python app hiện hữu.
- **Consequences:** Ít file và dependency; phù hợp demo, không hướng tới kiến trúc phân tán.

### `ADR-002` — Authlib và routes cố định

- **Status:** `accepted`.
- **Context:** PO đã chốt SDK và routes.
- **Decision:** Dùng `GET /auth/google/start`, `GET /auth/google/callback`, `POST /auth/logout`.
- **Consequences:** Developer không cần thiết kế thêm API.

### `ADR-003` — User và session tối thiểu

- **Status:** `accepted`.
- **Context:** Demo chỉ cần xác định user và duy trì đăng nhập.
- **Decision:** User có `google_sub` unique not null; signed cookie chỉ chứa `user_id`, `expires_at`, TTL 24 giờ.
- **Consequences:** Không cần profile sync hoặc session store.

### `ADR-004` — Luồng lỗi và logout đơn giản

- **Status:** `accepted`.
- **Context:** PO đã chốt hành vi demo.
- **Decision:** Fail/cancel quay về Login Page với message ngắn gần nút Google; logout chỉ xóa app session.
- **Consequences:** Không thêm error page, retry service hoặc Google revoke flow.

## `technical_risks`

| ID | Description | Likelihood | Impact | Mitigation | Owner |
|---|---|---|---|---|---|
| `RSK-001` | Cấu hình Google/redirect URI sai làm demo login không chạy. | medium | high | Kiểm tra redirect URI khớp `/auth/google/callback`; dùng config redacted. | Developer |
| `RSK-002` | Cookie ký hoặc TTL sai làm session không hợp lệ. | low | medium | Test payload, chữ ký và expiry 24 giờ. | Developer |

## `implementation_sequence`

### `ARCH-TASK-001` — Thêm user identity và Login Page

- **Objective:** Chuẩn bị UI và dữ liệu tối thiểu.
- **Related requirement:** `AC-001`, `AC-003`.
- **Scope:** Đảm bảo User có `google_sub` unique not null; render tên app, nút Google và vùng message; không có email/password.
- **Dependencies:** Không.
- **Acceptance criteria:** Login Page đúng AC-001/AC-003; user lookup theo `google_sub` hoạt động.
- **Required tests:** Test UI cơ bản và unique `google_sub`.
- **Component IDs:** `CMP-001`.

### `ARCH-TASK-002` — Implement Google login và session

- **Objective:** Hoàn thành happy path và fail/cancel.
- **Related requirement:** `AC-002`.
- **Scope:** Dùng Authlib implement start/callback; tìm hoặc tạo user; phát hành signed cookie `user_id`, `expires_at` TTL 24 giờ; fail/cancel quay về Login Page với message ngắn.
- **Dependencies:** `ARCH-TASK-001`.
- **Acceptance criteria:** Google login thành công mở Todo List; fail/cancel không tạo session; cookie đúng payload và TTL.
- **Required tests:** Mock Authlib callback success/fail/cancel; test cookie và route guard.
- **Component IDs:** `CMP-001`.

### `ARCH-TASK-003` — Implement logout và smoke test

- **Objective:** Hoàn tất demo US-001.
- **Related requirement:** `US-001`, `AC-001`, `AC-002`, `AC-003`.
- **Scope:** Implement `POST /auth/logout`; chạy smoke test ba AC.
- **Dependencies:** `ARCH-TASK-002`.
- **Acceptance criteria:** Logout xóa app session; AC-001–AC-003 pass; không có email/password.
- **Required tests:** Route test logout và một smoke/integration test happy path.
- **Component IDs:** `CMP-001`.

## `handoff_to_developer`

- **Required component IDs:** `CMP-001`.
- **Required interface IDs:** `INT-001`, `INT-002`, `INT-003`, `INT-004`.
- **Coding constraints:** Chỉ US-001; Python + Authlib; giữ đúng routes, `google_sub`, cookie payload và TTL; không thêm provider hoặc email/password; không đưa dữ liệu nhạy cảm thật vào code/log/artifact.
- **Quality gates:** Test Login Page; mock callback success/fail/cancel; test cookie TTL; test logout; chạy test suite liên quan.
- **Unresolved decisions:** Không có decision blocker cho demo. Developer dùng framework, ORM và test conventions sẵn có của Python codebase.
- **Readiness:** `ready`.

## Ma trận traceability

| Requirement | Implementation | Test |
|---|---|---|
| `AC-001` | Login Page có tên app và nút Google | UI test |
| `AC-002` | Authlib callback → user → signed cookie → Todo List | Integration test |
| `AC-003` | Chỉ có Google login, không có email/password | UI assertion |
