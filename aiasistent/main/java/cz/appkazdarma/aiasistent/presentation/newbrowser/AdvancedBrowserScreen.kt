package cz.appkazdarma.aiasistent.presentation.newbrowser

import android.util.Patterns
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import cz.appkazdarma.aiasistent.domain.model.BookmarkItem
import cz.appkazdarma.aiasistent.domain.model.BrowserHistoryItem
import cz.appkazdarma.aiasistent.R
import java.net.URLEncoder
import kotlin.runCatching

private class AdvancedBrowserTab(
    context: android.content.Context,
    initialUrl: String
) {
    val webView: WebView = WebView(context).apply {
        webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                lastLoadedUrl.value = url
                pageTitle.value = view?.title
            }
        }
        settings.javaScriptEnabled = true
        loadUrl(initialUrl)
    }

    val lastLoadedUrl = mutableStateOf<String?>(initialUrl)
    val pageTitle = mutableStateOf<String?>(null)

    fun loadUrl(url: String) {
        runCatching { webView.loadUrl(url) }
    }

    fun navigateBack() { if (webView.canGoBack()) webView.goBack() }
    fun navigateForward() { if (webView.canGoForward()) webView.goForward() }
    fun reload() { webView.reload() }
}

/**
 * A more complete browser implementation inspired by the provided UI design.
 * It shows a simple home screen with quick access bookmarks and history
 * suggestions. When a page is loaded, a top bar with an address field is
 * displayed together with bottom navigation controls.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvancedBrowserScreen(
    viewModel: SimpleBrowserViewModel = hiltViewModel()
) {
    val bookmarks by viewModel.bookmarks
    val history by viewModel.history

    val context = LocalContext.current
    var tabs by remember { mutableStateOf(listOf(AdvancedBrowserTab(context, "https://www.google.com"))) }
    var selectedTabIndex by remember { mutableStateOf(0) }
    var showHome by remember { mutableStateOf(true) }
    val currentTab = tabs[selectedTabIndex]

    var address by remember(selectedTabIndex, currentTab.lastLoadedUrl.value) {
        mutableStateOf(currentTab.lastLoadedUrl.value ?: "")
    }

    LaunchedEffect(currentTab.lastLoadedUrl.value) {
        val url = currentTab.lastLoadedUrl.value
        if (url != null) viewModel.recordHistory(url, currentTab.pageTitle.value)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        if (showHome) {
            HomeContent(
                address = address,
                onAddressChange = { address = it },
                onSubmit = {
                    loadAddress(address, currentTab) { showHome = false }
                },
                bookmarks = bookmarks,
                onBookmarkClick = { url ->
                    address = url
                    loadAddress(url, currentTab) { showHome = false }
                },
                history = history.take(5),
                onHistoryClick = { url ->
                    address = url
                    loadAddress(url, currentTab) { showHome = false }
                }
            )
        } else {
            TopBar(address, onAddressChange = { address = it }) {
                loadAddress(address, currentTab)
            }
            AndroidView(factory = { currentTab.webView }, modifier = Modifier.weight(1f))
        }

        BottomControls(
            onBack = { if (!showHome) currentTab.navigateBack() },
            onForward = { if (!showHome) currentTab.navigateForward() },
            onHome = { showHome = true },
            onNewTab = {
                tabs = tabs + AdvancedBrowserTab(context, "https://www.google.com")
                selectedTabIndex = tabs.lastIndex
                showHome = true
            },
            onMenu = { /* no-op placeholder */ }
        )
    }
}

@Composable
private fun HomeContent(
    address: String,
    onAddressChange: (String) -> Unit,
    onSubmit: () -> Unit,
    bookmarks: List<BookmarkItem>,
    onBookmarkClick: (String) -> Unit,
    history: List<BrowserHistoryItem>,
    onHistoryClick: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        AddressField(address, onAddressChange, onSubmit, modifier = Modifier.weight(1f))
        Text(text = stringResource(R.string.favorites), style = MaterialTheme.typography.titleMedium)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(bookmarks) { item ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(onClick = { onBookmarkClick(item.url) }) {
                        Icon(Icons.Default.Star, contentDescription = null)
                    }
                    Text(text = item.title ?: item.url, maxLines = 1)
                }
            }
        }
        Text(text = "Pro vás", style = MaterialTheme.typography.titleMedium)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            history.forEach { item ->
                Card(onClick = { onHistoryClick(item.url) }) {
                    Text(item.title ?: item.url, modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}

@Composable
private fun TopBar(address: String, onAddressChange: (String) -> Unit, onSubmit: () -> Unit) {
    Row(modifier = Modifier.padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        AddressField(address, onAddressChange, onSubmit, modifier = Modifier.fillMaxWidth())
        IconButton(onClick = onSubmit) { Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Go") }
        IconButton(onClick = { onSubmit() }) { Icon(Icons.Default.Refresh, contentDescription = "Reload") }
    }
}

@Composable
private fun AddressField(
    address: String,
    onAddressChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = address,
        onValueChange = { onAddressChange(it) },
        modifier = modifier,
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
        keyboardActions = KeyboardActions(onGo = { onSubmit() })
    )
}

@Composable
private fun BottomControls(
    onBack: () -> Unit,
    onForward: () -> Unit,
    onHome: () -> Unit,
    onNewTab: () -> Unit,
    onMenu: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") }
        IconButton(onClick = onForward) { Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Forward") }
        IconButton(onClick = onHome) { Icon(Icons.Default.Home, contentDescription = "Home") }
        IconButton(onClick = onNewTab) { Icon(Icons.Default.Add, contentDescription = "New Tab") }
        IconButton(onClick = onMenu) { Icon(Icons.Default.Menu, contentDescription = "Menu") }
    }
}

private fun loadAddress(address: String, tab: AdvancedBrowserTab, postAction: (() -> Unit)? = null) {
    runCatching {
        val query = address.trim()
        val url = if (Patterns.WEB_URL.matcher(query).matches()) {
            if (!query.startsWith("http://") && !query.startsWith("https://")) {
                "https://$query"
            } else query
        } else {
            try {
                "https://www.google.com/search?q=" + URLEncoder.encode(query, "UTF-8")
            } catch (e: Exception) {
                "https://www.google.com/search?q=$query"
            }
        }
        tab.loadUrl(url)
        postAction?.invoke()
    }
}
