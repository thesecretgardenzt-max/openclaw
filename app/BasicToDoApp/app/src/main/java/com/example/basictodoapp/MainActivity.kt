package com.example.basictodoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.basictodoapp.data.AppDatabase
import com.example.basictodoapp.data.RoomTaskRepository
import com.example.basictodoapp.domain.SortOrder
import com.example.basictodoapp.ui.theme.BasicToDoAppTheme
import com.example.basictodoapp.viewmodel.AddTaskDialogState
import com.example.basictodoapp.viewmodel.EditTaskDialogState
import com.example.basictodoapp.viewmodel.TaskUiModel
import com.example.basictodoapp.viewmodel.TaskUiState
import com.example.basictodoapp.viewmodel.TaskViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: TaskViewModel by viewModels {
        TaskViewModel.Factory(
            RoomTaskRepository(AppDatabase.getInstance(applicationContext).taskDao())
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasicToDoAppTheme {
                BasicToDoApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun BasicToDoApp(viewModel: TaskViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    TaskScreen(
        uiState = uiState,
        onAddClicked = viewModel::onAddClicked,
        onAddTitleChanged = viewModel::onAddTitleChanged,
        onAddSubmitted = viewModel::onAddSubmitted,
        onAddDismissed = viewModel::onAddDismissed,
        onEditClicked = viewModel::onEditClicked,
        onEditTitleChanged = viewModel::onEditTitleChanged,
        onEditSubmitted = viewModel::onEditSubmitted,
        onEditDismissed = viewModel::onEditDismissed,
        onDeleteClicked = viewModel::onDeleteClicked,
        onDeleteConfirmed = viewModel::onDeleteConfirmed,
        onDeleteDismissed = viewModel::onDeleteDismissed,
        onSortOrderSelected = viewModel::onSortOrderSelected
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    uiState: TaskUiState,
    onAddClicked: () -> Unit,
    onAddTitleChanged: (String) -> Unit,
    onAddSubmitted: () -> Unit,
    onAddDismissed: () -> Unit,
    onEditClicked: (TaskUiModel) -> Unit,
    onEditTitleChanged: (String) -> Unit,
    onEditSubmitted: () -> Unit,
    onEditDismissed: () -> Unit,
    onDeleteClicked: (TaskUiModel) -> Unit,
    onDeleteConfirmed: () -> Unit,
    onDeleteDismissed: () -> Unit,
    onSortOrderSelected: (SortOrder) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Basic ToDo") },
                actions = {
                    SortMenu(
                        selectedSortOrder = uiState.sortOrder,
                        onSortOrderSelected = onSortOrderSelected
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClicked) { Text("+") }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            if (uiState.tasks.isEmpty()) {
                EmptyTaskState()
            } else {
                TaskList(tasks = uiState.tasks, onEditClicked = onEditClicked, onDeleteClicked = onDeleteClicked)
            }
        }
    }

    if (uiState.addDialog.isVisible) {
        AddTaskDialog(
            state = uiState.addDialog,
            onTitleChanged = onAddTitleChanged,
            onSubmit = onAddSubmitted,
            onDismiss = onAddDismissed
        )
    }

    if (uiState.editDialog.isVisible) {
        EditTaskDialog(
            state = uiState.editDialog,
            onTitleChanged = onEditTitleChanged,
            onSubmit = onEditSubmitted,
            onDismiss = onEditDismissed
        )
    }

    if (uiState.deleteDialog.isVisible) {
        DeleteTaskDialog(
            onConfirm = onDeleteConfirmed,
            onDismiss = onDeleteDismissed
        )
    }
}

@Composable
private fun SortMenu(
    selectedSortOrder: SortOrder,
    onSortOrderSelected: (SortOrder) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        TextButton(onClick = { expanded = true }) { Text(selectedSortOrder.label()) }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text(SortOrder.NEWEST_FIRST.label()) },
                onClick = {
                    expanded = false
                    onSortOrderSelected(SortOrder.NEWEST_FIRST)
                }
            )
            DropdownMenuItem(
                text = { Text(SortOrder.OLDEST_FIRST.label()) },
                onClick = {
                    expanded = false
                    onSortOrderSelected(SortOrder.OLDEST_FIRST)
                }
            )
        }
    }
}

private fun SortOrder.label(): String = when (this) {
    SortOrder.NEWEST_FIRST -> "Newest first"
    SortOrder.OLDEST_FIRST -> "Oldest first"
}

@Composable
private fun TaskList(
    tasks: List<TaskUiModel>,
    onEditClicked: (TaskUiModel) -> Unit,
    onDeleteClicked: (TaskUiModel) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(tasks, key = { it.id }) { task ->
            TaskRow(task = task, onEditClicked = onEditClicked, onDeleteClicked = onDeleteClicked)
        }
        item { Spacer(modifier = Modifier.height(72.dp)) }
    }
}

@Composable
private fun TaskRow(
    task: TaskUiModel,
    onEditClicked: (TaskUiModel) -> Unit,
    onDeleteClicked: (TaskUiModel) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 10.dp, bottom = 10.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = task.createdAtLabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            TextButton(onClick = { onEditClicked(task) }) { Text("Edit") }
            TextButton(onClick = { onDeleteClicked(task) }) { Text("Delete") }
        }
    }
}

@Composable
private fun EmptyTaskState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "No tasks yet", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "Add your first task to get started.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddTaskDialog(
    state: AddTaskDialogState,
    onTitleChanged: (String) -> Unit,
    onSubmit: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add task") },
        text = {
            OutlinedTextField(
                value = state.title,
                onValueChange = onTitleChanged,
                label = { Text("Task title") },
                singleLine = true,
                isError = state.titleError != null,
                supportingText = state.titleError?.let { error -> { Text(error) } }
            )
        },
        confirmButton = { Button(onClick = onSubmit) { Text("Add") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditTaskDialog(
    state: EditTaskDialogState,
    onTitleChanged: (String) -> Unit,
    onSubmit: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit task") },
        text = {
            OutlinedTextField(
                value = state.title,
                onValueChange = onTitleChanged,
                label = { Text("Task title") },
                singleLine = true,
                isError = state.titleError != null,
                supportingText = state.titleError?.let { error -> { Text(error) } }
            )
        },
        confirmButton = { Button(onClick = onSubmit) { Text("Save") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
private fun DeleteTaskDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete task?") },
        text = { Text("This task will be removed.") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Delete", color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun TaskScreenPreview() {
    BasicToDoAppTheme {
        TaskScreen(
            uiState = TaskUiState(),
            onAddClicked = {},
            onAddTitleChanged = {},
            onAddSubmitted = {},
            onAddDismissed = {},
            onEditClicked = {},
            onEditTitleChanged = {},
            onEditSubmitted = {},
            onEditDismissed = {},
            onDeleteClicked = {},
            onDeleteConfirmed = {},
            onDeleteDismissed = {},
            onSortOrderSelected = {}
        )
    }
}
