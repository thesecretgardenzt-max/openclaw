# QA artifact — US-001 Đăng nhập bằng Google

Bộ artifact này ghi lại lần QA độc lập rerun ngày 2026-09-26 cho build `IMPL-US-001@00d6ec7`.

## Cấu trúc

- `qa.md`: QA report chính theo `qa.schema.json`, Markdown tiếng Việt.
- `testcases.md`: test cases và traceability `US-001`/`AC-001`–`AC-003`.
- `buglist.md`: danh sách defect; hiện tại `none`.
- `evidence/`: log, checksum và validation đã loại bỏ dữ liệu nhạy cảm.
- `evidence/previous-run/`: artifact/evidence cũ được gom lại để không làm mất bằng chứng hữu ích; không phải bằng chứng quyết định của rerun hiện tại.

## Kết quả chính

- Overall: `PASS` với residual risk (`passed_with_risks`).
- Repository test suite: 9/9 pass.
- Independent QA checks: 12/12 pass.
- Defect: none.
- Google OAuth E2E thật: `not_run`; không có môi trường/cấu hình thật và không tính là failure trong demo scope.
- Release recommendation: `go_with_conditions`.

## Nguồn và phạm vi

- `requirements/US-001-dang-nhap-bang-google.md`
- `architecture/ARCH-US-001-dang-nhap-bang-google.md`
- `implementation/IMPL-US-001-dang-nhap-bang-google.md`
- App dưới kiểm thử: `app/`

QA không sửa production code. `evidence/source-integrity.log` và checksum trước/sau xác nhận các file app/developer không đổi trong lần rerun.
