package cz.appkazdarma.aiasistent.domain.model

/**
 * Calendar event stored offline.
 */
data class EventItem(
    val id: String,
    val title: String,
    val description: String,
    val date: Long,
    val time: String?,
    val createdAt: Long
)
