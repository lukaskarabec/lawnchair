package cz.appkazdarma.aiasistent.domain.use_case.get_history

import cz.appkazdarma.aiasistent.domain.model.BrowserHistoryItem
import cz.appkazdarma.aiasistent.domain.repository.BrowserRepository
import javax.inject.Inject

class GetHistoryUseCase @Inject constructor(
    private val repository: BrowserRepository
) {
    suspend operator fun invoke(): List<BrowserHistoryItem> {
        return repository.getHistory()
    }
}
