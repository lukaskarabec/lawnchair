package cz.appkazdarma.aiasistent.presentation.gpt4all.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewModelScope
import cz.appkazdarma.aiasistent.R
import cz.appkazdarma.aiasistent.common.Resource
import cz.appkazdarma.aiasistent.presentation.apps.ChatItem
import cz.appkazdarma.aiasistent.presentation.gpt4all.Gpt4AllViewModel
import cz.appkazdarma.aiasistent.presentation.home.components.GeminiResponseContent
import androidx.compose.ui.platform.LocalContext
import cz.appkazdarma.aiasistent.voice.SpeechRecognizerManager
import cz.appkazdarma.aiasistent.voice.TtsManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * A composable function that displays the GPT4All screen with a list of chat items.
 *
 * @param modifier The modifier to be applied to the GPT4All screen.
 * @param inputText A shared flow of input text to be processed by the ViewModel.
 * @param viewModel The ViewModel that manages the state and logic of the GPT4All screen.
 */
@Composable
fun Gpt4AllScreen(
    modifier: Modifier = Modifier,
    inputText: MutableSharedFlow<String>,
    voiceMode: Boolean,
    onVoiceFinished: () -> Unit,
    viewModel: Gpt4AllViewModel = hiltViewModel()
) {
    val chatState by viewModel.chatState
    val context = LocalContext.current
    val ttsManager = remember { TtsManager(context) }
    var isListening by remember { mutableStateOf(false) }
    var isSpeaking by remember { mutableStateOf(false) }
    val speechRecognizer = remember {
        SpeechRecognizerManager(
            context,
            onResult = { result ->
                isListening = false
                viewModel.viewModelScope.launch {
                    viewModel.newRequest(result)
                }
            },
            onUnavailable = {},
            onListeningState = { state -> isListening = state }
        )
    }
    DisposableEffect(Unit) {
        onDispose {
            speechRecognizer.destroy()
            ttsManager.shutdown()
            onVoiceFinished()
        }
    }

    LaunchedEffect(inputText) {
        inputText.collectLatest { text ->
            viewModel.newRequest(text)
        }
    }

    LaunchedEffect(voiceMode) {
        if (voiceMode) {
            speechRecognizer.startListening()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            items(chatState.size) { index ->
                when (val item = chatState[index]) {
                    is ChatItem.GeminiResponse -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                                .padding(16.dp)
                        ) {
                            when (val response = item.response) {
                                is Resource.Success -> response.data?.let { data ->
                                    LaunchedEffect(data.response) {
                                        isSpeaking = true
                                        ttsManager.speak(data.response) {
                                            isSpeaking = false
                                            if (voiceMode) {
                                                speechRecognizer.startListening()
                                            }
                                        }
                                    }
                                    GeminiResponseContent(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .animateContentSize(),
                                        data = data,
                                        textAnimationCallback = {
                                            viewModel.completeAnimationState(index)
                                        },
                                        onAppClick = { packageName ->
                                            viewModel.launchApp(packageName)
                                        }
                                    )
                                }

                                is Resource.Loading -> Text(
                                    text = stringResource(R.string.generuji_odpov),
                                    style = MaterialTheme.typography.bodyLarge
                                )

                                is Resource.Error -> Text(
                                    text = response.message ?: "Error :(",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }

                    is ChatItem.UserRequest -> Box(
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = Color.Black.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .fillMaxWidth()
                            .padding(16.dp)

                    ) {
                        Text(
                            modifier = Modifier,
                            text = item.request,
                            style = MaterialTheme.typography.bodyLarge.copy(fontStyle = FontStyle.Italic),
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        if (isListening) {
            Text(
                text = stringResource(R.string.listening_indicator),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (isSpeaking) {
            Text(
                text = stringResource(R.string.speaking_indicator),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}