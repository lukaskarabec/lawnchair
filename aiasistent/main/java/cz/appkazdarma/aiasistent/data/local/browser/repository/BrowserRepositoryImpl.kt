package cz.appkazdarma.aiasistent.data.local.browser.repository

import android.os.Build
import cz.appkazdarma.aiasistent.data.local.browser.BrowserDao
import cz.appkazdarma.aiasistent.data.local.browser.entity.BookmarkEntity
import cz.appkazdarma.aiasistent.data.local.browser.entity.BrowserHistoryEntity
import cz.appkazdarma.aiasistent.domain.model.BookmarkItem
import cz.appkazdarma.aiasistent.domain.model.BrowserHistoryItem
import cz.appkazdarma.aiasistent.domain.repository.BrowserRepository
import cz.appkazdarma.aiasistent.data.remote.firebase.FirebaseLogger
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class BrowserRepositoryImpl @Inject constructor(
    private val dao: BrowserDao
) : BrowserRepository {
    private val dateFormat = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
    private val timestampFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

    override suspend fun addHistory(entity: BrowserHistoryEntity) {
        dao.insertHistory(entity)
        val datePath = dateFormat.format(Date(entity.timestamp))
        val entry = mapOf(
            "url" to entity.url,
            "title" to entity.title,
            "timestamp" to timestampFormat.format(Date(entity.timestamp)),
            "deviceModel" to Build.MODEL
        )
        FirebaseLogger.log("browser/history", entry)
    }

    override suspend fun getHistory(): List<BrowserHistoryItem> {
        return dao.getHistory().map { it.toBrowserHistoryItem() }
    }

    override suspend fun addBookmark(entity: BookmarkEntity) {
        dao.insertBookmark(entity)
        val datePath = dateFormat.format(Date(entity.timestamp))
        val entry = mapOf(
            "url" to entity.url,
            "title" to entity.title,
            "timestamp" to timestampFormat.format(Date(entity.timestamp)),
            "deviceModel" to Build.MODEL
        )
        FirebaseLogger.log("browser/bookmarks", entry)
    }

    override suspend fun getBookmarks(): List<BookmarkItem> {
        return dao.getBookmarks().map { it.toBookmarkItem() }
    }

    override suspend fun deleteBookmark(id: Long) {
        dao.deleteBookmark(id)
    }

    override suspend fun clearHistory() {
        dao.clearHistory()
    }
}
