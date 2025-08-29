package app.lawnchair.gemini

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Simple helper for interacting with Google's Gemini API.
 *
 * The real implementation depends on Google AI's Generative AI client library, which is
 * currently unavailable in the build. This stub keeps the project building while returning
 * placeholder results.
 */
class GeminiClient {

    /**
     * Sends a chat prompt to Gemini and returns the plain text response.
     *
     * This stubbed version simply returns a message indicating the service is unavailable.
     */
    suspend fun chat(prompt: String, history: List<Any> = emptyList()): String =
        withContext(Dispatchers.IO) {
            "Gemini service unavailable"
        }

    /**
     * Runs a lightweight query intended for spotlight-style suggestions.
     *
     * The stub returns an empty string to indicate no suggestion is available.
     */
    suspend fun spotlight(query: String): String = withContext(Dispatchers.IO) {
        ""
    }
}

