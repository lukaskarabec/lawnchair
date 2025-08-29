package cz.appkazdarma.aiasistent.domain.repository

import cz.appkazdarma.aiasistent.domain.model.TaskItem

interface TaskRepository {
    suspend fun addOrUpdateTask(item: TaskItem)
    suspend fun getTasks(): List<TaskItem>
    suspend fun deleteTask(id: String)
}
