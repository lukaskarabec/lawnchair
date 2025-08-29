package cz.appkazdarma.aiasistent.data.local.notes

import androidx.room.Database
import androidx.room.RoomDatabase
import cz.appkazdarma.aiasistent.data.local.notes.entity.NoteEntity

@Database(entities = [NoteEntity::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {
    abstract val noteDao: NoteDao
}
