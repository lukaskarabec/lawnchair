package cz.appkazdarma.aiasistent.data.local.browser

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cz.appkazdarma.aiasistent.data.local.browser.entity.BookmarkEntity
import cz.appkazdarma.aiasistent.data.local.browser.entity.BrowserHistoryEntity

@Dao
interface BrowserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: BrowserHistoryEntity)

    @Query("SELECT * FROM browser_history ORDER BY timestamp DESC")
    suspend fun getHistory(): List<BrowserHistoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity)

    @Query("SELECT * FROM bookmarks ORDER BY timestamp DESC")
    suspend fun getBookmarks(): List<BookmarkEntity>

    @Query("DELETE FROM bookmarks WHERE id = :id")
    suspend fun deleteBookmark(id: Long)

    @Query("DELETE FROM browser_history")
    suspend fun clearHistory()
}
