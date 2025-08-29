package cz.appkazdarma.aiasistent.data.local.browser.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.appkazdarma.aiasistent.domain.model.BrowserHistoryItem

@Entity(tableName = "browser_history")
data class BrowserHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val url: String,
    val title: String?,
    val timestamp: Long = System.currentTimeMillis()
) {
    fun toBrowserHistoryItem() = BrowserHistoryItem(
        id = id,
        url = url,
        title = title,
        timestamp = timestamp
    )
}
