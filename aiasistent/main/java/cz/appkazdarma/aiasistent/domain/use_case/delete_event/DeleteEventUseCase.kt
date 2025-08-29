package cz.appkazdarma.aiasistent.domain.use_case.delete_event

import cz.appkazdarma.aiasistent.domain.repository.EventRepository
import javax.inject.Inject

class DeleteEventUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend operator fun invoke(id: String) {
        repository.deleteEvent(id)
    }
}
