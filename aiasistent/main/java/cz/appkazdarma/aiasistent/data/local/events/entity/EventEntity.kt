package cz.appkazdarma.aiasistent.data.local.events.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.appkazdarma.aiasistent.domain.model.EventItem

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val date: Long,
    val time: String?,
    val createdAt: Long
) {
    fun toEventItem() = EventItem(
        id = id,
        title = title,
        description = description,
        date = date,
        time = time,
        createdAt = createdAt
    )

    companion object {
        fun fromEventItem(item: EventItem) = EventEntity(
            id = item.id,
            title = item.title,
            description = item.description,
            date = item.date,
            time = item.time,
            createdAt = item.createdAt
        )
    }
}
