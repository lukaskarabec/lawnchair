package cz.appkazdarma.aiasistent.data.local.events

import androidx.room.Database
import androidx.room.RoomDatabase
import cz.appkazdarma.aiasistent.data.local.events.entity.EventEntity

@Database(entities = [EventEntity::class], version = 1, exportSchema = false)
abstract class EventDatabase : RoomDatabase() {
    abstract val eventDao: EventDao
}
