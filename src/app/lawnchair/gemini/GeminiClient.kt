package app.lawnchair.gemini

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.android.launcher3.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Simple helper for interacting with Google's Gemini API.
 * Provides chat and spotlight style queries using the API key exposed via BuildConfig.
 */
class GeminiClient {

    private val model = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.geminiKey,
    )

    /**
     * Sends a chat prompt to Gemini and returns the plain text response.
     */
    suspend fun chat(prompt: String, history: List<Content> = emptyList()): String =
        withContext(Dispatchers.IO) {
            val chat = model.startChat(history = history)
            chat.sendMessage(prompt).text.orEmpty()
        }

    /**
     * Runs a lightweight query intended for spotlight-style suggestions.
     */
    suspend fun spotlight(query: String): String = withContext(Dispatchers.IO) {
        model.generateContent(query).text.orEmpty()
    }
}

