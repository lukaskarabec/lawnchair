package cz.appkazdarma.aiasistent.data.local.notes.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.appkazdarma.aiasistent.domain.model.NoteItem

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey val id: String,
    val title: String?,
    val content: String,
    val color: String,
    val labels: String,
    val createdAt: Long,
    val updatedAt: Long,
    val pinned: Boolean
) {
    fun toNoteItem() = NoteItem(
        id = id,
        title = title,
        content = content,
        color = color,
        labels = if (labels.isEmpty()) emptyList() else labels.split("|"),
        createdAt = createdAt,
        updatedAt = updatedAt,
        pinned = pinned
    )

    companion object {
        fun fromNoteItem(item: NoteItem) = NoteEntity(
            id = item.id,
            title = item.title,
            content = item.content,
            color = item.color,
            labels = item.labels.joinToString("|"),
            createdAt = item.createdAt,
            updatedAt = item.updatedAt,
            pinned = item.pinned
        )
    }
}
