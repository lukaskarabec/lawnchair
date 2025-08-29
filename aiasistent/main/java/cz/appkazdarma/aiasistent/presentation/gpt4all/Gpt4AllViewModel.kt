package cz.appkazdarma.aiasistent.presentation.gpt4all

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.content
import android.os.Build
import cz.appkazdarma.aiasistent.common.Resource
import cz.appkazdarma.aiasistent.domain.model.GeminiItem
import cz.appkazdarma.aiasistent.domain.use_case.get_gemini_prompt.GetGeminiPromptUseCase
import cz.appkazdarma.aiasistent.domain.use_case.get_gemini_response.GetGeminiResponseUseCase
import cz.appkazdarma.aiasistent.domain.use_case.launch_app.LaunchAppUseCase
import cz.appkazdarma.aiasistent.presentation.apps.ChatItem
import cz.appkazdarma.aiasistent.data.remote.firebase.FirebaseLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * Data Transfer Object for Firebase.
 *
 * @property request The user's request as a string.
 * @property response The response from GPT4All as a string.
 * @property timestamp The timestamp of the request and response.
 */
data class Gpt4AllItemDto(
    val request: String = "",
    val response: String = "",
    val timestamp: String = "",
    val deviceModel: String = Build.MODEL
    // Add other relevant properties from GeminiItem as needed
)

/**
 * ViewModel for managing the state of the GPT4All screen and handling user requests and responses.
 *
 * @property getGeminiPrompt Use case for retrieving the prompt.
 * @property getGeminiResponse Use case for retrieving the GPT4All response.
 * @property launchAppIntent Use case for launching an app by its package name.
 */
@HiltViewModel
class Gpt4AllViewModel @Inject constructor(
    private val getGeminiPrompt: GetGeminiPromptUseCase,
    private val getGeminiResponse: GetGeminiResponseUseCase,
    private val launchAppIntent: LaunchAppUseCase
) : ViewModel() {

    private var geminiJob: Job? = null

    private val _chatState = mutableStateOf<List<ChatItem>>(emptyList())
    val chatState: State<List<ChatItem>> = _chatState

    private val chatHistory: MutableList<Content> = mutableListOf()

    /**
     * Initializes the ViewModel.
     */
    init {
        println("Gpt4AllViewModel init")
    }

    /**
     * Handles a new user request by updating the chat state and retrieving the Gemini response.
     *
     * @param request The user's request as a string.
     */
    suspend fun newRequest(request: String) {
        val lastItem = _chatState.value.lastOrNull()

        when {
            lastItem is ChatItem.UserRequest -> return
            lastItem is ChatItem.GeminiResponse && lastItem.response is Resource.Loading -> return
        }

        geminiJob?.cancel()

        _chatState.value += ChatItem.UserRequest(request)
        _chatState.value += ChatItem.GeminiResponse(Resource.Loading())
        val index = _chatState.value.lastIndex

        geminiJob = viewModelScope.launch(Dispatchers.IO) {
            val (prompt, history) = getGeminiPrompt(request)
            if (chatHistory.isEmpty()) {
                chatHistory += history
            }

            getGeminiResponse(prompt, chatHistory)
                .onEach { result ->
                    _chatState.value = _chatState.value.toMutableList().apply {
                        set(index, ChatItem.GeminiResponse(result))
                    }

                    if (result is Resource.Success) {
                        val responseText = result.data?.response
                            ?: "Failed to generate response. Please try again later."
                        result.data?.let { geminiItem ->
                            saveToFirebase(request, responseText, geminiItem)
                            chatHistory += content(role = "user") { text(request) }
                            chatHistory += content(role = "model") {
                                text(
                                    geminiItem.response
                                )
                            }
                        }
                    }
                }.launchIn(this)
        }
    }

    /**
     * Saves the request and response to Firebase.
     *
     * @param request The user's request as a string.
     * @param response The response from Gemini as a string.
     * @param geminiItem The Gemini item containing the response data.
     */
    private fun saveToFirebase(request: String, response: String, @Suppress("UNUSED_PARAMETER") geminiItem: GeminiItem) {
        val timestampFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val formattedTimestamp = timestampFormat.format(Date())

        val geminiItemDto = Gpt4AllItemDto(
            request = request,
            response = response,
            timestamp = formattedTimestamp
        )

        FirebaseLogger.log("gpt4all/chat", geminiItemDto)
    }

    /**
     * Marks the Gemini response at the specified index as having completed its animation.
     *
     * @param index The index of the Gemini response in the chat state.
     */
    fun completeAnimationState(index: Int) {
        val item = _chatState.value[index]

        if (item is ChatItem.GeminiResponse && item.response is Resource.Success) {
            item.response.data?.let { data ->
                _chatState.value = _chatState.value.toMutableList().also { list ->
                    list[index] =
                        item.copy(response = Resource.Success(data.copy(hasAnimated = true)))
                }
            }
        }
    }

    /**
     * Launches an app by its package name.
     *
     * @param packageName The package name of the app to be launched.
     */
    fun launchApp(packageName: String) {
        if (!launchAppIntent(packageName)) {
            println("couldn't launch app")
        }
    }
}