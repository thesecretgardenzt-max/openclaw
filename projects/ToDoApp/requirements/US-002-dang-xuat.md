# US-002 — Đăng xuất

- **ID:** US-002
- **Name:** Đăng xuất
- **Status:** Draft

## User Story

**As a** người dùng đã đăng nhập  
**I want** đăng xuất khỏi ứng dụng  
**So that** tôi có thể kết thúc phiên sử dụng Todo List.

## Acceptance Criteria

### AC-004 — Hiển thị hành động đăng xuất

**Given** người dùng đã đăng nhập và đang ở trang Todo  
**When** trang Todo được hiển thị  
**Then** người dùng thấy hành động `Logout`.

### AC-005 — Đăng xuất thành công

**Given** người dùng đã đăng nhập và đang ở trang Todo  
**When** người dùng chọn `Logout`  
**Then** người dùng không còn ở trạng thái đăng nhập và trang Login được hiển thị.

## Business Rules

Không có business rule riêng cho đăng xuất được nêu trong Product Brief.

## Assumptions

Không có giả định nào được dùng để bổ sung hành vi ngoài Product Brief.

## Open Questions

Không có câu hỏi mở chặn phạm vi cơ bản của user story này.

## Traceability

- **REQ-002 — Hỗ trợ đăng xuất** → **US-002** → **AC-004, AC-005**
- **Nguồn:** Product Brief — Core Features/Authentication; User Flow; Screens/Todo Page; MVP Success Criteria.
