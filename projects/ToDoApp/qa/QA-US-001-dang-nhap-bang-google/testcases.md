# Test cases — US-001 Đăng nhập bằng Google

## Ma trận traceability và kết quả

| Test case | Mục tiêu | Trace | Loại | Kết quả | Evidence |
|---|---|---|---|---|---|
| `TC-US001-001` | Login Page có tên app và nút Google | `US-001`, `AC-001` | acceptance | `passed` | `evidence/pytest.log`, `evidence/independent-verification.log` |
| `TC-US001-002` | Không có đăng nhập email/password | `US-001`, `AC-003` | acceptance | `passed` | `evidence/pytest.log`, `evidence/independent-verification.log` |
| `TC-US001-003` | Route/method và callback contract đúng kiến trúc | `US-001`, `AC-002`, `ARCH-US-001/INT-002`–`INT-004` | contract | `passed` | `evidence/pytest.log`, `evidence/independent-verification.log` |
| `TC-US001-004` | Callback thành công tạo/reuse user, mở Todo List | `US-001`, `AC-002`, `ARCH-US-001/INT-003`, `MDL-001` | integration | `passed` | `evidence/pytest.log`, `evidence/independent-verification.log` |
| `TC-US001-005` | Session ký, TTL/cờ cookie và route guard | `US-001`, `AC-002`, `ARCH-US-001/MDL-002`, `RSK-002` | security | `passed` | `evidence/pytest.log`, `evidence/independent-verification.log` |
| `TC-US001-006` | Hủy/lỗi callback không tạo session | `US-001`, `AC-002`, `ARCH-US-001/ADR-004` | integration | `passed` | `evidence/pytest.log`, `evidence/independent-verification.log` |
| `TC-US001-007` | Logout POST xóa app session | `US-001`, `AC-002`, `ARCH-US-001/INT-004` | integration | `passed` | `evidence/pytest.log`, `evidence/independent-verification.log` |
| `TC-US001-008` | Google OAuth E2E thật | `US-001`, `AC-002`, `ARCH-US-001/RSK-001` | end_to_end | `not_run` | Không có cấu hình OAuth thật trong môi trường QA; residual risk, không phải failure trong demo scope |

## Chi tiết bước kiểm thử

### `TC-US001-001` — Trang đăng nhập

1. Không thiết lập app session.
2. Gửi `GET /`.
3. Xác nhận HTTP 200, có `Todo List` và `Continue with Google`.

**Kết quả:** `passed`.

### `TC-US001-002` — Chỉ có Google login

1. Render Login Page.
2. Kiểm tra có Google CTA.
3. Kiểm tra không có input/control email hoặc password.

**Kết quả:** `passed`.

### `TC-US001-003` — Route contract

1. Dùng test double ở biên Authlib, không gọi mạng.
2. Gọi `GET /auth/google/start`; callback URL phải kết thúc bằng `/auth/google/callback`.
3. Kiểm tra route map: start/callback dùng GET, logout chỉ dùng POST.

**Kết quả:** `passed`.

### `TC-US001-004` — Callback thành công

1. Mock callback trả identity tổng hợp hợp lệ.
2. Gọi callback; xác nhận redirect tới `/todos` và tạo app session.
3. Truy cập `/todos`; xác nhận HTTP 200.
4. Lặp callback cùng identity; xác nhận chỉ một user được lưu.

**Kết quả:** `passed`.

### `TC-US001-005` — Session và guard

1. Gọi callback bằng HTTPS mô phỏng.
2. Xác nhận cookie có `HttpOnly`, `Secure`, `SameSite=Lax`, `Max-Age=86400`.
3. Kiểm tra payload trong process test có `user_id`, `expires_at` và TTL 24 giờ; không in giá trị cookie ra evidence.
4. Gửi cookie sai chữ ký và cookie hết hạn; xác nhận `/todos` redirect về Login Page.

**Kết quả:** `passed`.

### `TC-US001-006` — Hủy/thất bại

1. Gọi callback với trạng thái bị hủy hoặc thiếu identity.
2. Xác nhận quay lại Login Page và có thông báo ngắn.
3. Xác nhận không tạo app session.

**Kết quả:** `passed`.

### `TC-US001-007` — Logout

1. Thiết lập app session tổng hợp hợp lệ.
2. Gửi `POST /auth/logout`.
3. Xác nhận redirect về `/` và app session bị xóa.

**Kết quả:** `passed`.

### `TC-US001-008` — Google OAuth E2E thật

1. Yêu cầu demo HTTPS có cấu hình Google OAuth thật và tài khoản test được phép.
2. Chọn `Continue with Google`, hoàn tất consent.
3. Xác nhận callback được Google xác minh, app mở Todo List và tạo secure session.

**Kết quả:** `not_run` — thiếu môi trường/cấu hình OAuth thật; ngoài credential-free demo scope. Không đánh dấu fail và không suy diễn pass.

## Tổng hợp

- `passed`: 7
- `failed`: 0
- `blocked`: 0
- `not_run`: 1
- Automated repository suite: 9/9 pass.
- Independent QA checks: 12/12 pass.
