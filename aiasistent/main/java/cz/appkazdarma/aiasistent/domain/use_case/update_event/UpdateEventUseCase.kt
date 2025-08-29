package cz.appkazdarma.aiasistent.domain.use_case.update_event

import cz.appkazdarma.aiasistent.domain.model.EventItem
import cz.appkazdarma.aiasistent.domain.repository.EventRepository
import javax.inject.Inject

class UpdateEventUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend operator fun invoke(item: EventItem) {
        repository.addOrUpdateEvent(item)
    }
}
