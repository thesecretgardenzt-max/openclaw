# Task Breakdown – BasicToDoApp Android MVP

## artifact_type
technical_architecture

## schema_version
1.0.0

## source_ba_artifact
- Product Brief: `product-brief/product-brief.md`
- BA Analysis: `product-brief/ba-analysis.md`
- Technical Plan: `architecture/technical-plan.md`

## developer_task_breakdown

### T01 – Project setup and baseline configuration
Requirement trace: NFR-006, demo APK success criteria.

Scope:
- Create/configure Android app project for Kotlin.
- Set minimum SDK to Android 8.0 / API 26.
- Add required dependencies: Jetpack Compose, Material 3, Lifecycle ViewModel, Kotlin Coroutines, Room, test libraries.
- Ensure app can build and launch a placeholder main screen.

Acceptance / verification:
- Gradle sync succeeds.
- Debug build succeeds.
- App launches on API 26+ emulator/device.
- No network/account dependency is introduced.

### T02 – Define task domain model and validation
Requirement trace: FR-001, FR-002, BA business rules, AC-002, AC-007.

Scope:
- Define `Task` model with `id`, `title`, `createdAt`, `updatedAt`.
- Define `SortOrder` with only `NEWEST_FIRST` and `OLDEST_FIRST`.
- Implement title validation: reject empty or whitespace-only input.
- Proposed: trim title before saving.

Acceptance / verification:
- Unit tests cover empty string, whitespace-only, and valid titles.
- No additional business fields such as priority, deadline, completion status, labels, or reminders are added.

### T03 – Implement Room persistence layer
Requirement trace: FR-001, FR-002, FR-003, FR-004, FR-005, AC-003, AC-005, AC-011, AC-015, AC-016.

Scope:
- Create `TaskEntity` Room table with `id`, `title`, `createdAt`, `updatedAt`.
- Create DAO methods:
  - observe newest first by `createdAt DESC`;
  - observe oldest first by `createdAt ASC`;
  - insert task;
  - update title and `updatedAt`;
  - delete by id.
- Create `AppDatabase`.
- Add mapping between entity and domain model.

Acceptance / verification:
- Room/database tests verify insert, update, delete, and both sort orders.
- Updated task keeps original `createdAt` and changes `updatedAt`.
- Deleted task is not returned by queries.

### T04 – Implement repository layer
Requirement trace: FR-001..FR-005.

Scope:
- Implement `TaskRepository` interface and Room-backed implementation.
- Expose `observeTasks(sortOrder)`.
- Implement `addTask`, `updateTask`, and `deleteTask` as suspend functions.
- Keep repository free of UI state.

Acceptance / verification:
- Repository tests or DAO-backed integration tests verify actions delegate correctly.
- Repository supports only creation-time sorting.

### T05 – Implement ViewModel and UI state
Requirement trace: FR-001..FR-006, AC-001..AC-017.

Scope:
- Create `TaskViewModel`.
- Create `TaskUiState` containing tasks, current input, selected sort order, validation message state, selected edit/delete task state as needed.
- Implement actions:
  - add task;
  - open/edit/save task;
  - request delete/cancel delete/confirm delete;
  - change sort order.
- Generate timestamps for create/update.
- Keep sort preference in memory only.

Acceptance / verification:
- Unit tests confirm invalid add/edit does not call repository.
- Unit tests confirm valid add/edit/delete calls repository with expected values.
- Unit tests confirm sort changes update observed order and are not persisted separately.

### T06 – Build main Compose UI
Requirement trace: FR-006, AC-017, NFR-002.

Scope:
- Build single main screen with:
  - title `BasicToDoApp`;
  - task input;
  - add button/control;
  - sort selector: newest first / oldest first;
  - task list;
  - task row title and creation time;
  - edit and delete controls per task;
  - simple empty state.
- Connect UI controls to ViewModel.
- Add UI test tags for QA automation.

Acceptance / verification:
- UI visibly includes all controls listed in AC-017.
- App remains a one-screen MVP; no out-of-scope flows are introduced.
- Compose preview or emulator smoke test confirms the screen renders.

### T07 – Implement add task flow
Requirement trace: FR-001, AC-001, AC-002, AC-003.

Scope:
- User enters title and taps add.
- Valid title creates task, clears input, displays task in list.
- Invalid title shows invalid-data message and does not create task.

Acceptance / verification:
- UI test: valid title appears in list.
- UI test: empty/whitespace title shows error and task count does not increase.
- Data test/manual check: saved task has id and creation timestamp.

### T08 – Implement edit task flow
Requirement trace: FR-002, AC-004, AC-005, AC-006, AC-007.

Scope:
- User opens edit control on a task row.
- Show edit dialog/sheet with current title.
- Save valid update to local DB and update list display.
- Reject empty/whitespace-only edit with error message.
- Update `updatedAt` on successful save.

Acceptance / verification:
- UI test: edited title appears in list.
- UI test: invalid edit title shows error and original title remains.
- Persistence/manual test: after app restart, edited title remains.
- Data test: `updatedAt` changes on edit.

### T09 – Implement delete task flow with confirmation
Requirement trace: FR-003, AC-008, AC-009, AC-010, AC-011.

Scope:
- User taps delete on a task row.
- Show confirmation dialog before deleting.
- Confirm removes selected task from Room and UI list.
- Cancel/dismiss leaves task unchanged.

Acceptance / verification:
- UI test: delete request shows confirmation.
- UI test: cancel keeps task.
- UI test: confirm removes only selected task.
- Persistence/manual test: deleted task does not reappear after app restart.

### T10 – Implement sort behavior
Requirement trace: FR-004, AC-012, AC-013, AC-014.

Scope:
- Add two sort choices only: newest first and oldest first.
- Query/order by `createdAt` only.
- Update displayed list immediately when user changes sort.
- Do not persist selected sort preference across app close/reopen.

Acceptance / verification:
- UI/data test: newest first displays descending creation time.
- UI/data test: oldest first displays ascending creation time.
- Manual test: close/reopen app; saved tasks remain, but previous sort selection is not required to be remembered.
- No title/update-time/priority sort options exist.

### T11 – Persistence and offline verification
Requirement trace: FR-005, NFR-003, NFR-004, NFR-005, AC-015, AC-016.

Scope:
- Verify add/edit/delete state persists via Room after process/app restart.
- Verify core actions work with network disabled.
- Confirm app does not require account, Internet, or third-party service.

Acceptance / verification:
- Manual QA checklist passes for add/edit/delete persistence after reopen.
- Offline emulator/device test passes for all core flows.
- Manifest/code review confirms no unnecessary network flow for MVP.

### T12 – Error handling, copy, and UX polish
Requirement trace: FR-001, FR-002, FR-003, NFR-001, NFR-002.

Scope:
- Add clear validation messages for invalid add/edit.
- Add generic persistence-error fallback if implementation exposes errors.
- Ensure delete confirmation copy is clear.
- Ensure long titles do not break layout; wrap or ellipsize safely.
- Keep UI responsive by using coroutines/Room off main thread.

Acceptance / verification:
- Invalid add/edit feedback is visible and understandable.
- Delete confirmation prevents accidental deletion.
- Long title smoke test does not crash or make screen unusable.
- Normal MVP actions complete without visible UI freeze.

### T13 – Automated test suite and QA handoff
Requirement trace: AC-001..AC-017, NFR-001..NFR-006.

Scope:
- Add/complete unit tests for validation and ViewModel.
- Add Room tests for persistence and sort order.
- Add Compose UI tests for core flows where feasible.
- Document manual QA steps and emulator/device assumptions.

Acceptance / verification:
- Unit tests pass.
- Room/instrumented tests pass where configured.
- UI tests pass or any environment blocker is documented.
- QA has a clear checklist mapping to AC-001..AC-017.

### T14 – Build demo APK
Requirement trace: Product success criteria, DevOps handoff.

Scope:
- Produce debug or demo APK according to project conventions.
- Confirm APK installs on API 26+ compatible emulator/device.
- Provide build path and notes for DevOps/QA.

Acceptance / verification:
- APK build succeeds.
- APK install succeeds on compatible Android device/emulator.
- Smoke test: launch app, add task, close/reopen, task remains.

## suggested_implementation_order
1. T01 project setup.
2. T02 domain/validation.
3. T03 persistence.
4. T04 repository.
5. T05 ViewModel.
6. T06 UI shell.
7. T07 add flow.
8. T08 edit flow.
9. T09 delete flow.
10. T10 sort flow.
11. T11 persistence/offline verification.
12. T12 polish/error handling.
13. T13 tests/QA handoff.
14. T14 APK build.

## unresolved_decisions_for_developer
- Treat duplicate titles as allowed unless BA/Product says otherwise.
- Trim titles before saving unless BA/Product rejects this proposed behavior.
- Do not enforce title maximum length unless a new requirement is added.
- Store `updatedAt` but do not display it unless UI/Design explicitly requests display.
