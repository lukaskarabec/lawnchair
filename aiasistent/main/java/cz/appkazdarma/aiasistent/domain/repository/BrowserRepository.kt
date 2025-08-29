package cz.appkazdarma.aiasistent.domain.repository

import cz.appkazdarma.aiasistent.data.local.browser.entity.BookmarkEntity
import cz.appkazdarma.aiasistent.data.local.browser.entity.BrowserHistoryEntity
import cz.appkazdarma.aiasistent.domain.model.BookmarkItem
import cz.appkazdarma.aiasistent.domain.model.BrowserHistoryItem

interface BrowserRepository {
    suspend fun addHistory(entity: BrowserHistoryEntity)
    suspend fun getHistory(): List<BrowserHistoryItem>
    suspend fun addBookmark(entity: BookmarkEntity)
    suspend fun getBookmarks(): List<BookmarkItem>
    suspend fun deleteBookmark(id: Long)
    suspend fun clearHistory()
}
