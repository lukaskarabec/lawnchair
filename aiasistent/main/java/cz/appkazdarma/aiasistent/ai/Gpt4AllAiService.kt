package cz.appkazdarma.aiasistent.ai

import android.content.Context
import android.graphics.Bitmap

/**
 * Placeholder implementation of [AiService] that would use GPT4All with a local model.
 *
 * Actual model execution is not implemented. Integrate the GPT4All library and
 * load the model specified in [Constants.GPT4ALL_MODEL_NAME] to enable on-device
 * inference.
 */
class Gpt4AllAiService(private val context: Context) : AiService {
    override suspend fun getAiTextResponse(prompt: String): AiResponse {
        // TODO: Replace with real GPT4All inference
        return AiResponse(
            content = "(GPT4All stub response for '$prompt')",
            mode = AiMode.ON_DEVICE,
            modality = AiModality.TEXT
        )
    }

    override suspend fun getAiCodeResponse(prompt: String): AiResponse {
        // TODO: Replace with real GPT4All inference
        return AiResponse(
            content = "(GPT4All stub code response for '$prompt')",
            mode = AiMode.ON_DEVICE,
            modality = AiModality.CODE
        )
    }

    override suspend fun getAiImageAnalysis(prompt: String, image: Bitmap): AiResponse {
        // TODO: Replace with real GPT4All image analysis
        return AiResponse(
            content = "(GPT4All stub image analysis)",
            mode = AiMode.ON_DEVICE,
            modality = AiModality.IMAGE
        )
    }
}
