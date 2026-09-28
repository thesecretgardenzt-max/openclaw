# PRODUCT BRIEF – BasicToDoApp

**Version:** 1.0
**Giai đoạn:** MVP
**Nền tảng:** Android

## 1. Tổng quan sản phẩm

| Thuộc tính | Mô tả |
|---|---|
| Tên ứng dụng | BasicToDoApp |
| Nền tảng | Android |
| Loại ứng dụng | Quản lý công việc cá nhân (ToDo List) |
| Phiên bản | 1.0 – MVP |
| Người dùng chính | Cá nhân cần quản lý công việc hằng ngày |
| Mục đích dự án | Thử nghiệm quy trình phát triển ứng dụng Android bằng hệ thống Multi-Agent gồm BA, Architect, Designer, Developer, QA và DevOps. |

## 2. Bối cảnh và bài toán

Người dùng cần một ứng dụng Android đơn giản để ghi lại và quản lý các công việc hằng ngày.

Ứng dụng tập trung vào 4 chức năng: thêm, sửa, xoá và sắp xếp công việc, không tích hợp các tính năng quản lý dự án phức tạp.

Đồng thời, BasicToDoApp được sử dụng làm dự án thử nghiệm quy trình phát triển phần mềm tự động bằng hệ thống 6 AI Agent.

## 3. Mục tiêu sản phẩm

- Xây dựng ứng dụng Android có giao diện đơn giản, dễ sử dụng.
- Cho phép người dùng thêm, sửa, xoá và sắp xếp danh sách công việc theo thời gian.
- Lưu trữ danh sách công việc trên thiết bị để không mất dữ liệu khi đóng ứng dụng.
- Tạo bản APK demo có thể cài đặt và kiểm thử trên Android.
- Kiểm chứng quy trình bàn giao giữa 6 Agent từ phân tích yêu cầu đến phát hành.

## 4. Người dùng chính

Người dùng cá nhân muốn quản lý công việc hằng ngày mà không cần đăng ký tài khoản hoặc sử dụng các tính năng phức tạp.

Ứng dụng chỉ phục vụ một người dùng trên một thiết bị. Không yêu cầu kết nối Internet để sử dụng các chức năng chính.

## 5. Phạm vi chức năng (In Scope)

### F01 – Thêm công việc (Add)

Người dùng nhập tên công việc và thêm vào danh sách. Không cho phép tạo công việc có tên rỗng hoặc chỉ chứa khoảng trắng.

### F02 – Sửa công việc (Edit)

Người dùng chọn một công việc và chỉnh sửa tên. Không cho phép lưu tên công việc rỗng hoặc chỉ chứa khoảng trắng. Nội dung mới hợp lệ được lưu và hiển thị trong danh sách.

### F03 – Xoá công việc (Delete)

Người dùng có thể xoá một công việc. Ứng dụng yêu cầu xác nhận trước khi xoá để tránh thao tác nhầm.

### F04 – Sắp xếp công việc (Sort)

Người dùng có thể sắp xếp danh sách theo thời gian tạo: mới nhất hoặc cũ nhất.

## 6. Ngoài phạm vi (Out of Scope)

Các chức năng sau không được triển khai trong MVP:

- Đăng ký, đăng nhập hoặc quản lý tài khoản.
- Đồng bộ dữ liệu lên cloud.
- Chia sẻ công việc với người khác.
- Đặt deadline, nhắc nhở hoặc thông báo.
- Phân loại công việc, gắn nhãn hoặc thiết lập độ ưu tiên.
- Đánh dấu hoàn thành và thống kê tiến độ.
- Tích hợp AI hoặc các dịch vụ bên thứ ba.

## 7. Yêu cầu giao diện

Ứng dụng sử dụng giao diện Android tối giản, gồm một màn hình chính hiển thị danh sách công việc. Người dùng có thể thực hiện 4 chức năng cốt lõi trực tiếp từ màn hình này.

**Các thành phần giao diện dự kiến:**

- Tiêu đề ứng dụng: `BasicToDoApp`.
- Ô nhập tên công việc và nút thêm (`+`).
- Danh sách hiển thị tên công việc và thời gian tạo.
- Nút sửa và xoá trên từng công việc.
- Bộ chọn sắp xếp: mới nhất, cũ nhất.

## 8. Yêu cầu dữ liệu

Mỗi công việc có các thông tin tối thiểu sau:

| Trường | Mô tả |
|---|---|
| `id` | Mã định danh duy nhất |
| `title` | Tên công việc |
| `createdAt` | Thời gian tạo |
| `updatedAt` | Thời gian chỉnh sửa gần nhất |

Dữ liệu được lưu cục bộ trên thiết bị Android. Việc lựa chọn công nghệ và phương thức lưu trữ cụ thể do Architect Agent quyết định.

## 9. Yêu cầu phi chức năng

| Nhóm | Yêu cầu |
|---|---|
| Hiệu năng | Các thao tác thêm, sửa, xoá và sắp xếp phản hồi nhanh, không gây treo giao diện. |
| Khả dụng | Giao diện dễ hiểu, có thông báo khi nhập dữ liệu không hợp lệ. |
| Lưu trữ | Dữ liệu vẫn tồn tại sau khi đóng và mở lại ứng dụng. |
| Kết nối | Hoạt động offline, không yêu cầu Internet. |
| Bảo mật | Không yêu cầu tài khoản, không thu thập hoặc truyền dữ liệu cá nhân lên máy chủ. |
| Tương thích | Chạy trên Android; phiên bản Android tối thiểu được Architect Agent xác định trong Technical Plan. |

## 10. Tiêu chí thành công (Success Criteria)

- [ ] Thêm công việc hợp lệ thành công; tên rỗng bị từ chối.
- [ ] Chỉnh sửa công việc và hiển thị đúng nội dung mới.
- [ ] Không cho phép lưu tên công việc rỗng khi chỉnh sửa.
- [ ] Xoá đúng công việc sau khi người dùng xác nhận.
- [ ] Sắp xếp chính xác theo thời gian.
- [ ] Dữ liệu không bị mất khi đóng và mở lại ứng dụng.
- [ ] Không yêu cầu lưu lựa chọn sắp xếp sau khi đóng và mở lại ứng dụng.
- [ ] QA xác nhận các Acceptance Criteria đã đạt.
- [ ] Tạo được APK demo và cài đặt thành công trên thiết bị Android tương thích.

## 11. Phạm vi triển khai Multi-Agent

| Agent | Trách nhiệm chính | Sản phẩm bàn giao |
|---|---|---|
| BA | Chuyển Product Brief thành yêu cầu chi tiết. | User Stories, Acceptance Criteria |
| Architect | Thiết kế giải pháp kỹ thuật. | Technical Plan, Task Breakdown |
| Designer | Thiết kế giao diện Android trên Figma. | Figma Design, UI Specifications |
| Developer | Triển khai và build ứng dụng. | Source Code, PR, Build |
| QA | Kiểm thử theo tiêu chí nghiệm thu. | Test Report, Bug List, QA Result |
| DevOps | Đóng gói và phát hành bản demo khi QA đạt. | APK Demo, Runbook, Release Notes |

**Điều kiện hoàn thành:** BasicToDoApp được coi là hoàn thành MVP khi cả 4 chức năng hoạt động đúng, dữ liệu được lưu ổn định, QA xác nhận đạt các tiêu chí nghiệm thu và DevOps tạo thành công bản APK demo.
