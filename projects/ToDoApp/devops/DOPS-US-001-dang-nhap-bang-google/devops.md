# DEVOPS-US-001 — Đăng nhập bằng Google

## Status

`AWAITING_APPROVAL`

Lý do: build đã qua validation local và QA đưa ra `go_with_conditions`, nhưng chưa có demo environment dùng HTTPS/Google OAuth thật, chưa chạy OAuth E2E thật và chưa xác minh cờ cookie `Secure` tại HTTPS edge/reverse proxy thật. Không deploy production.

## `schema_version`

`1.0.0`

## `artifact_type`

`devops_release_report`

## `artifact_id`

`DEVOPS-US-001`

## `project_id`

`TODO-LIST-DEMO`

## `source_architecture_artifact`

| Field | Value |
|---|---|
| `artifact_id` | `ARCH-US-001` |
| `artifact_type` | `technical_architecture` |
| `schema_version` | `1.0.0` |

Nguồn: `architecture/ARCH-US-001-dang-nhap-bang-google.md`.

## `source_developer_artifact`

| Field | Value |
|---|---|
| `artifact_id` | `IMPL-US-001` |
| `artifact_type` | `implementation_report` |
| `schema_version` | `1.0.0` |

Nguồn: `implementation/IMPL-US-001-dang-nhap-bang-google.md`.

## `source_qa_artifact`

| Field | Value |
|---|---|
| `artifact_id` | `QA-US-001` |
| `artifact_type` | `qa_report` |
| `schema_version` | `1.0.0` |

Nguồn: `qa/QA-US-001-dang-nhap-bang-google.md`; QA recommendation là `go_with_conditions`, build được phê duyệt có điều kiện.

## Traceability đầu vào

| Loại | Artifact | Phạm vi được trace |
|---|---|---|
| Requirement | `requirements/US-001-dang-nhap-bang-google.md` | `US-001`, `AC-001`–`AC-003` |
| Architecture | `architecture/ARCH-US-001-dang-nhap-bang-google.md` | `CMP-001`, `INT-001`–`INT-004`, `MDL-001`, `MDL-002`, `RSK-001`, `RSK-002` |
| Implementation | `implementation/IMPL-US-001-dang-nhap-bang-google.md` | `IMPL-US-001`, `TASK-001`–`TASK-003`, `DBCHG-001` |
| QA | `qa/QA-US-001-dang-nhap-bang-google.md` | `TC-US001-001`–`TC-US001-008`, `RISK-QA-US001-001`–`003` |

Artifact vận hành đi kèm:

- Runbook: `devops/DOPS-US-001-dang-nhap-bang-google/runbook.md`.
- Release Note: `devops/DOPS-US-001-dang-nhap-bang-google/release-note.md`.

Hai artifact này chỉ diễn giải cách vận hành và quyết định từ các nguồn trên; không mở rộng feature hoặc business logic.

## `release`

| Field | Value |
|---|---|
| `release_id` | `REL-US-001-00d6ec7` |
| `version` | `0.1.0-demo` |
| `build_identifier` | `IMPL-US-001@00d6ec7` |
| `change_summary` | Flask/Authlib demo cho đăng nhập Google, signed cookie 24 giờ, SQLite user mapping và local logout theo US-001. |
| `release_type` | `minor` |

## `deployment_target`

| Field | Value |
|---|---|
| `environment` | `test` |
| `provider` | `local/demo Flask process; target HTTPS provider chưa được cung cấp` |
| `region` | `local; demo region chưa xác định` |
| `strategy` | `manual` |
| `maintenance_window` | `not_scheduled — chờ demo environment và phê duyệt` |

Production không nằm trong phạm vi và không được triển khai.

## `release_artifacts`

| `name` | `type` | `uri_or_reference` | `version` | `checksum` | `signed` | Trace |
|---|---|---|---|---|---:|---|
| App source tree | `other` | `app/` | `IMPL-US-001@00d6ec7` | Git tree `76aacb98d9d120e5e054299f8810edd456b4278f` | `false` | `IMPL-US-001/change_set`; `QA-US-001/handoff_to_devops` |
| Runtime dependency manifest | `manifest` | `app/requirements.txt` | `0.1.0-demo` | SHA-256 `a562a75f486bb4b6044b7b1b1e8173ae0fc7f8476ba84776eb62f67cbcf343c8` | `false` | `IMPL-US-001/dependencies`; QA build gate |
| Local/demo runbook | `other` | `devops/DOPS-US-001-dang-nhap-bang-google/runbook.md` | `REL-US-001-00d6ec7` | Ghi tại `devops/DOPS-US-001-dang-nhap-bang-google/evidence/SHA256SUMS.txt` | `false` | `QA-US-001/handoff_to_devops`; report này |
| Release note | `other` | `devops/DOPS-US-001-dang-nhap-bang-google/release-note.md` | `REL-US-001-00d6ec7` | Ghi tại `devops/DOPS-US-001-dang-nhap-bang-google/evidence/SHA256SUMS.txt` | `false` | `US-001`; `QA-US-001/release_recommendation`; report này |

Không tạo binary/container; release hiện tại là source-based demo. Artifact chưa có chữ ký phát hành.

## `configuration`

| `key` | `source_reference` | `secret` | `required` | `validated` | Ghi chú/trace |
|---|---|---:|---:|---:|---|
| `GOOGLE_CLIENT_ID` | SecretRef của demo environment, giá trị `[REDACTED]` | `true` | `true` | `false` | Tên biến và fail-fast đã kiểm tra; giá trị thật/redirect URI chưa kiểm tra. `ARCH-US-001/RSK-001`; `IMPL-US-001/configuration_changes`. |
| `GOOGLE_CLIENT_SECRET` | SecretRef của demo environment, giá trị `[REDACTED]` | `true` | `true` | `false` | Không dùng credential thật trong validation. `ARCH-US-001/RSK-001`. |
| `SESSION_SECRET` | SecretRef của demo environment, giá trị `[REDACTED]` | `true` | `true` | `false` | App fail-fast khi thiếu; độ mạnh/rotation của secret thật chưa kiểm tra. `ARCH-US-001/MDL-002`. |

Evidence: `devops/DOPS-US-001-dang-nhap-bang-google/evidence/config-validation.log`. Không lưu giá trị secret vào report/evidence/source.

## `pipeline`

| `order` | `name` | `purpose` | `commands_or_actions` | `entry_conditions` | `exit_conditions` | `status` | Trace/evidence |
|---:|---|---|---|---|---|---|---|
| 1 | Source intake | Xác nhận đúng build và đọc artifact theo thứ tự | Đọc QA report/evidence → requirement → architecture → implementation; xác nhận commit `00d6ec7` | Có đủ artifacts US-001 | Build và traceability khớp | `passed` | Các source artifact; `devops/DOPS-US-001-dang-nhap-bang-google/evidence/app-integrity.log` |
| 2 | Build/dependency validation | Kiểm tra syntax và dependency consistency | `.venv/bin/python -m compileall -q app.py tests`; `.venv/bin/python -m pip check` | Venv của build QA tồn tại | Compile pass, không broken requirements | `passed` | `devops/DOPS-US-001-dang-nhap-bang-google/evidence/build-dependency-check.log` |
| 3 | Automated tests | Chạy lại suite đã QA xác nhận | `.venv/bin/python -m pytest -q` | Build đúng commit | 9 test pass | `passed` | `devops/DOPS-US-001-dang-nhap-bang-google/evidence/pytest.log`; `QA-US-001` |
| 4 | Credential-free smoke | Xác minh Login Page, guard, routes và cookie trên HTTPS mô phỏng | Flask test client với dữ liệu tổng hợp; không gọi mạng | Không dùng credential thật | 6 local smoke và 5 HTTPS-mock checks pass | `passed` | `devops/DOPS-US-001-dang-nhap-bang-google/evidence/local-smoke.log`; `devops/DOPS-US-001-dang-nhap-bang-google/evidence/https-cookie-smoke.log` |
| 5 | Security hygiene | Kiểm tra rò credential và ghi nhận gate chưa chạy | Scan source runtime; kiểm tra `.env`; review evidence | Source app không đổi | Không có pattern credential hoặc `.env` được phát hiện | `passed` | `devops/DOPS-US-001-dang-nhap-bang-google/evidence/security-hygiene.log` |
| 6 | Google OAuth E2E | Xác minh consent, redirect/callback và session với Google thật | Chạy `TC-US001-008` trên demo HTTPS | Có SecretRef, OAuth client, test account, redirect URI và HTTPS edge | OAuth thật thành công, không lộ dữ liệu nhạy cảm | `blocked` | `QA-US-001/TC-US001-008`; chưa có environment/credential |
| 7 | Demo deployment | Deploy build đã khóa và chạy post-deploy smoke | Làm theo runbook bên dưới | Stage 6 pass và có phê duyệt | Health/smoke pass; evidence được lưu | `blocked` | Chưa deploy; production bị loại khỏi phạm vi |

## `infrastructure_changes`

| `id` | `resource` | `operation` | `description` | `iac_path` | `risk` | `rollback_action` | Trace |
|---|---|---|---|---|---|---|---|
| `INF-US001-001` | Demo infrastructure | `none` | Không có IaC hoặc target demo được cung cấp; không thay đổi hạ tầng. | `not_applicable` | `low` | Không có thay đổi để rollback. | `ARCH-US-001/deployment_topology`; `IMPL-US-001/known_limitations` |

## `migration_plan`

| Field | Value |
|---|---|
| `required` | `false` |
| `steps` | Không chạy migration ngoài app; scaffold tự tạo bảng SQLite `users` khi khởi động. |
| `backup_before_migration` | `false` |
| `estimated_duration` | `not_applicable` |
| `backward_compatible` | `true` |
| `validation` | Với demo mới: xác nhận file database được tạo, schema có `google_sub UNIQUE NOT NULL`; không chạy trên dữ liệu production. |

Trace: `IMPL-US-001/DBCHG-001`, `ARCH-US-001/MDL-001`. Nếu demo environment đã có dữ liệu, phải dừng và lập kế hoạch backup riêng trước khi chạy.

## `deployment_runbook`

| `order` | `action` | `owner` | `expected_result` | `failure_action` | Trace |
|---:|---|---|---|---|---|
| 1 | Khóa release tại commit `00d6ec7`; đối chiếu app Git tree và dependency checksum trong report. | DevOps | Đúng build QA phê duyệt có điều kiện. | Dừng; không deploy build lệch checksum. | `QA-US-001/handoff_to_devops`; `IMPL-US-001` |
| 2 | Chuẩn bị test/demo environment dùng HTTPS; không chọn production. | DevOps | Có URL HTTPS và callback `/auth/google/callback`. | Dừng; không fallback sang HTTP cho demo thật. | `ARCH-US-001/deployment_topology`; `RISK-QA-US001-002` |
| 3 | Gắn `GOOGLE_CLIENT_ID`, `GOOGLE_CLIENT_SECRET`, `SESSION_SECRET` từ SecretRef; không in/ghi giá trị. | DevOps | App đọc đủ ba cấu hình và secret không nằm trong source/log. | Xóa binding sai; rotate nếu nghi lộ; dừng release. | `IMPL-US-001/configuration_changes`; `RISK-QA-US001-001` |
| 4 | Cấu hình Google redirect URI chính xác tới URL HTTPS kết thúc bằng `/auth/google/callback`. | DevOps/Google OAuth owner | Callback URI khớp tuyệt đối. | Dừng; sửa cấu hình Google trước khi tiếp tục. | `ARCH-US-001/INT-003`; `TC-US001-008` |
| 5 | Tạo môi trường Python và cài `app/requirements.txt`; chạy compile, `pip check`, pytest. | DevOps | Các gate tái lập kết quả evidence. | Dừng; thu log redacted và rollback release candidate. | `IMPL-US-001/verification`; pipeline 2–3 |
| 6 | Khởi động app sau HTTPS edge; chỉ ghi log sự kiện, không ghi token/cookie. | DevOps | App healthy và route `/` phản hồi. | Dừng app; kiểm tra config/proxy mà không dump environment. | `ARCH-US-001/cross_cutting_concerns` |
| 7 | Chạy `TC-US001-001`, `002`, `004`, `005`, `007`, `008`; riêng OAuth E2E dùng test account được phép. | QA/DevOps | Google login thật mở `/todos`; fail/cancel an toàn; logout hoạt động. | Dừng release; xóa session test; thu evidence redacted. | `QA-US-001/handoff_to_devops` |
| 8 | Qua HTTPS edge thật, xác nhận `Set-Cookie` có `Secure`, `HttpOnly`, `SameSite=Lax`, TTL 24 giờ nhưng không lưu cookie value. | DevOps | Metadata cookie đúng tại edge thật. | Dừng; kiểm tra trusted proxy/forwarded scheme; không bỏ cờ `Secure`. | `RISK-QA-US001-002`; `ARCH-US-001/RSK-002` |
| 9 | Ghi evidence/checksum, xin phê duyệt release có điều kiện hoặc đóng blocker. | DevOps/PO | Release decision có evidence và approval. | Giữ `AWAITING_APPROVAL`; không quảng bá release. | `QA-US-001/release_recommendation` |

## Runbook local/demo

Runbook thực thi đầy đủ: `devops/DOPS-US-001-dang-nhap-bang-google/runbook.md`. Release Note: `devops/DOPS-US-001-dang-nhap-bang-google/release-note.md`.

### Environment

- `local credential-free`: compile, `pip check`, pytest và Flask test client; đã validate, không gọi Google.
- `local OAuth manual`: cần ba SecretRef và redirect URI local được Google OAuth owner cho phép; `not_run`.
- `demo Google thật`: bắt buộc HTTPS edge, test account được phép, callback chính xác; `blocked`.
- `production`: ngoài phạm vi, `not_run`.

### Configuration redacted

Chỉ bind `GOOGLE_CLIENT_ID=[REDACTED]`, `GOOGLE_CLIENT_SECRET=[REDACTED]`, `SESSION_SECRET=[REDACTED]` từ SecretRef ngoài source. Không ghi giá trị vào `.env` trong repository, command history, log hoặc evidence; không dump environment.

### Setup/install

Từ repository root:

```bash
cd workspace-shared/projects/ToDoApp/app
python3 -m venv .venv
.venv/bin/python -m pip install -r requirements.txt pytest
.venv/bin/python -m compileall -q app.py tests
.venv/bin/python -m pip check
.venv/bin/python -m pytest -q
```

Kỳ vọng: compile pass, dependency nhất quán, `9 passed`. Build/checksum lệch hoặc test fail thì dừng.

### Run local/demo

- Local app: tại `workspace-shared/projects/ToDoApp/app/`, bind SecretRef rồi chạy `.venv/bin/flask --app app run`; callback local theo `app/README.md` là `http://localhost:5000/auth/google/callback`.
- Demo thật: khóa build `IMPL-US-001@00d6ec7`, chuẩn bị HTTPS target không phải production, bind SecretRef, cấu hình `https://<demo-host>/auth/google/callback`, start app sau HTTPS edge bằng cơ chế provider đã được owner cung cấp.
- Không có provider, Dockerfile, deployment manifest, IaC hoặc CI/CD command trong input; không tự suy diễn lệnh deploy.

### Health/smoke

Chạy `TC-US001-001`, `002`, `004`, `005`, `007`, `008`. Kiểm tra `GET /` trả 200; sau OAuth thật `/todos` trả 200; fail/cancel không tạo session; logout xóa session. Tại HTTPS edge, chỉ ghi metadata xác nhận `Secure`, `HttpOnly`, `SameSite=Lax`, TTL 24 giờ; không lưu cookie value.

### Logs

Theo dõi process health, HTTP status và login/callback success/failure ở mức sự kiện. Cấm log credential, token/code, cookie/session value, `google_sub` thật hoặc toàn bộ environment. Chưa có dashboard/tracing/alert; theo dõi thủ công trong cửa sổ demo.

### Rollback

Khi startup/config/OAuth/cookie/smoke/checksum fail hoặc nghi lộ secret: dừng/quarantine process, gỡ SecretRef binding, rotate/revoke nếu cần, khôi phục build demo trước đó nếu có, không tự xóa SQLite, rồi chạy lại health/smoke. Nếu không có build trước đó, giữ service dừng và trạng thái `BLOCKED`.

### Troubleshooting

- Redirect mismatch: sửa URI cho khớp tuyệt đối `/auth/google/callback`; không tiếp tục khi chưa đúng.
- Cookie thiếu `Secure`: kiểm tra HTTPS termination và trusted forwarded scheme; không hạ yêu cầu bảo mật.
- App không start: kiểm tra sự hiện diện tên biến và `pip check` mà không in giá trị.
- Callback fail hoặc `/todos` quay về `/`: xem event/error redacted, clock, OAuth state và secret binding; không dump token/cookie.
- SQLite error: dừng, backup nếu có dữ liệu; không sửa schema/business logic ngoài `DBCHG-001`.

### Residual conditions trước demo Google thật

Phải có demo HTTPS target, ba SecretRef, test account được phép, redirect URI đúng, `TC-US001-008` pass, cookie flags được xác minh tại edge thật, evidence sạch secret, và approval PO/DevOps. Vulnerability scan, license review và artifact signature vẫn `not_run` và phải thực hiện nếu release policy yêu cầu. Thiếu bất kỳ điều kiện bắt buộc nào thì giữ `blocked`/`AWAITING_APPROVAL`.

## Release Note

Release Note riêng: `devops/DOPS-US-001-dang-nhap-bang-google/release-note.md`.

Artifact này ghi rõ `release_id=REL-US-001-00d6ec7`, `version=0.1.0-demo`, scope chỉ theo input US-001, verification summary, known limitations/residual risks và quyết định `blocked`. Release Note không thay thế các field schema trong report chính và không mở rộng feature/business logic.

## `observability`

| Field | Value |
|---|---|
| `health_checks` | `GET /` trả HTTP 200; `/todos` khi chưa xác thực redirect về `/`; sau OAuth thật `/todos` trả 200. |
| `metrics` | Đếm login success/failure và callback error không chứa token/cookie; chưa được instrument trong scaffold. |
| `logs` | Log sự kiện start/callback failure ở mức ứng dụng; cấm log credential, token, cookie/session value. |
| `traces` | Chưa cấu hình tracing; `not_run` cho demo scaffold. |
| `dashboards` | Chưa có dashboard; theo dõi log/health thủ công trong cửa sổ demo. |
| `alerts` | Chưa có alert; owner phải theo dõi callback error và process health trong demo. |
| `slo_validation` | Chưa có SLO định lượng; xác nhận functional smoke theo QA handoff. |

Trace: `ARCH-US-001/cross_cutting_concerns`; `QA-US-001/handoff_to_devops`.

## `rollback_plan`

| Field | Value |
|---|---|
| `triggers` | Startup/config failure; OAuth callback/consent thất bại; cookie thiếu `Secure` tại HTTPS edge; smoke test fail; phát hiện rò secret; sai build/checksum. |
| `steps` | 1) Dừng/quarantine demo process; 2) gỡ binding SecretRef khỏi process; 3) rotate credential nếu nghi lộ; 4) khôi phục commit/build demo trước đó nếu có; 5) với demo mới không cần giữ dữ liệu, xóa database runtime chỉ sau khi owner xác nhận; 6) chạy lại health/smoke của phiên bản khôi phục. |
| `owner` | `DevOps`; OAuth credential owner chịu trách nhiệm rotate/revoke. |
| `estimated_duration` | `10–30 phút cho demo process; chưa đo thực tế` |
| `data_recovery` | Không có production data. SQLite demo có thể được sao lưu trước khi xóa; không tự xóa nếu chưa có xác nhận owner. |
| `validation` | `GET /` healthy; build/checksum đúng phiên bản rollback; không còn process lỗi; credential nghi lộ đã rotate; log/evidence không chứa secret. |

Trace: `IMPL-US-001/DBCHG-001`; `ARCH-US-001/ADR-004`; QA residual risks.

## `security_and_compliance`

| Field | Value |
|---|---|
| `vulnerability_scan` | `not_run` — scanner không có trong build QA; không tự thêm dependency hoặc sửa app. |
| `artifact_signature` | `not_run` — source tree/manifest chưa được ký; checksum đã ghi nhận. |
| `least_privilege_review` | `pending` — demo SecretRef/test account chưa được cung cấp; yêu cầu chỉ quyền tối thiểu và test account được phép. |
| `secret_scan` | `passed` trên source runtime/evidence theo `devops/DOPS-US-001-dang-nhap-bang-google/evidence/security-hygiene.log`; không phát hiện `.env` hoặc pattern credential thật. |
| `compliance_checks` | Không dùng credential thật trong validation; không gọi Google Identity; app không bị chỉnh sửa; evidence không in token/cookie; production không được deploy. |
| `exceptions` | OAuth E2E thật chưa chạy; HTTPS edge cookie chưa xác minh; vulnerability scan, license review và artifact signature chưa chạy. |

## `approvals`

| `role` | `approver` | `status` | `evidence` |
|---|---|---|---|
| QA | `QA-US-001` | `approved` | `go_with_conditions`; `qa/QA-US-001-dang-nhap-bang-google.md` |
| Product Owner / release owner | `pending` | `pending` | Cần chấp nhận residual risks và cho phép demo release sau khi các điều kiện bắt buộc pass. |
| DevOps | `DEVOPS-US-001` | `pending` | Local gates pass; chờ OAuth E2E thật, HTTPS edge cookie check và demo target. |
| Production deployment | `not_requested` | `not_required` | User yêu cầu không deploy production. |

## `deployment_execution`

| Field | Value |
|---|---|
| `status` | `not_started` |
| `started_at` | `not_run` |
| `completed_at` | `not_run` |
| `executor` | `DevOps agent` |
| `deployment_reference` | `not_created — không deploy production hoặc demo target thật` |
| `notes` | Chỉ validation/smoke credential-free được thực hiện; không có external deployment; không gọi Google Identity; không dùng credential thật. |

## `post_deployment_checks`

| `name` | `expected` | `actual` | `result` | `evidence` | Trace |
|---|---|---|---|---|---|
| Automated test suite | 9 test pass | 9 test pass | `passed` | `devops/DOPS-US-001-dang-nhap-bang-google/evidence/pytest.log` | `IMPL-US-001/automated_tests`; QA suite |
| Build/dependency consistency | Compile pass; không broken requirements | Đúng kỳ vọng | `passed` | `devops/DOPS-US-001-dang-nhap-bang-google/evidence/build-dependency-check.log` | QA build gate |
| Local functional smoke | Login Page/guard/routes pass | 6/6 pass | `passed` | `devops/DOPS-US-001-dang-nhap-bang-google/evidence/local-smoke.log` | `AC-001`, `AC-003`, `INT-001`–`004` |
| HTTPS cookie mô phỏng | Callback mock có metadata cookie an toàn | 5/5 pass | `passed` | `devops/DOPS-US-001-dang-nhap-bang-google/evidence/https-cookie-smoke.log` | `MDL-002`, `RSK-002` |
| App integrity | Không sửa app/business logic | Không có diff/status trong `app/` | `passed` | `devops/DOPS-US-001-dang-nhap-bang-google/evidence/app-integrity.log` | User constraint; build `00d6ec7` |
| Google OAuth E2E thật | Consent/callback thật thành công | Chưa có credential/demo target | `not_run` | `QA-US-001/TC-US001-008` | `AC-002`, `RISK-QA-US001-001` |
| Cookie `Secure` tại HTTPS edge thật | `Secure` được giữ sau reverse proxy | Chưa có HTTPS edge thật | `not_run` | `QA-US-001/RISK-QA-US001-002` | `ARCH-US-001/RSK-002` |
| Post-deploy production health | Production healthy | Không deploy production | `not_run` | Yêu cầu phạm vi | Không áp dụng |

Các check trên là pre-release/local validation, không được hiểu là post-deploy verification trên môi trường thật.

## `release_decision`

| Field | Value |
|---|---|
| `status` | `blocked` |
| `rationale` | QA là `go_with_conditions` và local gates đều pass, nhưng chưa deploy; OAuth E2E thật và cookie `Secure` tại HTTPS edge/reverse proxy thật chưa được xác minh. Vì vậy chưa phát hành demo tích hợp Google và không deploy production. |
| `follow_up_actions` | Cung cấp demo HTTPS target và SecretRef; cấu hình redirect URI; chạy `TC-US001-008`; xác minh metadata cookie tại edge thật; hoàn tất security/license gate nếu release policy yêu cầu; thu evidence redacted; xin PO/DevOps approval. |
| `incident_reference` | `not_applicable — không có deployment/incident` |

### Kết luận phát hành

Build `IMPL-US-001@00d6ec7` **sẵn sàng có điều kiện cho bước chuẩn bị demo**, nhưng release/deployment vẫn `blocked` và báo cáo ở trạng thái `AWAITING_APPROVAL`. Không có production deployment. Các điều kiện bắt buộc không được hạ cấp: Google OAuth E2E thật phải chạy và cookie `Secure` phải được xác minh qua HTTPS edge thật.
