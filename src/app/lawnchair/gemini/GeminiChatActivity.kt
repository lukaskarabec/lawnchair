package app.lawnchair.gemini

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * Simple activity allowing users to chat with Google Gemini.
 * Uses [GeminiClient] for network requests and keeps UI minimal to
 * match the existing Lawnchair design language.
 */
class GeminiChatActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val client = GeminiClient()
        setContent {
            MaterialTheme {
                var query by remember { mutableStateOf("") }
                var response by remember { mutableStateOf("") }
                val scope = rememberCoroutineScope()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        label = { Text("Ask Gemini") },
                        modifier = Modifier.fillMaxSize().weight(1f)
                    )
                    Button(onClick = {
                        scope.launch {
                            response = client.chat(query)
                        }
                    }) {
                        Text("Send")
                    }
                    if (response.isNotBlank()) {
                        Text(response)
                    }
                }
            }
        }
    }
}
