# FINAL QA Bug List - BasicToDoApp

QA status: `FINAL_QA_ACCEPTED_FOR_DEMO_REVIEW`
Test date/time: `2026-09-29 06:48:25 AEST`
APK: `/Users/lethimythao/.openclaw/workspace-shared/app/BasicToDoApp/app/build/outputs/apk/debug/app-debug.apk`
Device: `emulator-5554`

## Bugs

| ID | Title | Severity | Status |
|---|---|---:|---|
| BUG-001 | Task action controls do not meet icon/content-description design spec | Medium | ACCEPTED_FOR_DEMO |
| BUG-002 | Sort menu does not visually indicate selected option | Low | ACCEPTED_FOR_DEMO |

Product decision:

- Decision date: `2026-09-29 AEST`
- Decision: BUG-001 and BUG-002 are accepted for the current debug demo/review build.
- Follow-up: defer both issues to a future UI/accessibility polish pass before production sign-off.

## BUG-001 - Task action controls do not meet icon/content-description design spec

- Severity: Medium
- Status: ACCEPTED_FOR_DEMO
- Product decision: Accepted for current debug demo/review build; defer to future UI/accessibility polish before production.
- Area: Android UI / accessibility / design consistency
- Related AC: AC-017, UI Specifications: FAB, task list item actions, sort control

Steps to reproduce:

1. Install and launch the APK on `emulator-5554`.
2. Observe the main screen empty state and populated list.
3. Add two tasks so rows are visible.
4. Inspect UI hierarchy dumps:
   - `/Users/lethimythao/.openclaw/workspace-shared/qa/evidence/final-qa-initial-window.xml`
   - `/Users/lethimythao/.openclaw/workspace-shared/qa/evidence/final-qa-two-tasks-newest.xml`

Expected:

- FAB should use an add icon with content description `Add task`.
- Task row actions should be icon buttons with 48 x 48 dp target and content descriptions `Edit task` and `Delete task`.
- Sort should be an icon action in the top app bar.

Actual:

- FAB exposes visible text `+` and no content description in the UI dump.
- Row actions expose visible text `Edit` and `Delete`; no `content-desc` for `Edit task` / `Delete task`.
- Sort is a text button labelled `Newest first` / `Oldest first`, not an icon action.

Evidence:

- `final-qa-initial-window.xml`: FAB node has text `+`, `content-desc=""`.
- `final-qa-two-tasks-newest.xml`: task action nodes show text `Edit` and `Delete`, `content-desc=""`.
- `final-qa-sort-menu.xml`: sort is opened from text label, not icon action.

Impact:

- Core behavior remains usable, but implementation does not fully match approved UI/accessibility specification and may be less accessible for assistive technologies.

## BUG-002 - Sort menu does not visually indicate selected option

- Severity: Low
- Status: ACCEPTED_FOR_DEMO
- Product decision: Accepted for current debug demo/review build; defer to future UI polish before production.
- Area: Android UI / design consistency
- Related AC: AC-017, Sort Control specification

Steps to reproduce:

1. Launch app with at least one task.
2. Tap the sort control in the top app bar.
3. Observe the `Newest first` / `Oldest first` dropdown.
4. Select `Oldest first`, then open the menu again.

Expected:

- The selected sort option should show a check mark or selected background per UI specification.

Actual:

- Dropdown lists `Newest first` and `Oldest first`, but the UI dump does not expose any selected-state marker, check mark, or selected background.

Evidence:

- `/Users/lethimythao/.openclaw/workspace-shared/qa/evidence/final-qa-sort-menu.xml`
- `/Users/lethimythao/.openclaw/workspace-shared/qa/evidence/final-qa-sort-oldest.xml`

Impact:

- Sorting works correctly, but the selected menu state is less clear and does not fully match design requirements.

## Verified non-defects / passing behavior

- Add valid task: PASS.
- Add empty title validation: PASS.
- Edit valid title: PASS.
- Edit empty title validation: PASS.
- Delete confirmation, cancel, and confirm: PASS.
- Newest/oldest sort behavior: PASS.
- Sort preference reset after reopen: PASS.
- Local persistence after add/edit/delete: PASS.
- Duplicate titles: PASS; allowed by UI specification.
- Very long title: PASS; accepted and list remains usable.
