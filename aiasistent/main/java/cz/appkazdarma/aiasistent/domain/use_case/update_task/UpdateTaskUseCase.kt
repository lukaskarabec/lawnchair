package cz.appkazdarma.aiasistent.domain.use_case.update_task

import cz.appkazdarma.aiasistent.domain.model.TaskItem
import cz.appkazdarma.aiasistent.domain.repository.TaskRepository
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(item: TaskItem) {
        repository.addOrUpdateTask(item)
    }
}
