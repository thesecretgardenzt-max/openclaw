# RUNBOOK-US-001 — Đăng nhập bằng Google (local/demo)

## Trạng thái

`AWAITING_APPROVAL`

Runbook này chỉ dành cho local validation và demo environment. Không dùng để deploy production. Build được phép: `IMPL-US-001@00d6ec7`; QA: `QA-US-001` với quyết định `go_with_conditions`.

## 1. Environment

| Môi trường | Mục đích | Yêu cầu | Trạng thái |
|---|---|---|---|
| Local credential-free | Test, compile, smoke bằng mock | Python 3, source tại `workspace-shared/projects/ToDoApp/app/` | `validated` |
| Local OAuth thủ công | Kiểm tra luồng Google cục bộ | SecretRef ngoài source, redirect URI local | `not_run` |
| Demo Google thật | Chạy `TC-US001-008` | HTTPS edge, Google test account, SecretRef, callback chính xác | `blocked` |
| Production | Ngoài phạm vi | Không áp dụng | `not_run` |

Nguồn: `workspace-shared/projects/ToDoApp/qa/QA-US-001-dang-nhap-bang-google/qa.md`, `workspace-shared/projects/ToDoApp/implementation/IMPL-US-001-dang-nhap-bang-google.md`.

## 2. Configuration redacted

| Biến | Giá trị biểu diễn | Nguồn | Bắt buộc |
|---|---|---|---:|
| `GOOGLE_CLIENT_ID` | `[REDACTED]` | SecretRef của local/demo environment | Có |
| `GOOGLE_CLIENT_SECRET` | `[REDACTED]` | SecretRef của local/demo environment | Có |
| `SESSION_SECRET` | `[REDACTED]` | SecretRef của local/demo environment | Có |

- Không đặt giá trị thật trong source, command history, report, evidence hoặc log.
- Không dump environment. Nếu nghi lộ, dừng demo và rotate/revoke secret liên quan.
- Demo thật phải đăng ký redirect URI HTTPS chính xác: `https://<demo-host>/auth/google/callback`; `<demo-host>` chỉ là placeholder, không phải cấu hình đã xác minh.
- Local manual theo app README dùng `http://localhost:5000/auth/google/callback`; kết quả này không chứng minh cookie `Secure` tại HTTPS edge.

## 3. Setup/install

Từ repository root:

```bash
cd workspace-shared/projects/ToDoApp/app
python3 -m venv .venv
.venv/bin/python -m pip install -r requirements.txt pytest
.venv/bin/python -m compileall -q app.py tests
.venv/bin/python -m pip check
.venv/bin/python -m pytest -q
```

Kỳ vọng: compile thành công, `pip check` không có broken requirement, `9 passed`. Nếu checksum/build không khớp `IMPL-US-001@00d6ec7` hoặc test fail: dừng, không chạy demo.

## 4. Run local/demo

### 4.1 Local credential-free

Không cần credential thật. Chạy pytest và smoke bằng Flask test client theo evidence:

- `workspace-shared/projects/ToDoApp/devops/DOPS-US-001-dang-nhap-bang-google/evidence/pytest.log`
- `workspace-shared/projects/ToDoApp/devops/DOPS-US-001-dang-nhap-bang-google/evidence/local-smoke.log`
- `workspace-shared/projects/ToDoApp/devops/DOPS-US-001-dang-nhap-bang-google/evidence/https-cookie-smoke.log`

Không dùng kết quả mock để kết luận Google OAuth E2E đã pass.

### 4.2 Local OAuth thủ công

1. Bind ba biến cấu hình từ SecretRef vào process mà không in giá trị.
2. Tại `workspace-shared/projects/ToDoApp/app/`, chạy:

```bash
.venv/bin/flask --app app run
```

3. Mở `http://localhost:5000/`; xác nhận có `Todo List`, `Continue with Google`, không có email/password.
4. Chỉ tiếp tục OAuth nếu Google OAuth owner đã cho phép redirect URI local và test account.

### 4.3 Demo Google thật

1. Xác nhận demo target không phải production và có HTTPS.
2. Khóa build `IMPL-US-001@00d6ec7`; đối chiếu Git tree `76aacb98d9d120e5e054299f8810edd456b4278f` và manifest checksum trong report chính.
3. Bind SecretRef; không lưu credential vào file `.env` trong repository.
4. Cấu hình Google redirect URI chính xác tới `https://<demo-host>/auth/google/callback`.
5. Start Flask process sau HTTPS edge bằng cơ chế của demo provider. Không có manifest/IaC/provider command trong input nên không được tự suy diễn lệnh deploy.
6. Chạy health/smoke bên dưới và lưu evidence redacted.

## 5. Health/smoke

| ID | Kiểm tra | Kỳ vọng | Trạng thái hiện tại |
|---|---|---|---|
| `TC-US001-001` | `GET /` | HTTP 200, tên app và Google CTA | Local pass |
| `TC-US001-002` | Login methods | Không có email/password | Local pass |
| `TC-US001-004` | Callback thành công | Redirect `/todos`, user được reuse | Mock pass; thật `not_run` |
| `TC-US001-005` | Session/cookie | `HttpOnly`, `SameSite=Lax`, TTL 24 giờ; `Secure` qua HTTPS thật | Mock pass; edge thật `not_run` |
| `TC-US001-007` | `POST /auth/logout` | Xóa app session, về `/` | Local pass |
| `TC-US001-008` | Google OAuth E2E | Consent/callback thật thành công | `blocked` |

Health tối thiểu trước demo: `GET /` trả 200. Sau login thật: `/todos` trả 200. Không lưu header `Cookie`, `Set-Cookie` value, token hoặc authorization code; chỉ ghi metadata cờ cookie.

## 6. Logs

- Theo dõi process start/stop, HTTP status, login success/failure và callback error ở mức sự kiện.
- Không log credential, OAuth token/code, cookie/session value, `google_sub` thật hoặc toàn bộ environment.
- Khi thu evidence, redact hostname/account nếu nhạy cảm và dùng đường dẫn `workspace-shared/projects/ToDoApp/devops/DOPS-US-001-dang-nhap-bang-google/evidence/`.
- Không có dashboard, tracing hay alert được cấu hình; DevOps theo dõi thủ công trong cửa sổ demo.

## 7. Rollback

### Trigger

Startup/config fail; OAuth consent/callback fail; cookie thiếu `Secure` tại HTTPS edge; health/smoke fail; sai build/checksum; nghi lộ secret.

### Thực hiện

1. Dừng/quarantine demo process; không chuyển traffic sang build lỗi.
2. Gỡ SecretRef binding khỏi process.
3. Rotate/revoke secret nếu nghi lộ; OAuth credential owner thực hiện.
4. Khôi phục build demo trước đó nếu có; nếu không có, giữ service dừng và thông báo `BLOCKED`.
5. Không tự xóa SQLite. Chỉ sau xác nhận owner, backup rồi xóa database runtime của demo mới nếu cần.
6. Chạy lại `GET /`, build checksum và smoke của phiên bản khôi phục.

RTO ước tính: 10–30 phút cho process demo, chưa đo thực tế.

## 8. Troubleshooting

| Triệu chứng | Kiểm tra an toàn | Hành động |
|---|---|---|
| App không start | Thiếu tên biến bắt buộc; dependency | Bind lại SecretRef, chạy `pip check`; không in giá trị |
| Google báo redirect mismatch | URI đăng ký và callback app | Sửa URI cho khớp tuyệt đối `/auth/google/callback`; dừng demo đến khi đúng |
| Callback quay về login | Event/error code redacted, clock, OAuth state | Thử lại bằng test account; không log token/code; nếu lặp lại, giữ `BLOCKED` |
| Cookie không có `Secure` | HTTPS termination và forwarded scheme | Kiểm tra trusted proxy/forwarded scheme; không hạ yêu cầu `Secure` |
| `/todos` quay về `/` | Cookie hết hạn/sai chữ ký hoặc `SESSION_SECRET` thay đổi | Bind đúng SecretRef; không tái sử dụng cookie cũ; không dump cookie |
| SQLite error | Quyền ghi thư mục runtime, schema demo | Dừng; backup nếu có dữ liệu; không sửa business logic/schema ngoài `DBCHG-001` |

## 9. Residual conditions trước demo Google thật

Tất cả điều kiện sau phải hoàn tất:

- Demo target HTTPS cụ thể được cung cấp và xác nhận không phải production.
- Ba SecretRef được bind ngoài source; test account được phép và least-privilege review hoàn tất.
- Redirect URI Google khớp tuyệt đối callback HTTPS.
- `TC-US001-008` pass với evidence redacted.
- Cookie có `Secure`, `HttpOnly`, `SameSite=Lax`, TTL 24 giờ tại HTTPS edge thật.
- Không có secret/token/cookie value trong source, log hoặc evidence.
- Product Owner/release owner và DevOps phê duyệt residual risks.
- Vulnerability scan, license review và artifact signature vẫn là điều kiện theo release policy; hiện `not_run`.

Nếu thiếu bất kỳ điều kiện bắt buộc nào: quyết định là `blocked`, báo cáo tổng thể `AWAITING_APPROVAL`, không quảng bá demo là đã release.

## 10. Traceability

- Report chính: `workspace-shared/projects/ToDoApp/devops/DOPS-US-001-dang-nhap-bang-google/devops.md`
- Release note: `workspace-shared/projects/ToDoApp/devops/DOPS-US-001-dang-nhap-bang-google/release-note.md`
- QA: `workspace-shared/projects/ToDoApp/qa/QA-US-001-dang-nhap-bang-google/qa.md`
- Architecture: `workspace-shared/projects/ToDoApp/architecture/ARCH-US-001-dang-nhap-bang-google.md`
- Implementation: `workspace-shared/projects/ToDoApp/implementation/IMPL-US-001-dang-nhap-bang-google.md`
