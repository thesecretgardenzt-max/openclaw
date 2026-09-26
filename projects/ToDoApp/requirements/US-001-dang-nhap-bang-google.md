# US-001 — Đăng nhập bằng Google

- **ID:** US-001
- **Name:** Đăng nhập bằng Google

## Title

Đăng nhập bằng Google

## User Story

**As a** người dùng ứng dụng Todo List  
**I want** đăng nhập bằng tài khoản Google  
**So that** tôi có thể truy cập Todo List của mình.

## Acceptance Criteria

### AC-001 — Hiển thị trang đăng nhập

**Given** người dùng chưa đăng nhập  
**When** người dùng mở ứng dụng  
**Then** ứng dụng hiển thị trang đăng nhập có tên ứng dụng và nút `Continue with Google`.

### AC-002 — Đăng nhập thành công

**Given** người dùng đang ở trang đăng nhập  
**When** người dùng chọn `Continue with Google` và hoàn tất đăng nhập Google thành công  
**Then** người dùng được đăng nhập và thấy trang Todo List.

### AC-003 — Phương thức đăng nhập trong phạm vi

**Given** người dùng đang ở trang đăng nhập  
**When** người dùng xem các phương thức đăng nhập  
**Then** ứng dụng cung cấp đăng nhập bằng Google và không cung cấp đăng nhập bằng email/password.

## Assumptions

Không có giả định nào được dùng để bổ sung hành vi ngoài Product Brief.

## Open Questions

### Q-001 — Đăng nhập không thành công

Ứng dụng cần có hành vi và thông báo gì khi đăng nhập Google thất bại hoặc bị người dùng hủy?

- **Blocking:** Không chặn việc chuyển giao yêu cầu đăng nhập thành công; cần được xác nhận trước khi hoàn thiện acceptance criteria cho luồng lỗi.

## Traceability

- **REQ-001 — Hỗ trợ đăng nhập bằng Google** → **US-001** → **AC-001, AC-002, AC-003**
- **Nguồn:** Product Brief — Core Features/Authentication; User Flow; Screens/Login Page; MVP Success Criteria.
