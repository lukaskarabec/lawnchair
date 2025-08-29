package cz.appkazdarma.aiasistent.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.appkazdarma.aiasistent.domain.model.NoteItem
import cz.appkazdarma.aiasistent.domain.use_case.add_note.AddNoteUseCase
import cz.appkazdarma.aiasistent.domain.use_case.delete_note.DeleteNoteUseCase
import cz.appkazdarma.aiasistent.domain.use_case.get_notes.GetNotesUseCase
import cz.appkazdarma.aiasistent.domain.use_case.update_note.UpdateNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val getNotes: GetNotesUseCase,
    private val addNote: AddNoteUseCase,
    private val updateNote: UpdateNoteUseCase,
    private val deleteNote: DeleteNoteUseCase
) : ViewModel() {

    private val _notes = mutableStateOf<List<NoteItem>>(emptyList())
    val notes: State<List<NoteItem>> = _notes

    init {
        refreshNotes()
    }

    fun refreshNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            val items = getNotes()
            withContext(Dispatchers.Main) { _notes.value = items }
        }
    }

    fun addNewNote(title: String?, content: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val now = System.currentTimeMillis()
            val note = NoteItem(
                id = UUID.randomUUID().toString(),
                title = title,
                content = content,
                color = "#FFFFFF",
                labels = emptyList(),
                createdAt = now,
                updatedAt = now,
                pinned = false
            )
            addNote(note)
            refreshNotes()
        }
    }

    fun updateNote(item: NoteItem) {
        viewModelScope.launch(Dispatchers.IO) {
            updateNote(item.copy(updatedAt = System.currentTimeMillis()))
            refreshNotes()
        }
    }

    fun deleteNote(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteNote(id)
            refreshNotes()
        }
    }
}
