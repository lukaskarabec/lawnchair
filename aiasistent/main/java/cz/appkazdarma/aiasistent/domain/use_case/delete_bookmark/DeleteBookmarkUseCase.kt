package cz.appkazdarma.aiasistent.domain.use_case.delete_bookmark

import cz.appkazdarma.aiasistent.domain.repository.BrowserRepository
import javax.inject.Inject

class DeleteBookmarkUseCase @Inject constructor(
    private val repository: BrowserRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteBookmark(id)
    }
}
