package cz.appkazdarma.aiasistent.ai

import android.graphics.Bitmap

/**
 * Generic interface describing AI capabilities used in the app.
 */
interface AiService {
    /**
     * Process a text prompt and return the response.
     */
    suspend fun getAiTextResponse(prompt: String): AiResponse

    /**
     * Process a code prompt and return the generated code or explanation.
     */
    suspend fun getAiCodeResponse(prompt: String): AiResponse

    /**
     * Analyse an image together with a text prompt and return the result.
     */
    suspend fun getAiImageAnalysis(prompt: String, image: Bitmap): AiResponse
}
