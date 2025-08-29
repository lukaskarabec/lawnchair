package cz.appkazdarma.aiasistent.data.local.tasks.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.appkazdarma.aiasistent.domain.model.TaskItem

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String,
    val title: String,
    val priority: Int,
    val completed: Boolean,
    val dueDate: Long?,
    val createdAt: Long,
    val updatedAt: Long
) {
    fun toTaskItem() = TaskItem(
        id = id,
        title = title,
        priority = priority,
        completed = completed,
        dueDate = dueDate,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    companion object {
        fun fromTaskItem(item: TaskItem) = TaskEntity(
            id = item.id,
            title = item.title,
            priority = item.priority,
            completed = item.completed,
            dueDate = item.dueDate,
            createdAt = item.createdAt,
            updatedAt = item.updatedAt
        )
    }
}
