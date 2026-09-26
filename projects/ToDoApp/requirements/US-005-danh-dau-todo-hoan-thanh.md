# US-005 — Đánh dấu Todo hoàn thành

- **ID:** US-005
- **Name:** Đánh dấu Todo hoàn thành
- **Status:** Ready for Review

## User Story

**As a** người dùng đã đăng nhập  
**I want** đánh dấu Todo của mình là Completed  
**So that** tôi có thể theo dõi việc nào đã hoàn thành.

## Acceptance Criteria

### AC-014 — Hiển thị hành động hoàn thành

**Given** người dùng đã đăng nhập và có Todo trong danh sách  
**When** danh sách Todo được hiển thị  
**Then** mỗi Todo của người dùng có checkbox hoặc hành động Complete.

### AC-015 — Đánh dấu Todo là Completed

**Given** người dùng đã đăng nhập và một Todo của họ chưa ở trạng thái Completed  
**When** người dùng thực hiện hành động Complete cho Todo đó  
**Then** Completed status của Todo được cập nhật thành Completed và được phản ánh trong danh sách.

### AC-016 — Duy trì trạng thái sau khi refresh

**Given** người dùng đã đánh dấu Todo của mình là Completed  
**When** người dùng refresh trang  
**Then** Todo đó vẫn có trạng thái Completed.

### AC-017 — Hoàn tác trạng thái Completed

**Given** người dùng đã đăng nhập và một Todo của họ đang ở trạng thái Completed  
**When** người dùng thực hiện hành động undo hoặc bỏ chọn Completed cho Todo đó  
**Then** Todo được chuyển về trạng thái chưa hoàn thành.

### AC-018 — Duy trì trạng thái sau khi undo

**Given** người dùng đã undo trạng thái Completed của Todo  
**When** người dùng refresh trang  
**Then** Todo đó vẫn ở trạng thái chưa hoàn thành.

### AC-019 — Không cập nhật Todo của người dùng khác

**Given** một Todo thuộc về người dùng khác  
**When** người dùng hiện tại cố gắng cập nhật Todo đó  
**Then** Completed status của Todo không bị thay đổi.

## Business Rules

### BR-002 — Quyền sở hữu Todo

Người dùng chỉ được update Todo do chính họ tạo.

### BR-005 — Cho phép undo Completed

Người dùng được phép chuyển Todo của mình từ Completed về trạng thái chưa hoàn thành.

## Open Questions

Không còn open question blocking cho user story này.

## Traceability

- **REQ-005 — Đánh dấu Todo hoàn thành** → **US-005** → **AC-014, AC-015, AC-016, AC-017, AC-018, AC-019**
- **BR-002** liên kết **REQ-005**, **AC-019**.
- **BR-005** liên kết **REQ-005**, **AC-017, AC-018**.
- **Nguồn:** Product Brief — Core Features/Todo; User Flow; Screens/Todo Page; Business Rules/Ownership; MVP Success Criteria.
