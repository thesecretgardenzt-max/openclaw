# Technical Plan – BasicToDoApp Android MVP

## artifact_type
technical_architecture

## schema_version
1.0.0

## source_ba_artifact
- Product Brief: `product-brief/product-brief.md`
- BA Analysis: `product-brief/ba-analysis.md`

## scope_trace
| Requirement | Source | Architecture coverage |
|---|---|---|
| FR-001 Add task | BA Analysis FR-001 / AC-001..AC-003 | Main screen input, ViewModel validation, Room insert |
| FR-002 Edit task | BA Analysis FR-002 / AC-004..AC-007 | Edit dialog/sheet, ViewModel validation, Room update with `updatedAt` |
| FR-003 Delete task | BA Analysis FR-003 / AC-008..AC-011 | Confirmation dialog, repository delete, refreshed list |
| FR-004 Sort tasks | BA Analysis FR-004 / AC-012..AC-014 | UI sort selector for `createdAt DESC` / `createdAt ASC`; sort preference kept only in memory |
| FR-005 Local persistence | BA Analysis FR-005 / AC-015..AC-016 | Room database stored locally on device |
| FR-006 Main screen | BA Analysis FR-006 / AC-017 | Single Activity, single task-management screen |
| NFR-001..006 | BA Analysis NFR table | Coroutine-backed data access, offline-only design, no accounts/network, Android baseline defined below |

## architecture_overview
Build BasicToDoApp as a small native Android MVP using one app module and a simple MVVM-style architecture.

Recommended structure:

```
app/
  ui/              MainActivity, composables, dialogs, UI state
  viewmodel/       TaskViewModel, validation and user actions
  data/            TaskRepository, Room DAO/database/entity
  domain/          Task model, SortOrder enum, validation helpers
```

Data flow:

1. User interacts with the main screen.
2. UI calls `TaskViewModel` actions: add, edit, request delete, confirm delete, change sort.
3. ViewModel validates title using `trim().isNotEmpty()` for add/edit.
4. Repository delegates persistence to Room DAO.
5. DAO exposes tasks sorted by creation time.
6. ViewModel exposes `StateFlow<TaskUiState>` to UI.

## architecture_decisions
| ID | Decision | Status | Requirement trace | Rationale |
|---|---|---|---|---|
| AD-001 | Use Kotlin for Android implementation | accepted | All FR/NFR | Standard Android language, concise, good coroutine/Room support |
| AD-002 | Use Jetpack Compose for UI | accepted | FR-006, NFR-002 | Single-screen MVP can be implemented quickly with less XML boilerplate |
| AD-003 | Use MVVM with ViewModel + StateFlow | accepted | All FR | Keeps UI simple and testable without over-engineering |
| AD-004 | Use Room for local persistence | accepted | FR-001..FR-005, NFR-003 | Reliable local SQLite abstraction, supports persistence after app restart |
| AD-005 | Minimum SDK: Android 8.0 / API 26 | accepted | NFR-006 | Reasonable compatibility while allowing modern Java time APIs and current tooling |
| AD-006 | Sort preference is UI/session state only, not persisted | accepted | FR-004 / AC-014 | Explicitly not required to remember sort after app close/reopen |
| AD-007 | Duplicate task titles are allowed | proposed | BA assumption/gap | Requirement does not forbid duplicates; blocking them would add a new business rule |
| AD-008 | Trim title before validation and save trimmed value | proposed | FR-001, FR-002 | Prevents whitespace-only titles and avoids storing accidental edge spaces; confirm if Product wants raw spaces preserved |
| AD-009 | `updatedAt` is stored but not required to display | proposed | FR-002 / AC-005; BA open question | Brief requires display of title and creation time only |

## technology_stack
| Area | Choice | Reason |
|---|---|---|
| Language | Kotlin | Native Android standard, readable for MVP |
| UI | Jetpack Compose + Material 3 | Fast single-screen UI, built-in dialogs/buttons/text fields |
| Architecture | MVVM, Repository pattern | Clear separation; enough for MVP without extra complexity |
| Async/state | Kotlin Coroutines + Flow/StateFlow | Room integration and lifecycle-aware state updates |
| Persistence | Room SQLite | Local durable storage; no Internet/account dependency |
| Build | Gradle Android plugin | Standard APK generation |
| Unit tests | JUnit, Kotlin coroutines test | ViewModel/validation/repository tests |
| UI tests | Compose UI Test, AndroidX Test | Main screen flows and validation feedback |

## module_layer_component_design
### UI layer
Components:
- `MainActivity`: app entry point, hosts Compose content.
- `TaskScreen`: displays title, input field, add button, sort selector, task list, empty state.
- `TaskItemRow`: shows task title, creation time, edit action, delete action.
- `EditTaskDialog`: edits a selected task title.
- `ConfirmDeleteDialog`: confirms deletion before repository mutation.

Responsibilities:
- Render `TaskUiState`.
- Forward user events to ViewModel.
- Show validation error messages for add/edit.
- Do not contain persistence or business logic.

### ViewModel layer
Components:
- `TaskViewModel`
- `TaskUiState`
- `SortOrder` enum: `NEWEST_FIRST`, `OLDEST_FIRST`

Responsibilities:
- Own current sort order in memory only.
- Combine selected sort order with repository task stream.
- Validate add/edit title: reject empty or whitespace-only values.
- Create timestamps for new/updated tasks.
- Emit one-time or state-backed error messages.

### Domain layer
Components:
- `Task` domain model.
- `TaskTitleValidator` or simple validation function.

Responsibilities:
- Define task fields required by BA.
- Keep validation reusable for unit tests.

### Data layer
Components:
- `TaskEntity`
- `TaskDao`
- `AppDatabase`
- `TaskRepository`

Responsibilities:
- Store tasks locally.
- Query ordered by `createdAt` ascending/descending.
- Insert/update/delete tasks.
- Isolate Room details from ViewModel.

## interfaces
### TaskRepository
```kotlin
interface TaskRepository {
    fun observeTasks(sortOrder: SortOrder): Flow<List<Task>>
    suspend fun addTask(title: String, createdAt: Long, updatedAt: Long)
    suspend fun updateTask(id: Long, title: String, updatedAt: Long)
    suspend fun deleteTask(id: Long)
}
```

### TaskDao
```kotlin
@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    fun observeNewestFirst(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks ORDER BY createdAt ASC")
    fun observeOldestFirst(): Flow<List<TaskEntity>>

    @Insert
    suspend fun insert(task: TaskEntity)

    @Query("UPDATE tasks SET title = :title, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateTitle(id: Long, title: String, updatedAt: Long)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteById(id: Long)
}
```

## data_model_and_local_persistence
### Data model
| Field | Type | Required | Requirement trace | Notes |
|---|---|---:|---|---|
| `id` | Long | yes | FR-001 / AC-003 | Auto-generated Room primary key |
| `title` | String | yes | FR-001, FR-002 | Trimmed non-empty title; max length not specified |
| `createdAt` | Long epoch millis | yes | FR-001, FR-004 | Used for display and sort |
| `updatedAt` | Long epoch millis | yes | FR-002 / AC-005 | Updated on title edit |

Room entity:

```kotlin
@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val createdAt: Long,
    val updatedAt: Long
)
```

Persistence approach:
- Store only task data in Room database.
- Do not persist selected sort order.
- Use migration strategy appropriate for MVP: destructive migration is acceptable only before demo release; once QA/release artifacts exist, add explicit migrations for schema changes.
- No remote sync, no account data, no network persistence.

## ui_navigation_approach
- Single Activity, single Compose screen; no navigation graph required for MVP.
- Add task directly from top input area.
- Edit via modal dialog or bottom sheet opened from a task row.
- Delete via confirmation dialog.
- Sort via segmented buttons, dropdown, or two-option selector: `Newest first`, `Oldest first`.
- Display creation time on each row using a simple local date/time format.
- Empty state may show a neutral message such as “No tasks yet” because empty-state text is not specified by BA; keep it non-business-changing.

## required_behaviors
### Sort behavior
- Only supported sort key: `createdAt`.
- Supported orders:
  - newest first: `createdAt DESC`;
  - oldest first: `createdAt ASC`.
- Do not sort by title, update time, priority, completion, or any other field.
- Do not persist sort preference after app close/reopen.

### Validation behavior
- Add and edit must reject title values where `title.trim().isEmpty()`.
- Show an invalid-data message when rejecting input.
- Proposed save behavior: save `title.trim()`.
- Duplicate titles remain allowed unless Product/BA adds a rule forbidding them.

## error_handling_offline_security_privacy
Error handling:
- Validation errors are shown inline near the relevant input or inside the edit dialog.
- Persistence errors, if surfaced, should show a generic non-sensitive message and keep the app usable.
- Delete requires explicit confirmation; cancellation must leave data unchanged.

Offline:
- App core functionality must work fully offline.
- Do not add network permission unless future requirements need it.

Security/privacy:
- No login, credentials, cloud sync, analytics, or third-party services for MVP.
- Task content stays on device in app-local storage.
- No secrets or API keys required.
- If backups are enabled by Android defaults, Developer should document behavior; no custom backup/sync is required.

## testing_strategy
### Unit tests
Trace: FR-001, FR-002, FR-004.
- Title validation rejects empty and whitespace-only strings.
- Title validation accepts non-empty strings.
- Sort order maps to correct DAO/repository query.
- ViewModel add/edit calls repository only when input is valid.

### Data/persistence tests
Trace: FR-001..FR-005.
- Insert task persists id/title/createdAt/updatedAt.
- Update changes title and updatedAt without changing createdAt.
- Delete removes only selected task.
- Query newest/oldest returns expected order.

### Compose/UI tests
Trace: FR-001..FR-006.
- Main screen shows app title, input, add, list, creation time, edit, delete, sort controls.
- Add valid task displays it.
- Add/edit invalid title shows error and does not persist.
- Delete cancel keeps task; delete confirm removes task.
- Sort newest/oldest reorders displayed tasks.

### Manual QA guidance
- Test on Android API 26+ emulator/device.
- Test offline mode.
- Close/reopen app after add/edit/delete to verify persistence.
- Confirm sort preference does not need to survive app restart.

## developer_implementation_guidance
1. Keep MVP in one Android app module unless repository standards require otherwise.
2. Implement data layer first with Room and tests.
3. Implement ViewModel validation and sort state before UI wiring.
4. Build UI with clear test tags for QA automation: input, add button, sort selector, task rows, edit/delete buttons, dialogs.
5. Keep strings in resources for easier QA/reference.
6. Avoid adding out-of-scope features: completion checkbox, priority, deadlines, reminders, login, cloud sync, labels, statistics.
7. Use small commits or PR sections aligned with the task breakdown.

## technical_risks
| Risk | Type | Requirement trace | Impact | Mitigation |
|---|---|---|---|---|
| Exact empty-state text unspecified | REQUIREMENT_GAP | FR-006 / BA gap | QA may expect different wording | Use neutral text and document it in UI specs |
| Max title length unspecified | REQUIREMENT_GAP | FR-001, FR-002 | Very long titles may affect UI | Do not enforce business max length unless BA/Product confirms; UI should wrap/ellipsize gracefully |
| Duplicate title behavior unspecified | REQUIREMENT_GAP | FR-001 | Potential QA ambiguity | Allow duplicates; note no rule forbids them |
| Android target profile unspecified | REQUIREMENT_GAP | NFR-006 | QA environment mismatch | Use minSdk 26; QA validates on API 26+ emulator/device |
| Performance benchmark unspecified | REQUIREMENT_GAP | NFR-001 | Hard to quantify “fast” | QA uses normal MVP list sizes; Developer keeps DB operations off main thread |

## handoff_to_developer
### Ready inputs
- Implement Android MVP according to FR-001..FR-006 and AC-001..AC-017.
- Use Kotlin, Compose, MVVM, Room.
- Use API 26 as minimum Android version unless project constraints override.

### unresolved_decisions
- Confirm whether trimming before save is acceptable. Proposed: yes.
- Confirm whether duplicate titles are allowed. Proposed: yes.
- Confirm whether any title length cap is needed. Proposed: no explicit cap for MVP.
- Confirm final empty-state and validation message wording with Designer/BA if exact copy matters.
