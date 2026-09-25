# US-004 — Xem danh sách Todo

- **ID:** US-004
- **Name:** Xem danh sách Todo
- **Status:** Ready for Review

## User Story

**As a** người dùng đã đăng nhập  
**I want** xem danh sách Todo của mình  
**So that** tôi có thể theo dõi các việc cần làm và trạng thái hoàn thành của chúng.

## Acceptance Criteria

### AC-011 — Hiển thị danh sách Todo

**Given** người dùng đã đăng nhập và có Todo  
**When** người dùng mở trang Todo  
**Then** danh sách hiển thị các Todo của người dùng với Title và Completed status.

### AC-012 — Chỉ hiển thị Todo thuộc sở hữu của người dùng

**Given** hệ thống có Todo thuộc nhiều người dùng  
**When** một người dùng đã đăng nhập xem danh sách Todo  
**Then** người dùng chỉ thấy các Todo do chính họ tạo.

### AC-013 — Duy trì danh sách sau khi refresh

**Given** người dùng đã đăng nhập và danh sách của họ có Todo  
**When** người dùng refresh trang Todo  
**Then** các Todo của người dùng vẫn xuất hiện trong danh sách.

### AC-014 — Sắp xếp Todo theo ngày tạo mới nhất

**Given** người dùng đã đăng nhập và có nhiều Todo  
**When** danh sách Todo được hiển thị  
**Then** các Todo được sắp xếp theo ngày tạo từ mới nhất đến cũ nhất.

## Business Rules

### BR-002 — Quyền sở hữu Todo

Mỗi Todo thuộc về người dùng đã tạo Todo đó; người dùng chỉ được xem Todo của mình.

### BR-004 — Thứ tự hiển thị Todo

Danh sách Todo được hiển thị theo ngày tạo từ mới nhất đến cũ nhất.

## Open Questions

Không còn open question blocking cho user story này.

## Traceability

- **REQ-004 — Xem danh sách Todo** → **US-004** → **AC-011, AC-012, AC-013, AC-014**
- **BR-002** liên kết **REQ-004**, **AC-012**.
- **BR-004** liên kết **REQ-004**, **AC-014**.
- **Nguồn:** Product Brief — Core Features/Todo; Screens/Todo Page; Business Rules/Ownership; MVP Success Criteria.
