package app.lawnchair.gemini

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * Lightweight composable showing a single suggestion from Gemini.
 * Can be embedded on the home screen as a "Spotlight" section.
 */
@Composable
fun GeminiSpotlight(query: String, modifier: Modifier = Modifier) {
    val client = remember { GeminiClient() }
    var suggestion by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(query) {
        scope.launch {
            suggestion = client.spotlight(query)
        }
    }

    if (suggestion.isNotBlank()) {
        Card(modifier = modifier.fillMaxWidth().padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = suggestion, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
