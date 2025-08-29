package cz.appkazdarma.aiasistent.data.local.events

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cz.appkazdarma.aiasistent.data.local.events.entity.EventEntity

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(event: EventEntity)

    @Query("SELECT * FROM events ORDER BY date ASC")
    suspend fun getEvents(): List<EventEntity>

    @Query("DELETE FROM events WHERE id = :id")
    suspend fun deleteEvent(id: String)
}
