package cz.appkazdarma.aiasistent.presentation.tasks.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cz.appkazdarma.aiasistent.R
import cz.appkazdarma.aiasistent.domain.model.TaskItem
import cz.appkazdarma.aiasistent.presentation.tasks.TasksViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(viewModel: TasksViewModel = hiltViewModel()) {
    val tasks by viewModel.tasks.observeAsState(emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var editTask by remember { mutableStateOf<TaskItem?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.AddTask, contentDescription = "Add")
            }
        }
    ) { padding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(tasks) { task ->
                TaskRow(task = task, onEdit = {
                    editTask = task
                    showDialog = true
                }, onDelete = {
                    viewModel.deleteTask(task.id)
                })
            }
        }
    }

    if (showDialog) {
        val isEditing = editTask != null
        var title by remember { mutableStateOf(TextFieldValue(editTask?.title ?: "")) }
        var priority by remember { mutableStateOf(TextFieldValue(editTask?.priority?.toString() ?: "0")) }
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                editTask = null
            },
            confirmButton = {
                TextButton(onClick = {
                    val prio = priority.text.toIntOrNull() ?: 0
                    if (isEditing) {
                        viewModel.updateTask(
                            editTask!!.copy(
                                title = title.text,
                                priority = prio
                            )
                        )
                    } else {
                        viewModel.addNewTask(
                            title.text,
                            prio,
                            null
                        )
                    }
                    showDialog = false
                    editTask = null
                }) {
                    Text(stringResource(id = R.string.save))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                    editTask = null
                }) { Text(stringResource(id = R.string.cancel)) }
            },
            title = { Text(if (isEditing) stringResource(R.string.edit_task) else stringResource(R.string.new_task)) },
            text = {
                Column {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(R.string.title)) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = priority,
                        onValueChange = { priority = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(R.string.priority)) }
                    )
                }
            }
        )
    }
}

@Composable
private fun TaskRow(task: TaskItem, onEdit: () -> Unit, onDelete: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = task.title, style = MaterialTheme.typography.bodyLarge)
            Text(text = "${task.priority}", style = MaterialTheme.typography.bodyMedium)
        }
        IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, contentDescription = "Edit") }
        IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = "Delete") }
    }
}
