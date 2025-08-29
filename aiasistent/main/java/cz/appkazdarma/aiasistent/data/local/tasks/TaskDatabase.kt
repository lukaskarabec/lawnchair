package cz.appkazdarma.aiasistent.data.local.tasks

import androidx.room.Database
import androidx.room.RoomDatabase
import cz.appkazdarma.aiasistent.data.local.tasks.entity.TaskEntity

@Database(entities = [TaskEntity::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {
    abstract val taskDao: TaskDao
}
