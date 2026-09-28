package com.example.basictodoapp.viewmodel

import com.example.basictodoapp.data.TaskRepository
import com.example.basictodoapp.domain.SortOrder
import com.example.basictodoapp.domain.Task
import com.example.basictodoapp.domain.TaskTitleValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class TaskViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: FakeTaskRepository

    @Before
    fun setUp() {
        repository = FakeTaskRepository()
    }

    @Test
    fun invalidAddShowsValidationErrorAndDoesNotCallRepository() = runTest {
        val viewModel = TaskViewModel(repository, nowProvider = { 1000L })
        advanceUntilIdle()

        viewModel.onAddClicked()
        viewModel.onAddTitleChanged("   ")
        viewModel.onAddSubmitted()
        advanceUntilIdle()

        assertEquals(TaskTitleValidator.REQUIRED_ERROR, viewModel.uiState.value.addDialog.titleError)
        assertTrue(viewModel.uiState.value.addDialog.isVisible)
        assertEquals(emptyList<FakeTaskRepository.AddCall>(), repository.addCalls)
    }

    @Test
    fun validAddTrimsTitlePersistsTaskAndClosesDialog() = runTest {
        val viewModel = TaskViewModel(repository, nowProvider = { 1234L })
        advanceUntilIdle()

        viewModel.onAddClicked()
        viewModel.onAddTitleChanged("  Buy milk  ")
        viewModel.onAddSubmitted()
        advanceUntilIdle()

        assertEquals(listOf(FakeTaskRepository.AddCall("Buy milk", 1234L, 1234L)), repository.addCalls)
        assertFalse(viewModel.uiState.value.addDialog.isVisible)
        assertEquals("Buy milk", viewModel.uiState.value.tasks.single().title)
    }

    @Test
    fun editClickedPrefillsCurrentTitle() = runTest {
        repository.seed(Task(id = 7L, title = "Original", createdAt = 100L, updatedAt = 100L))
        val viewModel = TaskViewModel(repository, nowProvider = { 200L })
        advanceUntilIdle()

        viewModel.onEditClicked(viewModel.uiState.value.tasks.single())
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.editDialog.isVisible)
        assertEquals(7L, viewModel.uiState.value.editDialog.taskId)
        assertEquals("Original", viewModel.uiState.value.editDialog.title)
    }

    @Test
    fun invalidEditShowsValidationErrorAndDoesNotUpdateRepository() = runTest {
        repository.seed(Task(id = 7L, title = "Original", createdAt = 100L, updatedAt = 100L))
        val viewModel = TaskViewModel(repository, nowProvider = { 200L })
        advanceUntilIdle()

        viewModel.onEditClicked(viewModel.uiState.value.tasks.single())
        viewModel.onEditTitleChanged("   ")
        viewModel.onEditSubmitted()
        advanceUntilIdle()

        assertEquals(TaskTitleValidator.REQUIRED_ERROR, viewModel.uiState.value.editDialog.titleError)
        assertTrue(viewModel.uiState.value.editDialog.isVisible)
        assertEquals(emptyList<FakeTaskRepository.UpdateCall>(), repository.updateCalls)
        assertEquals("Original", viewModel.uiState.value.tasks.single().title)
    }

    @Test
    fun validEditTrimsTitleUpdatesExistingTaskAndClosesDialog() = runTest {
        repository.seed(Task(id = 7L, title = "Original", createdAt = 100L, updatedAt = 100L))
        val viewModel = TaskViewModel(repository, nowProvider = { 250L })
        advanceUntilIdle()

        viewModel.onEditClicked(viewModel.uiState.value.tasks.single())
        viewModel.onEditTitleChanged("  Updated title  ")
        viewModel.onEditSubmitted()
        advanceUntilIdle()

        assertEquals(listOf(FakeTaskRepository.UpdateCall(7L, "Updated title", 250L)), repository.updateCalls)
        assertFalse(viewModel.uiState.value.editDialog.isVisible)
        assertEquals("Updated title", viewModel.uiState.value.tasks.single().title)
    }

    @Test
    fun editDismissLeavesTaskUnchanged() = runTest {
        repository.seed(Task(id = 7L, title = "Original", createdAt = 100L, updatedAt = 100L))
        val viewModel = TaskViewModel(repository, nowProvider = { 250L })
        advanceUntilIdle()

        viewModel.onEditClicked(viewModel.uiState.value.tasks.single())
        viewModel.onEditTitleChanged("Changed but cancelled")
        viewModel.onEditDismissed()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.editDialog.isVisible)
        assertEquals(emptyList<FakeTaskRepository.UpdateCall>(), repository.updateCalls)
        assertEquals("Original", viewModel.uiState.value.tasks.single().title)
    }

    @Test
    fun deleteClickedShowsConfirmationDialog() = runTest {
        repository.seed(Task(id = 7L, title = "Task to delete", createdAt = 100L, updatedAt = 100L))
        val viewModel = TaskViewModel(repository, nowProvider = { 300L })
        advanceUntilIdle()

        viewModel.onDeleteClicked(viewModel.uiState.value.tasks.single())
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.deleteDialog.isVisible)
        assertEquals(7L, viewModel.uiState.value.deleteDialog.taskId)
        assertEquals("Task to delete", viewModel.uiState.value.deleteDialog.title)
    }

    @Test
    fun deleteDismissLeavesTaskUnchanged() = runTest {
        repository.seed(Task(id = 7L, title = "Keep me", createdAt = 100L, updatedAt = 100L))
        val viewModel = TaskViewModel(repository, nowProvider = { 300L })
        advanceUntilIdle()

        viewModel.onDeleteClicked(viewModel.uiState.value.tasks.single())
        viewModel.onDeleteDismissed()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.deleteDialog.isVisible)
        assertEquals(emptyList<Long>(), repository.deleteCalls)
        assertEquals("Keep me", viewModel.uiState.value.tasks.single().title)
    }

    @Test
    fun deleteConfirmedDeletesSelectedTaskAndClosesDialog() = runTest {
        repository.seed(
            Task(id = 7L, title = "Delete me", createdAt = 100L, updatedAt = 100L),
            Task(id = 8L, title = "Keep me", createdAt = 110L, updatedAt = 110L)
        )
        val viewModel = TaskViewModel(repository, nowProvider = { 300L })
        advanceUntilIdle()

        val taskToDelete = viewModel.uiState.value.tasks.first { it.id == 7L }
        viewModel.onDeleteClicked(taskToDelete)
        viewModel.onDeleteConfirmed()
        advanceUntilIdle()

        assertEquals(listOf(7L), repository.deleteCalls)
        assertFalse(viewModel.uiState.value.deleteDialog.isVisible)
        assertEquals(listOf("Keep me"), viewModel.uiState.value.tasks.map { it.title })
    }

    @Test
    fun deletingLastTaskShowsEmptyStateData() = runTest {
        repository.seed(Task(id = 7L, title = "Only task", createdAt = 100L, updatedAt = 100L))
        val viewModel = TaskViewModel(repository, nowProvider = { 300L })
        advanceUntilIdle()

        viewModel.onDeleteClicked(viewModel.uiState.value.tasks.single())
        viewModel.onDeleteConfirmed()
        advanceUntilIdle()

        assertEquals(emptyList<TaskUiModel>(), viewModel.uiState.value.tasks)
    }


    @Test
    fun defaultSortShowsNewestFirstByCreatedAt() = runTest {
        repository.seed(
            Task(id = 1L, title = "Old", createdAt = 100L, updatedAt = 100L),
            Task(id = 2L, title = "New", createdAt = 200L, updatedAt = 200L)
        )
        val viewModel = TaskViewModel(repository, nowProvider = { 300L })
        advanceUntilIdle()

        assertEquals(SortOrder.NEWEST_FIRST, viewModel.uiState.value.sortOrder)
        assertEquals(listOf("New", "Old"), viewModel.uiState.value.tasks.map { it.title })
    }

    @Test
    fun selectingOldestFirstReordersTasksByCreatedAtAscending() = runTest {
        repository.seed(
            Task(id = 1L, title = "Old", createdAt = 100L, updatedAt = 100L),
            Task(id = 2L, title = "New", createdAt = 200L, updatedAt = 200L)
        )
        val viewModel = TaskViewModel(repository, nowProvider = { 300L })
        advanceUntilIdle()

        viewModel.onSortOrderSelected(SortOrder.OLDEST_FIRST)
        advanceUntilIdle()

        assertEquals(SortOrder.OLDEST_FIRST, viewModel.uiState.value.sortOrder)
        assertEquals(listOf("Old", "New"), viewModel.uiState.value.tasks.map { it.title })
    }

    @Test
    fun selectingNewestFirstReordersTasksByCreatedAtDescending() = runTest {
        repository.seed(
            Task(id = 1L, title = "Old", createdAt = 100L, updatedAt = 100L),
            Task(id = 2L, title = "New", createdAt = 200L, updatedAt = 200L)
        )
        val viewModel = TaskViewModel(repository, nowProvider = { 300L })
        advanceUntilIdle()

        viewModel.onSortOrderSelected(SortOrder.OLDEST_FIRST)
        advanceUntilIdle()
        viewModel.onSortOrderSelected(SortOrder.NEWEST_FIRST)
        advanceUntilIdle()

        assertEquals(SortOrder.NEWEST_FIRST, viewModel.uiState.value.sortOrder)
        assertEquals(listOf("New", "Old"), viewModel.uiState.value.tasks.map { it.title })
    }

}

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) { Dispatchers.setMain(testDispatcher) }
    override fun finished(description: Description) { Dispatchers.resetMain() }
}

private class FakeTaskRepository : TaskRepository {
    data class AddCall(val title: String, val createdAt: Long, val updatedAt: Long)
    data class UpdateCall(val id: Long, val title: String, val updatedAt: Long)

    val addCalls = mutableListOf<AddCall>()
    val updateCalls = mutableListOf<UpdateCall>()
    val deleteCalls = mutableListOf<Long>()
    private val tasks = MutableStateFlow<List<Task>>(emptyList())
    private var nextId = 1L

    fun seed(vararg task: Task) { tasks.value = task.toList() }

    override fun observeTasks(sortOrder: SortOrder): Flow<List<Task>> = tasks.map { currentTasks ->
        when (sortOrder) {
            SortOrder.NEWEST_FIRST -> currentTasks.sortedByDescending { it.createdAt }
            SortOrder.OLDEST_FIRST -> currentTasks.sortedBy { it.createdAt }
        }
    }

    override suspend fun addTask(title: String, createdAt: Long, updatedAt: Long) {
        addCalls += AddCall(title, createdAt, updatedAt)
        tasks.value = listOf(Task(id = nextId++, title = title, createdAt = createdAt, updatedAt = updatedAt)) + tasks.value
    }

    override suspend fun updateTask(id: Long, title: String, updatedAt: Long) {
        updateCalls += UpdateCall(id, title, updatedAt)
        tasks.value = tasks.value.map { task ->
            if (task.id == id) task.copy(title = title, updatedAt = updatedAt) else task
        }
    }

    override suspend fun deleteTask(id: Long) {
        deleteCalls += id
        tasks.value = tasks.value.filterNot { it.id == id }
    }
}
