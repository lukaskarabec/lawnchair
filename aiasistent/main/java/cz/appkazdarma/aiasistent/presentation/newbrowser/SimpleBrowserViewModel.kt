package cz.appkazdarma.aiasistent.presentation.newbrowser

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.appkazdarma.aiasistent.data.local.browser.entity.BookmarkEntity
import cz.appkazdarma.aiasistent.data.local.browser.entity.BrowserHistoryEntity
import cz.appkazdarma.aiasistent.domain.model.BookmarkItem
import cz.appkazdarma.aiasistent.domain.model.BrowserHistoryItem
import cz.appkazdarma.aiasistent.domain.use_case.add_bookmark.AddBookmarkUseCase
import cz.appkazdarma.aiasistent.domain.use_case.add_history.AddHistoryUseCase
import cz.appkazdarma.aiasistent.domain.use_case.delete_bookmark.DeleteBookmarkUseCase
import cz.appkazdarma.aiasistent.domain.use_case.get_bookmarks.GetBookmarksUseCase
import cz.appkazdarma.aiasistent.domain.use_case.get_history.GetHistoryUseCase
import cz.appkazdarma.aiasistent.domain.use_case.clear_history.ClearHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel for the new browser implementation.
 * It exposes bookmarks and history and provides helpers to record activity.
 */
@HiltViewModel
class SimpleBrowserViewModel @Inject constructor(
    private val addHistory: AddHistoryUseCase,
    private val getHistory: GetHistoryUseCase,
    private val addBookmark: AddBookmarkUseCase,
    private val getBookmarks: GetBookmarksUseCase,
    private val deleteBookmark: DeleteBookmarkUseCase,
    private val clearHistoryUseCase: ClearHistoryUseCase
) : ViewModel() {

    private val _bookmarks = mutableStateOf<List<BookmarkItem>>(emptyList())
    val bookmarks: State<List<BookmarkItem>> = _bookmarks

    private val _history = mutableStateOf<List<BrowserHistoryItem>>(emptyList())
    val history: State<List<BrowserHistoryItem>> = _history

    private val _incognito = mutableStateOf(false)
    val incognito: State<Boolean> = _incognito

    init {
        refreshData()
    }

    fun toggleIncognito() {
        _incognito.value = !_incognito.value
    }

    private fun refreshData() {
        viewModelScope.launch(Dispatchers.IO) {
            val bm = getBookmarks()
            val hist = getHistory()
            withContext(Dispatchers.Main) {
                _bookmarks.value = bm
                _history.value = hist
            }
        }
    }

    fun recordHistory(url: String, title: String?) {
        if (incognito.value) return
        viewModelScope.launch(Dispatchers.IO) {
            addHistory(BrowserHistoryEntity(url = url, title = title))
            val hist = getHistory()
            withContext(Dispatchers.Main) { _history.value = hist }
        }
    }

    fun addBookmark(url: String, title: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            addBookmark(BookmarkEntity(url = url, title = title))
            val bm = getBookmarks()
            withContext(Dispatchers.Main) { _bookmarks.value = bm }
        }
    }

    fun removeBookmark(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteBookmark(id)
            val bm = getBookmarks()
            withContext(Dispatchers.Main) { _bookmarks.value = bm }
        }
    }

    fun clearHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            clearHistoryUseCase()
            withContext(Dispatchers.Main) { _history.value = emptyList() }
        }
    }
}
