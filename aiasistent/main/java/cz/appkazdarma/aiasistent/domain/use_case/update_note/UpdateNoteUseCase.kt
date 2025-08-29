package cz.appkazdarma.aiasistent.domain.use_case.update_note

import cz.appkazdarma.aiasistent.domain.model.NoteItem
import cz.appkazdarma.aiasistent.domain.repository.NoteRepository
import javax.inject.Inject

class UpdateNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(item: NoteItem) {
        repository.addOrUpdateNote(item)
    }
}
