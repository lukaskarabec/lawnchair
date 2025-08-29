package cz.appkazdarma.aiasistent.domain.use_case.add_bookmark

import cz.appkazdarma.aiasistent.data.local.browser.entity.BookmarkEntity
import cz.appkazdarma.aiasistent.domain.repository.BrowserRepository
import javax.inject.Inject

class AddBookmarkUseCase @Inject constructor(
    private val repository: BrowserRepository
) {
    suspend operator fun invoke(entity: BookmarkEntity) {
        repository.addBookmark(entity)
    }
}
