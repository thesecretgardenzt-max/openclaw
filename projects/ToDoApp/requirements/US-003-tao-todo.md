# US-003 — Tạo Todo

- **ID:** US-003
- **Name:** Tạo Todo
- **Status:** Ready for Review

## User Story

**As a** người dùng đã đăng nhập  
**I want** tạo Todo bằng một tiêu đề  
**So that** tôi có thể ghi lại việc cần làm của mình.

## Acceptance Criteria

### AC-006 — Hiển thị chức năng tạo Todo

**Given** người dùng đã đăng nhập và đang ở trang Todo  
**When** trang Todo được hiển thị  
**Then** người dùng thấy input nhập Todo và nút `Add`.

### AC-007 — Tạo Todo với tiêu đề hợp lệ

**Given** người dùng đã đăng nhập và đã nhập tiêu đề không rỗng, không chỉ chứa khoảng trắng  
**When** người dùng chọn `Add`  
**Then** Todo được tạo với tiêu đề đã nhập, thuộc về người dùng đó, có trạng thái ban đầu là `New` và xuất hiện trong danh sách Todo của họ.

### AC-008 — Todo mới có trạng thái New

**Given** người dùng đã tạo Todo thành công  
**When** Todo mới xuất hiện trong danh sách  
**Then** trạng thái ban đầu của Todo là `New`.

### AC-009 — Không tạo Todo khi thiếu tiêu đề

**Given** người dùng đã đăng nhập và input Todo đang rỗng  
**When** người dùng chọn `Add`  
**Then** Todo không được tạo.

### AC-010 — Không tạo Todo với tiêu đề chỉ chứa khoảng trắng

**Given** người dùng đã đăng nhập và input Todo chỉ chứa khoảng trắng  
**When** người dùng chọn `Add`  
**Then** Todo không được tạo.

### AC-011 — Todo tồn tại sau khi refresh

**Given** người dùng đã tạo Todo thành công  
**When** người dùng refresh trang  
**Then** Todo đó vẫn xuất hiện trong danh sách Todo của người dùng.

## Business Rules

### BR-001 — Tiêu đề Todo bắt buộc

Todo Title là bắt buộc và không được chỉ chứa khoảng trắng.

### BR-002 — Quyền sở hữu Todo

Mỗi Todo thuộc về người dùng đã tạo Todo đó.

### BR-003 — Trạng thái ban đầu của Todo mới

Todo mới tạo có trạng thái ban đầu là `New`.

## Open Questions

Không còn open question blocking cho user story này.

## Traceability

- **REQ-003 — Tạo Todo** → **US-003** → **AC-006, AC-007, AC-008, AC-009, AC-010, AC-011**
- **BR-001** liên kết **REQ-003**, **AC-007, AC-009, AC-010**.
- **BR-002** liên kết **REQ-003**, **AC-007**.
- **BR-003** liên kết **REQ-003**, **AC-007, AC-008**.
- **Nguồn:** Product Brief — Core Features/Todo; User Flow; Screens/Todo Page; Business Rules; MVP Success Criteria.
