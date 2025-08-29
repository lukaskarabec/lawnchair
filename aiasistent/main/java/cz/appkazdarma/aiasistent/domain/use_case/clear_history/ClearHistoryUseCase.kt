package cz.appkazdarma.aiasistent.domain.use_case.clear_history

import cz.appkazdarma.aiasistent.domain.repository.BrowserRepository
import javax.inject.Inject

class ClearHistoryUseCase @Inject constructor(
    private val repository: BrowserRepository
) {
    suspend operator fun invoke() {
        repository.clearHistory()
    }
}
