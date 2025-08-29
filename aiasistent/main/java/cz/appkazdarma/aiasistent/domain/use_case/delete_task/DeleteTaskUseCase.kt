package cz.appkazdarma.aiasistent.domain.use_case.delete_task

import cz.appkazdarma.aiasistent.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(id: String) {
        repository.deleteTask(id)
    }
}
