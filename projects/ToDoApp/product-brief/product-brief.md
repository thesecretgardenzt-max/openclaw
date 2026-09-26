# Product Brief – Todo List Demo App

## 1. Mục tiêu

Tạo một web app Todo List rất đơn giản để thử nghiệm.

---

## 2. Core Features

App chỉ cần các chức năng sau:

### Authentication

* Đăng nhập bằng Google.
* Đăng xuất.

Không cần email/password.

---

### Todo

User có thể:

* Tạo Todo.
* Xem danh sách Todo.
* Đánh dấu Todo là Completed.
* Xóa Todo.

Mỗi Todo chỉ cần:

* Title.
* Completed status.

---

## 3. User Flow

```text
Open App
↓
Login with Google
↓
Todo List
↓
Create Todo
↓
Mark Complete / Delete
↓
Logout
```

---

## 4. Screens

### Login Page

Bao gồm:

* App name.
* Button: `Continue with Google`.

### Todo Page

Bao gồm:

* Input nhập Todo.
* Button `Add`.
* Danh sách Todo.
* Checkbox hoặc action Complete.
* Delete action.
* Logout.

---

## 5. Business Rules

### Todo Title

* Bắt buộc.
* Không được chỉ chứa khoảng trắng.

### Ownership

Mỗi Todo thuộc về user đã tạo Todo đó.

User chỉ được:

* Xem Todo của mình.
* Update Todo của mình.
* Delete Todo của mình.

---

## 6. MVP Success Criteria

Ứng dụng đạt yêu cầu khi:

* User đăng nhập được bằng Google.
* User logout được.
* User tạo được Todo.
* Todo vẫn tồn tại sau khi refresh trang.
* User mark Todo completed được.
* User delete Todo được.
* User chỉ thấy Todo của chính mình.
* App deploy thành công và có URL demo.

---

## 7. Out of Scope

Không làm:

* Description.
* Due Date.
* Overdue.
* Filter.
* Search.
* Priority.
* Tags.
* Categories.
* Subtasks.
* Reminder.
* Notifications.
* Dark Mode.
* Drag & Drop.
* Team collaboration.
* Sharing.
* Profile management.
* Account deletion.
* AI features.
* Google Calendar integration.

---

## 8. Technical Scope

Product Brief không quyết định:

* Frontend framework.
* Backend framework.
* Database.
* ORM.
* Authentication library.
* Hosting platform.

Architect sẽ chọn giải pháp kỹ thuật phù hợp.

---

## 9. Constraint

Không tự thêm feature ngoài Product Brief.

Ưu tiên:

```text
Simple
Small
Testable
Deployable
```
