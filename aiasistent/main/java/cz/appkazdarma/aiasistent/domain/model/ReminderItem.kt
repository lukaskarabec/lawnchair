package cz.appkazdarma.aiasistent.domain.model

/**
 * Data class representing a reminder or simple task.
 */
data class ReminderItem(
    val id: String,
    val text: String,
    val time: Long,
    val repeat: RepeatType,
    val label: String?,
    val createdAt: Long,
    val done: Boolean
)

enum class RepeatType { NONE, DAILY, WEEKLY }
