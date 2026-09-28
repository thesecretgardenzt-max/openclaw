# BasicToDoApp UI Specifications

## Design Source

- Figma file: https://www.figma.com/design/rTt7yFfdmtdYoG3xAQRkrg
- Figma page: `BasicToDoApp - Pink Material 3`
- Primary color: pink `#E91E63`
- Baseline device: Android phone, `360 x 800 dp`
- Design system direction: Material 3, Jetpack Compose friendly

The Figma artifact contains editable frames for:

1. `Android Screen / Task List - Empty`
2. `Android Screen / Task List - Populated`
3. `Android Screen / Add Task`
4. `Android Screen / Edit Task`
5. `Android Screen / Delete Confirmation`
6. `Android Screen / Sort Menu`
7. `Android Screen / Validation Error`

## Scope Decisions

- MVP only: list, add, edit, delete, and sort tasks.
- No login, sync, reminders, priority, completed state, labels, AI, or third-party services.
- Sort options are only `Newest first` and `Oldest first`.
- Sort key is `createdAt` only.
- Sort preference is in-memory UI state only and does not need to persist after app restart.
- Title validation rule: `title.trim().isNotEmpty()`.
- Save trimmed title.
- Duplicate task titles are allowed unless product/domain rules later change.

## Visual Tokens

| Token | Value |
|---|---|
| Primary | `#E91E63` |
| On primary | `#FFFFFF` |
| Primary container | `#FFD8E7` |
| On primary container | `#3F001C` |
| Background | `#FFF8FA` |
| Surface | `#FFFBFF` |
| Surface container | `#F7EEF3` |
| Outline | `#84737C` |
| Error | Material error / `#BA1A1A` |
| Shape | 8 dp for task rows, 16 dp for FAB, 24-28 dp for modal/dialog containers |
| Font | Roboto in Figma; use Material 3 typography in Compose |

## App Structure

Use one Compose screen with modal state owned by the screen state.

- Root: `Scaffold`
- Top: Material 3 `TopAppBar`
- Body: `LazyColumn` when tasks exist, empty-state content when not
- Primary action: bottom-end `FloatingActionButton`
- Sort: top app bar action opens `DropdownMenu`
- Add/Edit: `ModalBottomSheet` preferred; `AlertDialog` acceptable if project scope prefers dialogs
- Delete: `AlertDialog`

## Task List Screen

- Top app bar height: 64 dp.
- App title: `Basic ToDo`.
- Sort icon action at trailing edge.
- Content padding: 16 dp horizontal, 12 dp top, 88 dp bottom for FAB clearance.
- List: `LazyColumn`.
- List item spacing: 8 dp.
- FAB: bottom-end, 16 dp margin, 56 dp size, pink primary fill, add icon, content description `Add task`.

## Task List Item

- Container color: `surfaceContainer`.
- Shape: 8 dp radius.
- Minimum height: 64 dp.
- Padding: 16 dp start, 4-8 dp around trailing actions, 10-12 dp vertical.
- Title: `bodyLarge`, max 2 lines, ellipsized overflow.
- Optional supporting created-time text: `Created Sep 28, 2026, 10:30 AM` if created-time label exists.
- Actions:
  - Edit icon button, 48 x 48 dp target, content description `Edit task`.
  - Delete icon button, 48 x 48 dp target, content description `Delete task`.

## Empty State

- Center the empty state in the available body area.
- Copy:
  - Title: `No tasks yet`
  - Body: `Add your first task to get started.`
- FAB remains visible and opens Add Task.
- Do not add onboarding or marketing copy.

## Add Task

- Component: `ModalBottomSheet` preferred.
- Title: `Add task`.
- Field: `OutlinedTextField`.
- Field label: `Task title`.
- Initial field value: empty.
- Actions: `Cancel`, `Add`.
- On Add:
  - Trim title.
  - Validate non-empty.
  - Create task with current `createdAt`.
  - Dismiss only after successful creation.

## Edit Task

- Component: same pattern as Add.
- Title: `Edit task`.
- Field label: `Task title`.
- Field prefilled with current task title.
- Actions: `Cancel`, `Save`.
- On Save:
  - Trim title.
  - Validate non-empty.
  - Update selected task.
  - Dismiss only after successful save.

## Validation State

- Rule: `title.trim().isNotEmpty()`.
- Error copy: `Task title is required.`
- Use `OutlinedTextField(isError = true)` and supporting error text.
- Keep focus on the field after failed submit.
- Do not save whitespace-only titles.

## Delete Confirmation

- Component: Material 3 `AlertDialog`.
- Title: `Delete task?`.
- Body: `This task will be removed.`
- Actions: `Cancel`, `Delete`.
- Delete action uses Material error color.
- Back/outside dismiss behaves like Cancel.

## Sort Control

- Sort icon in top app bar opens anchored `DropdownMenu`.
- Options:
  - `Newest first`
  - `Oldest first`
- Selected option shows a check mark or selected background.
- Sorting applies immediately to visible list.
- Do not add A-Z, Z-A, priority, updated-time, or completed-state sorting.

## Compose Mapping

Recommended components:

- `Scaffold`
- `TopAppBar`
- `LazyColumn`
- `ListItem` or custom `Surface` row
- `FloatingActionButton`
- `DropdownMenu`
- `DropdownMenuItem`
- `ModalBottomSheet`
- `AlertDialog`
- `OutlinedTextField`
- `TextButton`
- `Button`
- `IconButton`

Suggested state model:

```kotlin
data class TaskUiState(
    val tasks: List<TaskUiModel> = emptyList(),
    val sortOrder: SortOrder = SortOrder.NEWEST_FIRST,
    val addSheet: AddTaskState? = null,
    val editSheet: EditTaskState? = null,
    val pendingDeleteTask: TaskUiModel? = null
)

data class TaskUiModel(
    val id: Long,
    val title: String,
    val createdAtLabel: String
)
```

## Developer Acceptance Checklist

- Empty list state is implemented.
- Populated list state is implemented.
- Add task works and validates empty/whitespace title.
- Edit task works and validates empty/whitespace title.
- Delete requires confirmation.
- Sort supports only newest/oldest by `createdAt`.
- Primary color uses pink `#E91E63` or theme-derived equivalent.
- Touch targets are at least 48 x 48 dp.
- Long task titles do not break row layout.
- No out-of-scope features are introduced.
