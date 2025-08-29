package cz.appkazdarma.aiasistent.domain.use_case.delete_note

import cz.appkazdarma.aiasistent.domain.repository.NoteRepository
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: String) {
        repository.deleteNote(id)
    }
}
