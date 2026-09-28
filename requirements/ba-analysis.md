# BA Analysis – BasicToDoApp Product Brief

**Artifact type:** Business Analysis
**Source:** `product-brief.md`
**Version analyzed:** Product Brief v1.0 – MVP
**Date:** 2026-09-28

## Goal

BasicToDoApp aims to deliver a simple Android MVP for personal daily task management, focused on four core actions: add, edit, delete, and sort tasks. The product also serves as a pilot project to validate a 6-agent software delivery workflow from BA through DevOps.

## Stakeholders / Users

| Stakeholder / User | Need / Interest |
|---|---|
| Personal end user | Manage daily tasks on one Android device without account registration or Internet. |
| BA Agent | Convert brief into clear requirements, user stories, and acceptance criteria. |
| Architect Agent | Define technical plan and Android compatibility baseline. |
| Designer Agent | Produce Android UI design and specifications. |
| Developer Agent | Implement the MVP and create build output. |
| QA Agent | Verify functional and acceptance criteria. |
| DevOps Agent | Package and release APK demo after QA pass. |

## Problem Statement

Personal users need a lightweight Android app to record and manage daily tasks without complex project-management features, login, cloud sync, or Internet dependency. The MVP must preserve task data locally and provide clear, fast interactions for adding, editing, deleting, and sorting tasks.

## Scope

### In Scope

- Add a task with a non-empty title.
- Edit an existing task title, rejecting empty or whitespace-only titles.
- Delete a task after user confirmation.
- Sort task list by:
  - creation time newest first;
  - creation time oldest first.
- Persist task data locally on the Android device.
- Provide a simple Android UI centered on one main screen.
- Produce a demo APK installable on compatible Android devices.

### Out of Scope

- Account registration, login, or account management.
- Cloud synchronization.
- Sharing tasks with other users.
- Deadlines, reminders, or notifications.
- Categories, labels, or priority.
- Completion status and progress statistics.
- AI integration or third-party service integration.
- Multi-user or multi-device use.

## User Journeys / Use Cases

### UC-001 – Add task

1. User opens the app.
2. User enters a task title.
3. User submits the task.
4. App validates that the title is not empty or whitespace-only.
5. App adds the task to the list and stores it locally.

### UC-002 – Edit task

1. User views the task list.
2. User selects edit for a task.
3. User changes the task title.
4. App validates that the updated title is not empty or whitespace-only.
5. App saves the updated title.
6. App displays the updated task in the list.

### UC-003 – Delete task

1. User views the task list.
2. User selects delete for a task.
3. App asks for confirmation.
4. If user confirms, app removes the task and persists the change.
5. If user cancels, the task remains unchanged.

### UC-004 – Sort tasks

1. User views the task list.
2. User chooses a supported sort option.
3. App reorders the displayed list according to the selected option.

### UC-005 – Reopen app with persisted data

1. User creates or updates tasks.
2. User closes the app.
3. User reopens the app.
4. Previously saved tasks are still available.

## Functional Requirements

### FR-001 – Add task

The app shall allow the user to create a task by entering a task title.

**Business rules**

- Task title must not be empty.
- Task title must not contain only whitespace.
- A created task must have at minimum: `id`, `title`, `createdAt`, `updatedAt`.

**Acceptance Criteria**

- **AC-001** Given the user enters a valid non-empty task title, when the user adds the task, then the task is created and displayed in the task list.
- **AC-002** Given the user enters an empty or whitespace-only title, when the user tries to add the task, then the app rejects the input and shows an invalid-data message.
- **AC-003** Given a task is added successfully, when the app stores the task, then the task includes a unique identifier and creation timestamp.

### FR-002 – Edit task

The app shall allow the user to edit the title of an existing task.

**Business rules**

- Updated task title must not be empty.
- Updated task title must not contain only whitespace.

**Acceptance Criteria**

- **AC-004** Given an existing task, when the user updates its title, then the task list displays the new title.
- **AC-005** Given a task title is edited, when the change is saved, then the task's latest update time is recorded.
- **AC-006** Given the app is closed and reopened after an edit, when the task list is displayed, then the edited title remains available.
- **AC-007** Given the user enters an empty or whitespace-only title while editing, when the user tries to save the change, then the app rejects the input and shows an invalid-data message.

### FR-003 – Delete task

The app shall allow the user to delete an existing task only after confirmation.

**Acceptance Criteria**

- **AC-008** Given an existing task, when the user requests deletion, then the app asks for confirmation before deleting.
- **AC-009** Given the user confirms deletion, when the deletion is processed, then the selected task is removed from the list.
- **AC-010** Given the user cancels deletion, when the confirmation is dismissed, then the task remains in the list.
- **AC-011** Given a task is deleted and the app is reopened, when the list is displayed, then the deleted task is not restored.

### FR-004 – Sort tasks

The app shall allow the user to sort tasks by supported sort options.

**Acceptance Criteria**

- **AC-012** Given multiple tasks with different creation times, when the user selects newest first, then tasks are displayed from newest to oldest.
- **AC-013** Given multiple tasks with different creation times, when the user selects oldest first, then tasks are displayed from oldest to newest.
- **AC-014** Given the app is closed and reopened after selecting a sort option, when the task list is displayed again, then the app is not required to remember the previous sort selection.

### FR-005 – Local persistence

The app shall store task data locally on the Android device so task data remains after closing and reopening the app.

**Acceptance Criteria**

- **AC-015** Given tasks exist in the app, when the user closes and reopens the app, then the saved tasks are still displayed.
- **AC-016** Given task data is added, edited, or deleted, when persistence is relevant, then the task data state remains consistent after reopening the app.

### FR-006 – Main screen task management

The app shall provide a simple Android interface where the user can access the four core functions from the main screen.

**Acceptance Criteria**

- **AC-017** Given the user opens the app, when the main screen loads, then the app title, task input, add control, task list, task creation time, edit control, delete control, and sort control are available as defined in the brief.

## Non-Functional Requirements

| ID | Requirement | Acceptance / Verification Direction |
|---|---|---|
| NFR-001 Performance | Add, edit, delete, and sort actions should respond quickly and not freeze the UI. | QA verifies core actions complete without visible UI hang during normal MVP use. |
| NFR-002 Usability | Interface must be simple and understandable for personal task management. | QA verifies invalid input feedback and basic task actions are discoverable on the main screen. |
| NFR-003 Persistence | Task data must remain after app close/reopen. | Covered by AC-015 and AC-016. |
| NFR-004 Offline usage | Core functions must work without Internet. | QA verifies add/edit/delete/sort while offline. |
| NFR-005 Privacy / Security | App must not require account credentials and must not transmit personal task data to a server. | QA/technical review verifies no login flow and no required network dependency for core functions. |
| NFR-006 Compatibility | App must run on Android; minimum Android version is to be defined by Architect. | Architect defines minimum version; QA validates on compatible device/emulator. |

## Business Rules

- The MVP serves a single user on a single Android device.
- Core functions must be usable offline.
- Task title cannot be empty or whitespace-only when adding a task.
- Task title cannot be empty or whitespace-only when editing a task.
- Deletion requires explicit confirmation.
- Each task must include `id`, `title`, `createdAt`, and `updatedAt`.
- Sort is limited to creation time: newest first and oldest first.
- Sort preference does not need to persist after app close/reopen.
- Technology and local storage approach are Architect decisions, not BA decisions.

## Assumptions

- The task list may contain multiple tasks.
- Data persistence applies to task content and deletion results; sort preference persistence is explicitly not required.
- Duplicate task titles are not explicitly forbidden in the brief.
- The demo APK is for testing/demo purposes, not public store release.

## Risks

| Risk | Impact | Mitigation / Clarification Needed |
|---|---|---|
| Android minimum version undefined | Architect/QA may target different baselines. | Architect to define in technical plan before implementation/testing. |
| No volume/performance benchmark | “Fast” may be interpreted differently by QA and Dev. | Define approximate expected task count or qualitative test baseline. |

## Gaps / Inconsistencies Detected

1. **Duplicate titles not specified:** The brief does not say whether duplicate task names are allowed.
2. **Task title constraints missing:** No maximum length, allowed characters, or trimming behavior is specified.
3. **Empty state behavior not specified:** No requirement for how the app displays an empty task list.
4. **Error/message wording not specified:** Invalid input feedback is required, but exact message content is not defined.
5. **APK demo target unspecified:** Device/emulator target and Android minimum version are left to Architect, which is acceptable but must be completed before QA.

## Open Questions

1. Should task titles be trimmed before saving?
2. Are duplicate task titles allowed?
3. Is there a maximum task title length?
4. What empty-state message should be shown when there are no tasks?
5. Should `updatedAt` be displayed to the user or only stored internally?
6. What Android minimum version and device/emulator profile should QA use?
7. What task count should be used as a normal performance baseline for MVP testing?

## Handoff Readiness

**Readiness:** Ready for Architect with minor open questions.

The brief is sufficient to start technical planning for a simple Android MVP. Sort scope, edit validation, and sort preference persistence have been clarified.
