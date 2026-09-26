# US-006 — Xóa Todo

- **ID:** US-006
- **Name:** Xóa Todo
- **Status:** Ready for Review

## User Story

**As a** người dùng đã đăng nhập  
**I want** xóa Todo của mình  
**So that** tôi có thể loại bỏ việc không còn cần theo dõi.

## Acceptance Criteria

### AC-018 — Hiển thị hành động xóa

**Given** người dùng đã đăng nhập và có Todo trong danh sách  
**When** danh sách Todo được hiển thị  
**Then** mỗi Todo của người dùng có hành động Delete.

### AC-019 — Xóa Todo thuộc sở hữu của người dùng

**Given** người dùng đã đăng nhập và một Todo thuộc về họ  
**When** người dùng thực hiện hành động Delete cho Todo đó  
**Then** hệ thống yêu cầu người dùng xác nhận trước khi xóa Todo.

### AC-020 — Xác nhận và xóa Todo

**Given** người dùng đã chọn Delete cho Todo thuộc về họ và hộp xác nhận đang hiển thị  
**When** người dùng xác nhận xóa  
**Then** Todo bị xóa và không còn xuất hiện trong danh sách của người dùng.

### AC-021 — Hủy xác nhận xóa

**Given** người dùng đã chọn Delete cho Todo thuộc về họ và hộp xác nhận đang hiển thị  
**When** người dùng hủy thao tác xóa  
**Then** Todo không bị xóa và vẫn xuất hiện trong danh sách của người dùng.

### AC-022 — Duy trì kết quả xóa sau khi refresh

**Given** người dùng đã xóa Todo thành công  
**When** người dùng refresh trang  
**Then** Todo đã xóa không xuất hiện lại trong danh sách.

### AC-023 — Không xóa Todo của người dùng khác

**Given** một Todo thuộc về người dùng khác  
**When** người dùng hiện tại cố gắng xóa Todo đó  
**Then** Todo không bị xóa.

## Business Rules

### BR-002 — Quyền sở hữu Todo

Người dùng chỉ được delete Todo do chính họ tạo.

### BR-006 — Xác nhận trước khi xóa

Người dùng phải xác nhận trước khi Todo bị xóa.

## Open Questions

Không còn open question blocking cho user story này.

## Traceability

- **REQ-006 — Xóa Todo** → **US-006** → **AC-018, AC-019, AC-020, AC-021, AC-022, AC-023**
- **BR-002** liên kết **REQ-006**, **AC-023**.
- **BR-006** liên kết **REQ-006**, **AC-019, AC-020, AC-021**.
- **Nguồn:** Product Brief — Core Features/Todo; User Flow; Screens/Todo Page; Business Rules/Ownership; MVP Success Criteria.
