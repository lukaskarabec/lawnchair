package cz.appkazdarma.aiasistent.data.local.browser

import androidx.room.Database
import androidx.room.RoomDatabase
import cz.appkazdarma.aiasistent.data.local.browser.entity.BookmarkEntity
import cz.appkazdarma.aiasistent.data.local.browser.entity.BrowserHistoryEntity

@Database(
    entities = [BrowserHistoryEntity::class, BookmarkEntity::class],
    version = 1,
    exportSchema = false
)
abstract class BrowserDatabase : RoomDatabase() {
    abstract val browserDao: BrowserDao
}
