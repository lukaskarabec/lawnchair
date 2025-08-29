package cz.appkazdarma.aiasistent.ai

import android.content.Context
import android.graphics.Bitmap
import com.google.ai.client.generativeai.GenerativeModel
import cz.appkazdarma.aiasistent.common.Constants

/**
 * Implementation of [AiService] that automatically chooses between on-device
 * Gemini Nano and cloud based Gemini models. Detection of on-device support is
 * very naive and checks for the presence of the Google AI Core package. When
 * on-device inference fails or is not available, requests fall back to the
 * cloud API.
 */
class HybridAiService(private val context: Context) : AiService {

    private val cloudModel by lazy {
        GenerativeModel(
            modelName = Constants.MODEL_NAME,
            apiKey = "AIzaSyAlQgWH8rQ6bzv_HysVLWAIgDIkj7r1YKE",
            generationConfig = Constants.GEMINI_CONFIG,
            safetySettings = Constants.SAFETY_SETTINGS
        )
    }

    private val onDeviceAvailable: Boolean by lazy { isGeminiNanoAvailable() }

    /** Heuristic check if Google AI Core (Gemini Nano) is installed. */
    private fun isGeminiNanoAvailable(): Boolean =
        try {
            context.packageManager.getPackageInfo("com.google.android.aicore", 0)
            true
        } catch (_: Exception) {
            false
        }

    override suspend fun getAiTextResponse(prompt: String): AiResponse {
        return tryOnDeviceOrCloud(prompt, AiModality.TEXT)
    }

    override suspend fun getAiCodeResponse(prompt: String): AiResponse {
        return tryOnDeviceOrCloud(prompt, AiModality.CODE)
    }

    override suspend fun getAiImageAnalysis(prompt: String, image: Bitmap): AiResponse {
        return tryOnDeviceOrCloud(prompt, AiModality.IMAGE)
    }

    private suspend fun tryOnDeviceOrCloud(prompt: String, modality: AiModality): AiResponse {
        if (onDeviceAvailable) {
            runCatching {
                val content = runOnDevice(prompt, modality)
                return AiResponse(content, AiMode.ON_DEVICE, modality)
            }.onFailure { /* fall back */ }
        }
        val cloudContent = runCloud(prompt, modality)
        return AiResponse(cloudContent, AiMode.CLOUD, modality)
    }

    /** Placeholder on-device implementation. In a real app this would call
     * Google AI Core APIs. */
    private suspend fun runOnDevice(prompt: String, modality: AiModality): String {
        // TODO: implement Gemini Nano handling once available in the SDK
        return runCloud(prompt, modality)
    }

    /**
     * Execute the request using the cloud based Gemini model.
     */
    private suspend fun runCloud(prompt: String, modality: AiModality): String {
        val chat = cloudModel.startChat()
        return chat.sendMessage(prompt).text ?: ""
    }
}
