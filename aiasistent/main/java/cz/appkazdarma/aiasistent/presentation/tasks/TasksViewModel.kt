package cz.appkazdarma.aiasistent.presentation.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.appkazdarma.aiasistent.domain.model.TaskItem
import cz.appkazdarma.aiasistent.domain.use_case.add_task.AddTaskUseCase
import cz.appkazdarma.aiasistent.domain.use_case.update_task.UpdateTaskUseCase
import cz.appkazdarma.aiasistent.domain.use_case.delete_task.DeleteTaskUseCase
import cz.appkazdarma.aiasistent.domain.use_case.get_tasks.GetTasksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val getTasks: GetTasksUseCase,
    private val addTask: AddTaskUseCase,
    private val updateTask: UpdateTaskUseCase,
    private val deleteTask: DeleteTaskUseCase
) : ViewModel() {

    private val _tasks = MutableLiveData<List<TaskItem>>(emptyList())
    val tasks: LiveData<List<TaskItem>> = _tasks

    init {
        refreshTasks()
    }

    fun refreshTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            val items = getTasks()
            withContext(Dispatchers.Main) { _tasks.value = items }
        }
    }

    fun addNewTask(title: String, priority: Int, dueDate: Long?) {
        viewModelScope.launch(Dispatchers.IO) {
            val now = System.currentTimeMillis()
            val item = TaskItem(
                id = UUID.randomUUID().toString(),
                title = title,
                priority = priority,
                completed = false,
                dueDate = dueDate,
                createdAt = now,
                updatedAt = now
            )
            addTask(item)
            refreshTasks()
        }
    }

    fun updateTask(item: TaskItem) {
        viewModelScope.launch(Dispatchers.IO) {
            updateTask(item.copy(updatedAt = System.currentTimeMillis()))
            refreshTasks()
        }
    }

    fun deleteTask(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteTask(id)
            refreshTasks()
        }
    }
}
