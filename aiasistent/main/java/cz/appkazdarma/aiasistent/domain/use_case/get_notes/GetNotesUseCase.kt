package cz.appkazdarma.aiasistent.domain.use_case.get_notes

import cz.appkazdarma.aiasistent.domain.model.NoteItem
import cz.appkazdarma.aiasistent.domain.repository.NoteRepository
import javax.inject.Inject

class GetNotesUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(): List<NoteItem> {
        return repository.getNotes()
    }
}
