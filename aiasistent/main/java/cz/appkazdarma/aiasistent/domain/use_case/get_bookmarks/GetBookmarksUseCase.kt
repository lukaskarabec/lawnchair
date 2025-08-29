package cz.appkazdarma.aiasistent.domain.use_case.get_bookmarks

import cz.appkazdarma.aiasistent.domain.model.BookmarkItem
import cz.appkazdarma.aiasistent.domain.repository.BrowserRepository
import javax.inject.Inject

class GetBookmarksUseCase @Inject constructor(
    private val repository: BrowserRepository
) {
    suspend operator fun invoke(): List<BookmarkItem> {
        return repository.getBookmarks()
    }
}
