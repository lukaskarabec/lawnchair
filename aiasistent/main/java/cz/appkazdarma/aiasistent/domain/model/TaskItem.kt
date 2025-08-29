package cz.appkazdarma.aiasistent.domain.model

/**
 * Simple task item stored offline.
 */
data class TaskItem(
    val id: String,
    val title: String,
    val priority: Int,
    val completed: Boolean,
    val dueDate: Long?,
    val createdAt: Long,
    val updatedAt: Long
)
