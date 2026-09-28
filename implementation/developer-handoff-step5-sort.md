# Developer Handoff – Step 5 Sort Only

**Status:** STEP_5_SORT_READY_FOR_REVIEW

## Implemented

- Replaced non-interactive sort placeholder with a working Material 3 dropdown control.
- Added ViewModel action `onSortOrderSelected`.
- Wired `SortOrder.NEWEST_FIRST` and `SortOrder.OLDEST_FIRST` through UI/ViewModel/repository observations.
- Task list updates immediately when sort selection changes.
- Added focused unit tests for default newest-first, oldest-first, and switching back to newest-first behavior.

## Preserved

- Add behavior remains working.
- Edit behavior remains working.
- Delete behavior remains working.
- No unrelated features or refactors were added.

## Verification

See `/Users/lethimythao/.openclaw/workspace-shared/implementation/build-test-report-step5-sort.md`.
