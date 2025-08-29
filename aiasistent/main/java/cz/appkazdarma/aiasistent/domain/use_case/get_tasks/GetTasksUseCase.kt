package cz.appkazdarma.aiasistent.domain.use_case.get_tasks

import cz.appkazdarma.aiasistent.domain.repository.TaskRepository
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke() = repository.getTasks()
}
