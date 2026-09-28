# Design Handoff - BasicToDoApp Android UI

## Status

Status: `PASS WITH PARENT RECOVERY`

Designer was called in a separate Designer dashboard session with Figma context:

- Session: `agent:designer:dashboard:eb15663e-1e5b-4351-a346-f53d2098e7c4`
- Run ID: `872776ed-d5d4-4a9d-be4f-926edf92df65`
- Requested artifact: Android BasicToDoApp UI in Figma, primary color pink, plus Developer UI Specifications.

The Designer run started and used Figma-related tooling, but did not return a terminal handoff summary during parent verification. To keep the delivery unblocked, the parent session created the Figma artifact directly in the same prepared Figma file and rewrote the handoff/spec files.

## Figma Artifact

- URL: https://www.figma.com/design/rTt7yFfdmtdYoG3xAQRkrg
- File key: `rTt7yFfdmtdYoG3xAQRkrg`
- Page: `BasicToDoApp - Pink Material 3`
- Primary color: pink `#E91E63`
- Font used in Figma: Roboto
- Screens created: 7

Created screen frames:

| Frame | Node ID |
|---|---|
| `Android Screen / Task List - Empty` | `4:14` |
| `Android Screen / Task List - Populated` | `4:26` |
| `Android Screen / Add Task` | `4:59` |
| `Android Screen / Edit Task` | `4:104` |
| `Android Screen / Delete Confirmation` | `4:149` |
| `Android Screen / Sort Menu` | `4:191` |
| `Android Screen / Validation Error` | `4:231` |

## Files

- UI Specifications: `workspace-shared/design/ui-specifications.md`
- Design Handoff: `workspace-shared/design/design-handoff.md`

## UI Coverage

- Empty ToDo list: covered.
- Populated ToDo list: covered.
- Add task: covered with FAB + modal bottom sheet.
- Edit task: covered with modal bottom sheet.
- Delete: covered with confirmation dialog.
- Sort newest/oldest: covered with dropdown menu.
- Empty title validation: covered with error field state.
- Material 3 / Jetpack Compose guidance: covered.
- Primary pink color: covered with `#E91E63`.

## Implementation Guidance

Build a single Android screen in Jetpack Compose using Material 3:

- `Scaffold`
- `TopAppBar`
- `LazyColumn`
- `FloatingActionButton`
- `DropdownMenu`
- `ModalBottomSheet` or `AlertDialog`
- `OutlinedTextField`
- `AlertDialog` for delete confirmation

Keep sort as UI state only. Sort by `createdAt` with `Newest first` and `Oldest first`. Validate add/edit with `title.trim().isNotEmpty()` and show `Task title is required.` on error.

## Notes For Developer

- The Figma artifact is editable layer-based UI, not a flattened screenshot.
- The UI is intentionally MVP-scoped. Do not add login, cloud sync, reminders, priority, completed state, AI, or third-party services.
- The earlier no-Figma/no-Canvas blockers applied to older Designer/main attempts. This handoff supersedes those for implementation.
