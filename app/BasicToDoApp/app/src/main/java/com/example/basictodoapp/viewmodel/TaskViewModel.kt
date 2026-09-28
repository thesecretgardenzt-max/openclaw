package com.example.basictodoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.basictodoapp.data.TaskRepository
import com.example.basictodoapp.domain.SortOrder
import com.example.basictodoapp.domain.Task
import com.example.basictodoapp.domain.TaskTitleValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(
    private val repository: TaskRepository,
    private val nowProvider: () -> Long = { System.currentTimeMillis() }
) : ViewModel() {
    private val addDialogState = MutableStateFlow(AddTaskDialogState())
    private val editDialogState = MutableStateFlow(EditTaskDialogState())
    private val deleteDialogState = MutableStateFlow(DeleteTaskDialogState())
    private val sortOrder = MutableStateFlow(SortOrder.NEWEST_FIRST)

    private val sortedTasks = combine(
        repository.observeTasks(SortOrder.NEWEST_FIRST),
        repository.observeTasks(SortOrder.OLDEST_FIRST),
        sortOrder
    ) { newestTasks, oldestTasks, selectedSort ->
        val selectedTasks = when (selectedSort) {
            SortOrder.NEWEST_FIRST -> newestTasks
            SortOrder.OLDEST_FIRST -> oldestTasks
        }
        selectedSort to selectedTasks
    }

    val uiState: StateFlow<TaskUiState> = combine(
        sortedTasks,
        addDialogState,
        editDialogState,
        deleteDialogState
    ) { sortAndTasks, addState, editState, deleteState ->
        val (selectedSort, selectedTasks) = sortAndTasks
        TaskUiState(
            tasks = selectedTasks.map { it.toUiModel() },
            sortOrder = selectedSort,
            addDialog = addState,
            editDialog = editState,
            deleteDialog = deleteState
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = TaskUiState()
    )

    fun onAddClicked() { addDialogState.value = AddTaskDialogState(isVisible = true) }

    fun onAddTitleChanged(value: String) {
        addDialogState.value = addDialogState.value.copy(title = value, titleError = null)
    }

    fun onAddDismissed() { addDialogState.value = AddTaskDialogState() }

    fun onAddSubmitted() {
        val normalizedTitle = TaskTitleValidator.normalizeOrNull(addDialogState.value.title)
        if (normalizedTitle == null) {
            addDialogState.value = addDialogState.value.copy(titleError = TaskTitleValidator.REQUIRED_ERROR)
            return
        }
        viewModelScope.launch {
            val now = nowProvider()
            repository.addTask(title = normalizedTitle, createdAt = now, updatedAt = now)
            addDialogState.value = AddTaskDialogState()
        }
    }

    fun onEditClicked(task: TaskUiModel) {
        editDialogState.value = EditTaskDialogState(
            isVisible = true,
            taskId = task.id,
            title = task.title,
            originalTitle = task.title
        )
    }

    fun onEditTitleChanged(value: String) {
        editDialogState.value = editDialogState.value.copy(title = value, titleError = null)
    }

    fun onEditDismissed() { editDialogState.value = EditTaskDialogState() }

    fun onEditSubmitted() {
        val current = editDialogState.value
        val normalizedTitle = TaskTitleValidator.normalizeOrNull(current.title)
        if (normalizedTitle == null) {
            editDialogState.value = current.copy(titleError = TaskTitleValidator.REQUIRED_ERROR)
            return
        }
        val taskId = current.taskId ?: return
        viewModelScope.launch {
            repository.updateTask(id = taskId, title = normalizedTitle, updatedAt = nowProvider())
            editDialogState.value = EditTaskDialogState()
        }
    }

    fun onSortOrderSelected(order: SortOrder) {
        sortOrder.value = order
    }

    fun onDeleteClicked(task: TaskUiModel) {
        deleteDialogState.value = DeleteTaskDialogState(
            isVisible = true,
            taskId = task.id,
            title = task.title
        )
    }

    fun onDeleteDismissed() { deleteDialogState.value = DeleteTaskDialogState() }

    fun onDeleteConfirmed() {
        val taskId = deleteDialogState.value.taskId ?: return
        viewModelScope.launch {
            repository.deleteTask(taskId)
            deleteDialogState.value = DeleteTaskDialogState()
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val repository: TaskRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TaskViewModel::class.java)) return TaskViewModel(repository) as T
            throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}

data class TaskUiState(
    val tasks: List<TaskUiModel> = emptyList(),
    val sortOrder: SortOrder = SortOrder.NEWEST_FIRST,
    val addDialog: AddTaskDialogState = AddTaskDialogState(),
    val editDialog: EditTaskDialogState = EditTaskDialogState(),
    val deleteDialog: DeleteTaskDialogState = DeleteTaskDialogState()
)

data class AddTaskDialogState(
    val isVisible: Boolean = false,
    val title: String = "",
    val titleError: String? = null
)

data class EditTaskDialogState(
    val isVisible: Boolean = false,
    val taskId: Long? = null,
    val title: String = "",
    val originalTitle: String = "",
    val titleError: String? = null
)

data class DeleteTaskDialogState(
    val isVisible: Boolean = false,
    val taskId: Long? = null,
    val title: String = ""
)

data class TaskUiModel(
    val id: Long,
    val title: String,
    val createdAt: Long,
    val createdAtLabel: String
)

private fun Task.toUiModel(): TaskUiModel = TaskUiModel(
    id = id,
    title = title,
    createdAt = createdAt,
    createdAtLabel = formatCreatedAt(createdAt)
)

private fun formatCreatedAt(createdAt: Long): String = "Created " + java.text.SimpleDateFormat(
    "MMM d, yyyy, h:mm a",
    java.util.Locale.getDefault()
).format(java.util.Date(createdAt))
