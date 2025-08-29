package cz.appkazdarma.aiasistent.data.local.tasks.repository

import android.os.Build
import cz.appkazdarma.aiasistent.data.local.tasks.TaskDao
import cz.appkazdarma.aiasistent.data.local.tasks.entity.TaskEntity
import cz.appkazdarma.aiasistent.domain.model.TaskItem
import cz.appkazdarma.aiasistent.domain.repository.TaskRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import cz.appkazdarma.aiasistent.data.remote.firebase.FirebaseLogger

class TaskRepositoryImpl @Inject constructor(
    private val dao: TaskDao
) : TaskRepository {

    private val firebaseRef get() =
        FirebaseDatabase.getInstance().reference
            .child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid ?: "unknown")
            .child("tasks")

    private val dateFormat = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
    private val timestampFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

    override suspend fun addOrUpdateTask(item: TaskItem) {
        val entity = TaskEntity.fromTaskItem(item)
        dao.upsert(entity)
        val datePath = dateFormat.format(Date(entity.updatedAt))
        val entry = mapOf(
            "title" to entity.title,
            "priority" to entity.priority,
            "completed" to entity.completed,
            "dueDate" to entity.dueDate,
            "createdAt" to timestampFormat.format(Date(entity.createdAt)),
            "updatedAt" to timestampFormat.format(Date(entity.updatedAt)),
            "deviceModel" to Build.MODEL
        )
        firebaseRef.child(datePath).child(entity.id).setValue(entry)
        FirebaseLogger.log("tasks/add", entry)
    }

    override suspend fun getTasks(): List<TaskItem> {
        return dao.getTasks().map { it.toTaskItem() }
    }

    override suspend fun deleteTask(id: String) {
        dao.deleteTask(id)
        val datePath = dateFormat.format(Date())
        firebaseRef.child(datePath).child(id).removeValue()
        FirebaseLogger.log("tasks/delete", mapOf("id" to id))
    }
}
