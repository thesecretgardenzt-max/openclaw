# Evidence — QA-US-001 rerun

## Evidence hiện tại

- `environment.log`: phiên bản runtime/test framework.
- `pytest.log`: kết quả repository test suite.
- `independent-verification.log`: 12 kiểm tra QA độc lập.
- `build-verification.log`: compileall và dependency consistency.
- `coverage.log`: trạng thái đo coverage.
- `source-files.txt`: danh sách file được checksum.
- `source-before.sha256`, `source-after.sha256`: checksum trước/sau rerun.
- `source-integrity.log`: kết quả so sánh checksum.
- `artifact-validation.log`: kiểm tra cấu trúc, schema projection, đường dẫn và dữ liệu nhạy cảm.
- `SHA256SUMS.txt`: checksum của artifact/evidence rerun.

## Evidence lịch sử

`previous-run/` chứa output/evidence cũ đã được chuyển từ vị trí legacy. Nội dung này chỉ để bảo toàn lịch sử; quyết định QA hiện tại dựa trên các file evidence ở thư mục này, không dựa trên `previous-run/`.

Không evidence nào được phép chứa giá trị xác thực, token, giá trị cookie/session, private key, file `.env` hoặc đường dẫn tuyệt đối.
