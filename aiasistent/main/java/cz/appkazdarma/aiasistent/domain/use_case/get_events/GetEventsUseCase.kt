package cz.appkazdarma.aiasistent.domain.use_case.get_events

import cz.appkazdarma.aiasistent.domain.repository.EventRepository
import javax.inject.Inject

class GetEventsUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend operator fun invoke() = repository.getEvents()
}
