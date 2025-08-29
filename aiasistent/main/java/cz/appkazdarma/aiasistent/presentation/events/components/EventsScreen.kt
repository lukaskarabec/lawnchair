package cz.appkazdarma.aiasistent.presentation.events.components

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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cz.appkazdarma.aiasistent.R
import cz.appkazdarma.aiasistent.domain.model.EventItem
import cz.appkazdarma.aiasistent.presentation.events.EventsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(viewModel: EventsViewModel = hiltViewModel()) {
    val events by viewModel.events
    var showDialog by remember { mutableStateOf(false) }
    var editEvent by remember { mutableStateOf<EventItem?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Event, contentDescription = "Add")
            }
        }
    ) { padding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(events) { event ->
                EventRow(event = event, onEdit = {
                    editEvent = event
                    showDialog = true
                }, onDelete = {
                    viewModel.deleteEvent(event.id)
                })
            }
        }
    }

    if (showDialog) {
        val isEditing = editEvent != null
        var title by remember { mutableStateOf(TextFieldValue(editEvent?.title ?: "")) }
        var description by remember { mutableStateOf(TextFieldValue(editEvent?.description ?: "")) }
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                editEvent = null
            },
            confirmButton = {
                TextButton(onClick = {
                    if (isEditing) {
                        viewModel.updateEvent(
                            editEvent!!.copy(
                                title = title.text,
                                description = description.text
                            )
                        )
                    } else {
                        viewModel.addNewEvent(
                            title.text,
                            description.text,
                            System.currentTimeMillis(),
                            null
                        )
                    }
                    showDialog = false
                    editEvent = null
                }) {
                    Text(stringResource(id = R.string.save))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                    editEvent = null
                }) { Text(stringResource(id = R.string.cancel)) }
            },
            title = { Text(if (isEditing) stringResource(R.string.edit_event) else stringResource(R.string.new_event)) },
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
                        value = description,
                        onValueChange = { description = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(R.string.description)) }
                    )
                }
            }
        )
    }
}

@Composable
private fun EventRow(event: EventItem, onEdit: () -> Unit, onDelete: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = event.title, style = MaterialTheme.typography.bodyLarge)
            Text(text = event.description, style = MaterialTheme.typography.bodyMedium)
        }
        IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, contentDescription = "Edit") }
        IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = "Delete") }
    }
}
