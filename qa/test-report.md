# FINAL QA Test Report - BasicToDoApp

## 1. Test environment and artifacts used

- QA status: `FINAL_QA_ACCEPTED_FOR_DEMO_REVIEW`
- Test date/time: `2026-09-29 06:48:25 AEST`
- Scope: first debug build available for final QA of BasicToDoApp MVP.
- Project path: `/Users/lethimythao/.openclaw/workspace-shared/app/BasicToDoApp`
- APK path: `/Users/lethimythao/.openclaw/workspace-shared/app/BasicToDoApp/app/build/outputs/apk/debug/app-debug.apk`
- APK metadata: `applicationId=com.example.basictodoapp`, `versionCode=1`, `versionName=0.1.0-t01`, artifact timestamp `Sep 29 01:10:37 2026`, size `6545203` bytes.
- Commit: `BLOCKED/N/A`; project directory is not a git repository, so no commit hash can be retrieved.
- Android SDK env: `source scripts/android-env.sh`
- Emulator/device: `emulator-5554`, `model=Android_SDK_built_for_arm64`, `device=emulator64_arm64`.
- Runtime evidence directory: `/Users/lethimythao/.openclaw/workspace-shared/qa/evidence`

Key runtime evidence:

- `final-qa-initial.png/xml` - launch and empty state.
- `final-qa-add-empty-validation.png/xml` - add validation for empty title.
- `final-qa-two-tasks-newest.png/xml` - add two tasks and default newest-first order.
- `final-qa-sort-menu.png/xml` - sort dropdown options.
- `final-qa-sort-oldest.png/xml` - oldest-first sort applied.
- `final-qa-reopen-default-newest.png/xml` - reopen keeps task data and resets sort to newest first.
- `final-qa-edit-empty-validation.png/xml` - edit validation for empty title.
- `final-qa-edit-success.png/xml` - edited title displayed.
- `final-qa-delete-dialog.png/xml` - delete confirmation dialog.
- `final-qa-delete-cancel.xml` - cancel delete keeps task.
- `final-qa-delete-confirmed.png/xml` - confirm delete removes selected task.
- `final-qa-reopen-after-edit-delete.png/xml` - edited task persists and deleted task does not return.
- `final-qa-long-duplicate.png/xml` - duplicate and very long title accepted/displayed.

## 2. Build/unit test status with commands

| Check | Command | Result | Evidence |
|---|---|---:|---|
| Unit tests | `source scripts/android-env.sh && ./gradlew --no-daemon :app:testDebugUnitTest --stacktrace` | PASS | Gradle output: `BUILD SUCCESSFUL in 6s`, `25 actionable tasks: 25 up-to-date` |
| Debug build | `source scripts/android-env.sh && ./gradlew --no-daemon :app:assembleDebug --stacktrace` | PASS | Gradle output: `BUILD SUCCESSFUL in 5s`, `34 actionable tasks: 34 up-to-date` |
| Device availability | `source scripts/android-env.sh && adb devices -l` | PASS | `emulator-5554 device product:sdk_gphone64_arm64 model:Android_SDK_built_for_arm64` |
| Install/launch | `adb -s emulator-5554 install -r app/build/outputs/apk/debug/app-debug.apk`, `adb -s emulator-5554 shell pm clear com.example.basictodoapp`, `adb -s emulator-5554 shell monkey -p com.example.basictodoapp 1` | PASS | `Performing Streamed Install`, `Success`, initial UI dump/screenshot captured |

## 3. Acceptance Criteria traceability matrix

| AC | Requirement | Result | Evidence / Notes |
|---|---|---:|---|
| AC-001 | Add valid non-empty task and show in list | PASS | Runtime added `OldTask` and `NewTask`; both displayed in `final-qa-two-tasks-newest.xml`. |
| AC-002 | Reject empty/whitespace add title with invalid-data message | PASS | Empty add submission kept dialog open and showed `Task title is required.` in `final-qa-add-empty-validation.xml`; whitespace rejection covered by unit test `invalidAddShowsValidationErrorAndDoesNotCallRepository`. |
| AC-003 | Added task has unique id and creation timestamp | PASS | Runtime shows created timestamp labels; unit/ViewModel and Room source support generated ids and timestamps. |
| AC-004 | Edit existing task title and display new title | PASS | Runtime changed `NewTask` to `EditedTask`; see `final-qa-edit-success.xml`. |
| AC-005 | Edit records updated time | PASS | Unit test `validEditTrimsTitleUpdatesExistingTaskAndClosesDialog` verifies repository update receives `updatedAt`; source `TaskDao.updateTitle` persists `updatedAt`. |
| AC-006 | Edited title remains after close/reopen | PASS | Force-stop/relaunch kept `EditedTask`; see `final-qa-reopen-after-edit-delete.xml`. |
| AC-007 | Reject empty/whitespace edit title with invalid-data message | PASS | Runtime cleared edit title and saw `Task title is required.` in `final-qa-edit-empty-validation.xml`; whitespace covered by unit test. |
| AC-008 | Delete asks for confirmation | PASS | Runtime showed `Delete task?` / `This task will be removed.` dialog; see `final-qa-delete-dialog.xml`. |
| AC-009 | Confirm delete removes selected task | PASS | Confirmed delete removed `OldTask`; see `final-qa-delete-confirmed.xml`. |
| AC-010 | Cancel delete leaves task unchanged | PASS | Cancel left `EditedTask` and `OldTask`; see `final-qa-delete-cancel.xml`. |
| AC-011 | Deleted task not restored after reopen | PASS | Force-stop/relaunch after delete showed `EditedTask` only; see `final-qa-reopen-after-edit-delete.xml`. |
| AC-012 | Newest-first sort displays newest to oldest | PASS | Default/reopened list shows newer task before older task; see `final-qa-two-tasks-newest.xml` and `final-qa-reopen-default-newest.xml`. |
| AC-013 | Oldest-first sort displays oldest to newest | PASS | Selecting `Oldest first` reordered `OldTask` before `NewTask`; see `final-qa-sort-oldest.xml`. |
| AC-014 | Sort preference not required to persist | PASS | After selecting oldest and reopening, sort reset to `Newest first`; see `final-qa-reopen-default-newest.xml`. |
| AC-015 | Tasks persist after close/reopen | PASS | `NewTask`/`OldTask` persisted after force-stop/relaunch before delete; see `final-qa-reopen-default-newest.xml`. |
| AC-016 | Add/edit/delete persistence remains consistent after reopen | PASS | After edit and delete, `EditedTask` remained and `OldTask` did not return; see `final-qa-reopen-after-edit-delete.xml`. |
| AC-017 | Main Android UI exposes core task management controls | FAIL | Core controls are present and usable, but design/accessibility details deviate: FAB/Edit/Delete/Sort are text controls without content descriptions/icon-button semantics; sort selected option has no checkmark/selected background. See BUG-001 and BUG-002. |

## 4. Manual/runtime test cases and results

| ID | Test case | Result | Evidence |
|---|---|---:|---|
| TC-001 | Install APK, clear app data, launch app | PASS | `final-qa-initial.png/xml` |
| TC-002 | Empty state shows app title, empty copy, sort control, FAB | PASS | `Basic ToDo`, `Newest first`, `No tasks yet`, `Add your first task to get started.`, `+` in `final-qa-initial.xml` |
| TC-003 | Add empty title | PASS | `Task title is required.` in `final-qa-add-empty-validation.xml` |
| TC-004 | Add two valid tasks | PASS | `NewTask`, `OldTask` visible in `final-qa-two-tasks-newest.xml` |
| TC-005 | Open sort menu | PASS | `Newest first` and `Oldest first` visible in `final-qa-sort-menu.xml` |
| TC-006 | Sort oldest first | PASS | `OldTask` before `NewTask` in `final-qa-sort-oldest.xml` |
| TC-007 | Reopen after sort | PASS | Tasks persisted; sort reset to `Newest first` in `final-qa-reopen-default-newest.xml` |
| TC-008 | Edit task with empty title | PASS | `Task title is required.` in `final-qa-edit-empty-validation.xml` |
| TC-009 | Edit task with valid title | PASS | `EditedTask` visible in `final-qa-edit-success.xml` |
| TC-010 | Delete cancel | PASS | Both `EditedTask` and `OldTask` remain in `final-qa-delete-cancel.xml` |
| TC-011 | Delete confirm | PASS | `OldTask` removed in `final-qa-delete-confirmed.xml` |
| TC-012 | Reopen after edit/delete | PASS | `EditedTask` remains; `OldTask` absent in `final-qa-reopen-after-edit-delete.xml` |

## 5. Invalid data test results

| Case | Result | Evidence |
|---|---:|---|
| Add empty title | PASS | Runtime validation message in `final-qa-add-empty-validation.xml` |
| Add whitespace-only title | PASS | Unit test `invalidAddShowsValidationErrorAndDoesNotCallRepository`; same validator used by add runtime path. |
| Add title trim behavior | PASS | Unit test `validAddTrimsTitlePersistsTaskAndClosesDialog` and `TaskTitleValidatorTest.normalizeOrNullTrimsValidTitle`. |
| Edit empty title | PASS | Runtime validation message in `final-qa-edit-empty-validation.xml` |
| Edit whitespace-only title | PASS | Unit test `invalidEditShowsValidationErrorAndDoesNotUpdateRepository`; same validator used by edit runtime path. |
| Edit title trim behavior | PASS | Unit test `validEditTrimsTitleUpdatesExistingTaskAndClosesDialog`. |
| Duplicate title | PASS | Runtime added duplicate `EditedTask`; see `final-qa-long-duplicate.xml`. Duplicate titles are allowed by UI spec. |
| Very long title | PASS | Runtime added `VeryLongTaskTitle_ABCDEFGHIJKLMNOPQRSTUVWXYZ_0123456789_abcdefghijklmnopqrstuvwxyz`; list remained usable in `final-qa-long-duplicate.xml`. |
| Rapid add/edit/delete | PASS with limited coverage | Normal sequential adb smoke did not show freeze/crash. No dedicated stress automation exists. |

## 6. UI/design verification notes

- PASS: Theme uses required pink primary `#E91E63`, background/surface tokens align with design.
- PASS: App title, empty state copy, add/edit/delete dialogs, validation copy, delete confirmation copy, created-time labels, and two sort options match the approved spec.
- PASS: Add/Edit use `AlertDialog`; spec says `ModalBottomSheet` preferred but `AlertDialog` acceptable.
- FAIL: Several controls are text-based without expected icon/content-description accessibility semantics: FAB text `+`, text buttons `Edit`/`Delete`, and text sort button. See BUG-001.
- FAIL: Sort selected option has no check mark or selected background. See BUG-002.

## 7. Bug list summary

Detailed bug list is written to `/Users/lethimythao/.openclaw/workspace-shared/qa/bug-list.md`.

| Bug ID | Severity | Status | Title |
|---|---|---|---|
| BUG-001 | Medium | ACCEPTED_FOR_DEMO | Task action controls do not meet icon/content-description design spec |
| BUG-002 | Low | ACCEPTED_FOR_DEMO | Sort menu does not visually indicate selected option |

## 8. QA recommendation

Recommendation: `FINAL_QA_ACCEPTED_FOR_DEMO_REVIEW`.

Functional MVP behavior for Add, Edit, Delete, Sort, validation, and local persistence is verified with Gradle and emulator evidence. Product accepted BUG-001 and BUG-002 for the current debug demo/review build on `2026-09-29 AEST`; both remain deferred UI/accessibility polish items before production sign-off.
