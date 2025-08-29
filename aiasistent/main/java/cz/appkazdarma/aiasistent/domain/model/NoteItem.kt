package cz.appkazdarma.aiasistent.domain.model

/**
 * Data class representing a user note.
 */
data class NoteItem(
    val id: String,
    val title: String?,
    val content: String,
    val color: String,
    val labels: List<String>,
    val createdAt: Long,
    val updatedAt: Long,
    val pinned: Boolean
)
