package cz.appkazdarma.aiasistent.domain.use_case.add_event

import cz.appkazdarma.aiasistent.domain.model.EventItem
import cz.appkazdarma.aiasistent.domain.repository.EventRepository
import javax.inject.Inject

class AddEventUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend operator fun invoke(item: EventItem) {
        repository.addOrUpdateEvent(item)
    }
}
