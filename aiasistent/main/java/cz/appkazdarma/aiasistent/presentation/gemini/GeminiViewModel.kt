package cz.appkazdarma.aiasistent.presentation.gemini

import android.os.Build
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.appkazdarma.aiasistent.common.Resource
import cz.appkazdarma.aiasistent.domain.use_case.get_gemini_prompt.GetGeminiPromptUseCase
import cz.appkazdarma.aiasistent.domain.use_case.get_gemini_response.GetGeminiResponseUseCase
import cz.appkazdarma.aiasistent.domain.use_case.launch_app.LaunchAppUseCase
import cz.appkazdarma.aiasistent.presentation.apps.ChatItem
import cz.appkazdarma.aiasistent.data.remote.firebase.FirebaseLogger
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.content
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

data class GeminiItemDto(
    val request: String = "",
    val response: String = "",
    val timestamp: String = "",
    val deviceModel: String = Build.MODEL,
)

@HiltViewModel
class GeminiViewModel @Inject constructor(
    private val getGeminiPrompt: GetGeminiPromptUseCase,
    private val getGeminiResponse: GetGeminiResponseUseCase,
    private val launchAppIntent: LaunchAppUseCase
) : ViewModel() {

    private var geminiJob: Job? = null

    private val _chatState = mutableStateOf<List<ChatItem>>(emptyList())
    val chatState: State<List<ChatItem>> = _chatState

    private val chatHistory: MutableList<Content> = mutableListOf()

    init {
        println("GeminiViewModel init")
        // add widget content in chat history using data store
    }

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
                        val responseText = result.data?.response ?: "Error getting Gemini response"
                        saveToFirebase(request, responseText)
                        chatHistory += content(role = "user") { text(request) }
                        chatHistory += content(role = "model") { text(responseText) }
                    }
                }.launchIn(this)

        }
    }

    fun completeGeminiAnimationState(index: Int) {
        val item = _chatState.value[index]

        if (item is ChatItem.GeminiResponse && item.response is Resource.Success) {
            item.response.data?.let { data->
                _chatState.value = _chatState.value.toMutableList().also { list ->
                    list[index] = item.copy(response = Resource.Success(data.copy(hasAnimated = true)))
                }
            }
        }
    }

    fun launchApp(packageName: String) {
        if (!launchAppIntent(packageName)) {
            println("couldn't launch app")
//            viewModelScope.launch {
//                _eventFlow.emit(HomeUIEvent.ShowSnackbar("Couldn't launch app"))
//            }
//            need to show snackbar
        }
    }

    private fun saveToFirebase(request: String, response: String) {
        val timestampFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val formattedTimestamp = timestampFormat.format(Date())

        val item = GeminiItemDto(
            request = request,
            response = response,
            timestamp = formattedTimestamp,
        )

        FirebaseLogger.log("gemini/chat", item)
    }

}