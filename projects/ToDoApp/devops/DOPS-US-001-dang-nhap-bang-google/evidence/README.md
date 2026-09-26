# Evidence DevOps US-001

Evidence được tạo ngày 2026-09-26 cho build `IMPL-US-001@00d6ec7`.

| File | Nội dung |
|---|---|
| `pytest.log` | Chạy lại test tự động. |
| `build-dependency-check.log` | Compile Python và kiểm tra dependency consistency. |
| `local-smoke.log` | Smoke Login Page, route guard và route/method contract bằng Flask test client. |
| `https-cookie-smoke.log` | Smoke callback mock trên HTTPS mô phỏng; không in cookie/token. |
| `config-validation.log` | Xác minh fail-fast khi thiếu tên cấu hình bắt buộc. |
| `security-hygiene.log` | Kiểm tra dấu hiệu credential trong source runtime; ghi rõ gate chưa chạy. |
| `app-integrity.log` | Xác nhận không có thay đổi DevOps trong `workspace-shared/projects/ToDoApp/app`. |
| `report-validation.log` | Kiểm tra section schema, status, Runbook, reference Release Note, đường dẫn tương đối, điều kiện release và dấu hiệu secret trong ba artifact DevOps. |
| `organization-validation.log` | Xác minh cấu trúc đích, dọn đường dẫn cũ, reference mới và không có đường dẫn tuyệt đối. |
| `path-reorg-validation.log` | Tổng hợp validation việc tổ chức lại đường dẫn và giữ nguyên trạng thái phát hành. |
| `checksum-verification.log` | Kết quả xác minh checksum evidence và ba artifact; file này và `SHA256SUMS.txt` không tự checksum. |
| `SHA256SUMS.txt` | Checksum evidence, report chính, runbook và release note sau khi hoàn tất. |

Artifact được kiểm tra: `devops/DOPS-US-001-dang-nhap-bang-google/devops.md`, `devops/DOPS-US-001-dang-nhap-bang-google/runbook.md`, `devops/DOPS-US-001-dang-nhap-bang-google/release-note.md`.

Không dùng credential thật, không gọi Google Identity và không ghi token/cookie/session value. Giá trị dùng trong smoke test là dữ liệu tổng hợp. Google OAuth E2E thật và xác minh cookie tại HTTPS edge/reverse proxy thật chưa chạy.
