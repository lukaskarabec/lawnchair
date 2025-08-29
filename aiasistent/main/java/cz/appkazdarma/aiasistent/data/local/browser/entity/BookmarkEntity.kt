package cz.appkazdarma.aiasistent.data.local.browser.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.appkazdarma.aiasistent.domain.model.BookmarkItem

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val url: String,
    val title: String?,
    val timestamp: Long = System.currentTimeMillis()
) {
    fun toBookmarkItem() = BookmarkItem(
        id = id,
        url = url,
        title = title,
        timestamp = timestamp
    )
}
