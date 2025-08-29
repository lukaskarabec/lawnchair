package cz.appkazdarma.aiasistent.domain.repository

import cz.appkazdarma.aiasistent.domain.model.EventItem

interface EventRepository {
    suspend fun addOrUpdateEvent(item: EventItem)
    suspend fun getEvents(): List<EventItem>
    suspend fun deleteEvent(id: String)
}
