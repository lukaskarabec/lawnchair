package cz.appkazdarma.aiasistent.presentation.browser

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

/**
 * Experimentální implementace prohlížeče využívající Chromium Embedded Framework (CEF).
 * Vyžaduje nativní knihovny CEF, které nejsou součástí projektu.
 * [CefBrowserWrapper] obaluje platformně specifický CEF view.
 */
class CefBrowserWrapper(context: Context, initialUrl: String) {
    /** Zástupný View, dokud není CEF inicializováno. */
    val view = android.view.View(context)

    init {
        // TODO: Inicializovat CEF a načíst [initialUrl].
    }

    fun loadUrl(url: String) {
        // TODO: Předat URL instanci prohlížeče.
    }

    fun destroy() {
        // TODO: Korektně ukončit běh CEF.
    }
}

@Composable
fun CefBrowserScreen(initialUrl: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val browser = remember { CefBrowserWrapper(context, initialUrl) } // vytvoření wrapperu a zapamatování mezi recomposy

    AndroidView(factory = { browser.view }, modifier = modifier.fillMaxSize()) // vložení nativního View do Compose stromu

    DisposableEffect(Unit) {
        onDispose { browser.destroy() } // při zničení composable ukončit CEF
    }
}
