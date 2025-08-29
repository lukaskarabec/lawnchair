package cz.appkazdarma.aiasistent.presentation.notes.components

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
import androidx.compose.material.icons.automirrored.filled.NoteAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cz.appkazdarma.aiasistent.domain.model.NoteItem
import cz.appkazdarma.aiasistent.presentation.notes.NotesViewModel
import androidx.compose.ui.res.stringResource
import cz.appkazdarma.aiasistent.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(viewModel: NotesViewModel = hiltViewModel()) {
    val notes by viewModel.notes
    var showDialog by remember { mutableStateOf(false) }
    var editNote by remember { mutableStateOf<NoteItem?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.AutoMirrored.Filled.NoteAdd, contentDescription = stringResource(R.string.add))
            }
        }
    ) { padding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(notes) { note ->
                NoteRow(note = note, onEdit = {
                    editNote = note
                    showDialog = true
                }, onDelete = {
                    viewModel.deleteNote(note.id)
                })
            }
        }
    }

    if (showDialog) {
        val isEditing = editNote != null
        var title by remember { mutableStateOf(TextFieldValue(editNote?.title ?: "")) }
        var content by remember { mutableStateOf(TextFieldValue(editNote?.content ?: "")) }
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                editNote = null
            },
            confirmButton = {
                TextButton(onClick = {
                    if (isEditing) {
                        viewModel.updateNote(
                            editNote!!.copy(
                                title = title.text,
                                content = content.text
                            )
                        )
                    } else {
                        viewModel.addNewNote(
                            title.text.takeIf { it.isNotBlank() },
                            content.text
                        )
                    }
                    showDialog = false
                    editNote = null
                }) {
                    Text(stringResource(id = R.string.save))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                    editNote = null
                }) { Text(stringResource(id = R.string.cancel)) }
            },
            title = { Text(if (isEditing) stringResource(R.string.edit_note) else stringResource(R.string.new_note)) },
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
                        value = content,
                        onValueChange = { content = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(R.string.content)) }
                    )
                }
            }
        )
    }
}

@Composable
private fun NoteRow(note: NoteItem, onEdit: () -> Unit, onDelete: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = note.title ?: stringResource(R.string.no_title), style = MaterialTheme.typography.bodyLarge)
            Text(text = note.content, style = MaterialTheme.typography.bodyMedium)
        }
        IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.edit)) }
        IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.delete)) }
    }
}
