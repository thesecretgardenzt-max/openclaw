# RELEASE NOTE — US-001 Đăng nhập bằng Google

## Trạng thái

`AWAITING_APPROVAL`

## Release ID và version

| Field | Value |
|---|---|
| `release_id` | `REL-US-001-00d6ec7` |
| `version` | `0.1.0-demo` |
| `build_identifier` | `IMPL-US-001@00d6ec7` |
| `release_type` | `minor` |
| `target` | Local validation / demo test; không production |

## Scope/features

Chỉ gồm phạm vi đã được xác định bởi `US-001`, `ARCH-US-001` và `IMPL-US-001`:

- Login Page hiển thị `Todo List` và `Continue with Google`; không có email/password.
- Authlib routes: `GET /auth/google/start`, `GET /auth/google/callback`, `POST /auth/logout`.
- User mapping tối thiểu bằng `google_sub` unique/not null trong SQLite demo.
- Signed app cookie chứa `user_id`, `expires_at`, TTL 24 giờ; `HttpOnly`, `SameSite=Lax`, `Secure` khi request được nhận diện là HTTPS.
- Callback fail/cancel quay về Login Page và không tạo app session.
- Logout cục bộ xóa app session.

Không bổ sung feature, business logic, hạ tầng production, migration framework hoặc provider triển khai.

## Verification summary

| Gate | Kết quả | Evidence |
|---|---|---|
| QA | `go_with_conditions` | `workspace-shared/projects/ToDoApp/qa/QA-US-001-dang-nhap-bang-google/qa.md` |
| Automated tests | `9 passed` | `workspace-shared/projects/ToDoApp/devops/DOPS-US-001-dang-nhap-bang-google/evidence/pytest.log` |
| Build/dependency | `passed` | `workspace-shared/projects/ToDoApp/devops/DOPS-US-001-dang-nhap-bang-google/evidence/build-dependency-check.log` |
| Local functional smoke | `6/6 passed` | `workspace-shared/projects/ToDoApp/devops/DOPS-US-001-dang-nhap-bang-google/evidence/local-smoke.log` |
| HTTPS cookie mock | `5/5 passed` | `workspace-shared/projects/ToDoApp/devops/DOPS-US-001-dang-nhap-bang-google/evidence/https-cookie-smoke.log` |
| Config fail-fast | `passed` | `workspace-shared/projects/ToDoApp/devops/DOPS-US-001-dang-nhap-bang-google/evidence/config-validation.log` |
| Secret hygiene | `passed` cho source/evidence đã kiểm tra | `workspace-shared/projects/ToDoApp/devops/DOPS-US-001-dang-nhap-bang-google/evidence/security-hygiene.log` |
| App integrity | `passed`; DevOps không sửa app/business logic | `workspace-shared/projects/ToDoApp/devops/DOPS-US-001-dang-nhap-bang-google/evidence/app-integrity.log` |
| Google OAuth E2E thật | `not_run` | `QA-US-001/TC-US001-008` |
| Cookie tại HTTPS edge thật | `not_run` | `QA-US-001/RISK-QA-US001-002` |

## Known limitations / residual risks

- Chưa xác minh consent, redirect URI, callback, OAuth state và test account với Google Identity thật.
- Chưa xác minh cờ cookie `Secure` sau HTTPS edge/reverse proxy thật.
- Demo dùng Flask + SQLite scaffold; không có Dockerfile, deployment manifest, IaC, CI/CD, HA hoặc migration framework.
- Vulnerability scan, license review, artifact signature và statement/branch coverage chưa chạy.
- Monitoring chỉ ở mức health/log thủ công; chưa có metrics, tracing, dashboard, alert hoặc SLO định lượng.
- Source-based artifact chưa ký; chỉ có Git tree/checksum tham chiếu.

## Deployment/release decision

| Field | Value |
|---|---|
| `decision` | `blocked` |
| `report_status` | `AWAITING_APPROVAL` |
| `production_deployment` | `not_run` |
| `rationale` | Local và QA gates hiện có pass, nhưng demo Google thật, HTTPS edge cookie check, target/SecretRef và approvals chưa hoàn tất. |

Điều kiện để chuyển sang demo release có điều kiện:

1. Cung cấp demo HTTPS target không phải production và ba SecretRef ngoài source.
2. Cấu hình callback HTTPS kết thúc bằng `/auth/google/callback`.
3. Chạy pass `TC-US001-008` với evidence redacted.
4. Xác minh `Secure`, `HttpOnly`, `SameSite=Lax`, TTL 24 giờ tại edge thật.
5. Hoàn tất approval của Product Owner/release owner và DevOps; chạy thêm security/license gate nếu release policy yêu cầu.

Thao tác vận hành chi tiết: `workspace-shared/projects/ToDoApp/devops/DOPS-US-001-dang-nhap-bang-google/runbook.md`.
Báo cáo nguồn: `workspace-shared/projects/ToDoApp/devops/DOPS-US-001-dang-nhap-bang-google/devops.md`.
