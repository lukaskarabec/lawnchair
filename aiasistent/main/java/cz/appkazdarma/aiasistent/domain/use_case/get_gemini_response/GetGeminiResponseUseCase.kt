package cz.appkazdarma.aiasistent.domain.use_case.get_gemini_response

import android.util.Log
import cz.appkazdarma.aiasistent.BuildConfig
import cz.appkazdarma.aiasistent.common.Constants
import cz.appkazdarma.aiasistent.common.Resource
import cz.appkazdarma.aiasistent.domain.model.GeminiItem
import cz.appkazdarma.aiasistent.domain.use_case.get_app.GetAppUseCase
import cz.appkazdarma.aiasistent.domain.use_case.get_app.StringType
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetGeminiResponseUseCase @Inject constructor(
    private val getApp: GetAppUseCase
) {

    val TAG = "GetGeminiResponseUseCase"

    private val generativeModel = GenerativeModel(
        modelName = Constants.MODEL_NAME,
        apiKey = BuildConfig.geminiKey,
        generationConfig = Constants.GEMINI_CONFIG,
        safetySettings = Constants.SAFETY_SETTINGS
    )

    suspend operator fun invoke(prompt: String, history: List<Content> = emptyList()) : Flow<Resource<GeminiItem>> = flow {
        try {
            emit(Resource.Loading())
            val chat = generativeModel.startChat(history = history)
            var result = GeminiItem("")

            // with timeout
            val response = chat.sendMessage(prompt).text

            // do proper error handling
            response?.let {
                val userResponse = response
                    .substringBefore("-END-")
                    .filterNot { it == '\n' || it == '\r' }

                result = result.copy(response = userResponse)

                val apps = response
                    .substringAfter("-END-")
                    .split(",").map { it.trim() }
                    .mapNotNull { appName ->
                        getApp(appName, StringType.APP_NAME)
                    }

                result = result.copy(apps = apps)
                emit(Resource.Success(result))
                return@flow
            }

            emit(Resource.Error("Error getting Gemini response"))
            return@flow
        } catch(e: Exception) {
            Log.e(TAG, "Error getting Gemini response ${e.message}", e)
            emit(Resource.Error("Error getting Gemini response"))
        }
    }

}
