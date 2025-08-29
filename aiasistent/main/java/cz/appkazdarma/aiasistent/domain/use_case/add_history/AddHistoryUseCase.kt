package cz.appkazdarma.aiasistent.domain.use_case.add_history

import cz.appkazdarma.aiasistent.data.local.browser.entity.BrowserHistoryEntity
import cz.appkazdarma.aiasistent.domain.repository.BrowserRepository
import javax.inject.Inject

class AddHistoryUseCase @Inject constructor(
    private val repository: BrowserRepository
) {
    suspend operator fun invoke(entity: BrowserHistoryEntity) {
        repository.addHistory(entity)
    }
}
