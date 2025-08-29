package cz.appkazdarma.aiasistent.presentation.newbrowser

import android.util.Log
import android.util.Patterns
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.CookieManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import cz.appkazdarma.aiasistent.voice.SpeechRecognizerManager
import cz.appkazdarma.aiasistent.R
import java.net.URLEncoder
import kotlin.runCatching

private class BrowserTab(context: android.content.Context, initialUrl: String) {
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
        try {
            webView.loadUrl(url)
        } catch (e: Exception) {
            Log.e("BrowserTab", "Failed to load URL: $url", e)
        }
    }

    fun navigateBack() { if (webView.canGoBack()) webView.goBack() }
    fun navigateForward() { if (webView.canGoForward()) webView.goForward() }
    fun reload() { webView.reload() }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleBrowserScreen(
    viewModel: SimpleBrowserViewModel = hiltViewModel()
) {
    var showBookmarks by remember { mutableStateOf(false) }
    var showHistory by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var showTabs by remember { mutableStateOf(false) }
    var isListening by remember { mutableStateOf(false) }

    val bookmarks by viewModel.bookmarks
    val history by viewModel.history

    val context = LocalContext.current
    val tabs = remember { mutableStateListOf(BrowserTab(context, "https://www.google.com")) }
    var selectedTabIndex by remember { mutableStateOf(0) }
    val currentTab = tabs[selectedTabIndex]

    val isHttps by remember(currentTab.lastLoadedUrl.value) {
        derivedStateOf { currentTab.lastLoadedUrl.value?.startsWith("https://") == true }
    }

    LaunchedEffect(currentTab.lastLoadedUrl.value) {
        val url = currentTab.lastLoadedUrl.value
        if (url != null) viewModel.recordHistory(url, currentTab.pageTitle.value)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Tab row with close buttons and new tab
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, tab ->
                Tab(selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(tab.pageTitle.value ?: stringResource(R.string.tab_number, index + 1)) },
                    icon = {
                        IconButton(onClick = {
                            tabs.removeAt(index)
                            if (tabs.isEmpty()) {
                                tabs.add(BrowserTab(context, "https://www.google.com"))
                                selectedTabIndex = 0
                            } else if (selectedTabIndex >= tabs.size) {
                                selectedTabIndex = tabs.lastIndex.coerceAtLeast(0)
                            }
                        }) { Icon(Icons.Default.Close, contentDescription = "Close tab") }
                    }
                )
            }
            Tab(selected = false, onClick = {
                tabs.add(BrowserTab(context, "https://www.google.com"))
                selectedTabIndex = tabs.lastIndex
            }) { Icon(Icons.Default.Add, contentDescription = "New tab") }
        }

        if (bookmarks.isNotEmpty()) {
            LazyRow(modifier = Modifier.padding(vertical = 8.dp)) {
                items(bookmarks) { item ->
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .clickable { currentTab.loadUrl(item.url) },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Star, contentDescription = item.title ?: item.url)
                        Text(text = item.title ?: item.url, maxLines = 1)
                    }
                }
            }
        }

        var address by remember(selectedTabIndex, currentTab.lastLoadedUrl.value) {
            mutableStateOf(currentTab.lastLoadedUrl.value ?: "")
        }
        var suggestionsExpanded by remember { mutableStateOf(false) }
        val suggestions by remember(address, history) {
            derivedStateOf {
                history.filter {
                    it.url.contains(address, true) || (it.title?.contains(address, true) == true)
                }.take(5)
            }
        }

        val speechRecognizer = remember {
            SpeechRecognizerManager(
                context,
                onResult = { result -> address = result },
                onListeningState = { state -> isListening = state }
            )
        }
        DisposableEffect(Unit) {
            onDispose { speechRecognizer.destroy() }
        }

        Row(
            modifier = Modifier
                .padding(8.dp)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                if (isHttps) Icons.Default.Lock else Icons.Default.Warning,
                contentDescription = null
            )
        ExposedDropdownMenuBox(
            expanded = suggestionsExpanded && suggestions.isNotEmpty(),
            onExpandedChange = { suggestionsExpanded = it },
            modifier = Modifier.weight(1f)
        ) {
                TextField(
                    value = address,
                    onValueChange = {
                        address = it
                        suggestionsExpanded = it.isNotBlank()
                    },
                    singleLine = true,
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(imeAction = ImeAction.Go),
                    keyboardActions = androidx.compose.foundation.text.KeyboardActions(onGo = {
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
                            currentTab.loadUrl(url)
                        }.onFailure { e -> Log.e("Browser", "Failed to load $address", e) }
                    })
                )
                DropdownMenu(
                    expanded = suggestionsExpanded && suggestions.isNotEmpty(),
                    onDismissRequest = { suggestionsExpanded = false }
                ) {
                    suggestions.forEach { item ->
                        DropdownMenuItem(text = { Text(item.title ?: item.url) }, onClick = {
                            address = item.url
                            suggestionsExpanded = false
                        })
                    }
                }
            }
            IconButton(onClick = { speechRecognizer.startListening() }) {
                Icon(Icons.Default.Mic, contentDescription = "Voice")
            }
            IconButton(onClick = {
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
                    currentTab.loadUrl(url)
                }.onFailure { e -> Log.e("Browser", "Failed to load $address", e) }
            }) { Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Go") }
        }

        AndroidView(factory = { currentTab.webView }, modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .padding(8.dp)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = { currentTab.navigateBack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") }
            IconButton(onClick = { currentTab.navigateForward() }) { Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Forward") }
            IconButton(onClick = { currentTab.reload() }) { Icon(Icons.Default.Refresh, contentDescription = "Reload") }
            IconButton(onClick = { currentTab.loadUrl("https://www.google.com") }) { Icon(Icons.Default.Home, contentDescription = "Home") }
            IconButton(onClick = { tabs.add(BrowserTab(context, "https://www.google.com")); selectedTabIndex = tabs.lastIndex }) { Icon(Icons.Default.Add, contentDescription = "New tab") }
            IconButton(onClick = { showTabs = true }) {
                Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Tabs")
            }
            IconButton(onClick = {
                viewModel.addBookmark(
                    currentTab.lastLoadedUrl.value ?: "",
                    currentTab.pageTitle.value
                )
            }) {
                Icon(Icons.Default.Star, contentDescription = "Bookmark")
            }
            IconButton(onClick = { showBookmarks = true }) { Icon(Icons.Default.Star, contentDescription = "Show bookmarks") }
            IconButton(onClick = { showHistory = true }) { Icon(Icons.Default.History, contentDescription = "Show history") }
            IconButton(onClick = { viewModel.toggleIncognito() }) { Icon(Icons.Default.VisibilityOff, contentDescription = if (viewModel.incognito.value) "Incognito" else "Regular") }
            IconButton(onClick = { showMenu = true }) { Icon(Icons.Default.MoreVert, contentDescription = "Menu") }
        }
    }

    if (showBookmarks) {
        ModalBottomSheet(onDismissRequest = { showBookmarks = false }) {
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                items(bookmarks) { item ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = item.title ?: item.url)
                        Button(onClick = { currentTab.loadUrl(item.url); showBookmarks = false }) { Text(stringResource(R.string.open)) }
                    }
                }
            }
        }
    }

    if (showHistory) {
        ModalBottomSheet(onDismissRequest = { showHistory = false }) {
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                items(history) { item ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = item.title ?: item.url)
                        Button(onClick = { currentTab.loadUrl(item.url); showHistory = false }) { Text(stringResource(R.string.open)) }
                    }
                }
            }
        }
    }

    if (showTabs) {
        ModalBottomSheet(onDismissRequest = { showTabs = false }) {
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                itemsIndexed(tabs) { index, tab ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = tab.pageTitle.value ?: stringResource(R.string.tab_number, index + 1))
                        Button(onClick = { selectedTabIndex = index; showTabs = false }) { Text(stringResource(R.string.open)) }
                    }
                }
            }
        }
    }

    if (showMenu) {
        ModalBottomSheet(onDismissRequest = { showMenu = false }) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    tabs.add(BrowserTab(context, "https://www.google.com"))
                    selectedTabIndex = tabs.lastIndex
                    showMenu = false
                  }) { Text(stringResource(R.string.new_tab)) }
                Button(onClick = {
                    viewModel.toggleIncognito()
                    tabs.add(BrowserTab(context, "https://www.google.com"))
                    selectedTabIndex = tabs.lastIndex
                    showMenu = false
                  }) { Text(stringResource(R.string.incognito_tab)) }
                  Button(onClick = { showBookmarks = true; showMenu = false }) { Text(stringResource(R.string.favorites)) }
                  Button(onClick = { showHistory = true; showMenu = false }) { Text(stringResource(R.string.history)) }
                Button(onClick = {
                    viewModel.clearHistory()
                    CookieManager.getInstance().removeAllCookies(null)
                    currentTab.webView.clearCache(true)
                    currentTab.webView.clearHistory()
                    showMenu = false
                  }) { Text(stringResource(R.string.clear_data)) }
            }
        }
    }
}
