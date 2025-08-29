package cz.appkazdarma.aiasistent.domain.model

data class BookmarkItem(
    val id: Long,
    val url: String,
    val title: String?,
    val timestamp: Long
)
