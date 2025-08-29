package cz.appkazdarma.aiasistent.presentation.gpt4all

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import cz.appkazdarma.aiasistent.ui.theme.TriviLauncherTheme
import cz.appkazdarma.aiasistent.presentation.gpt4all.components.Gpt4AllScreen

@AndroidEntryPoint
class Gpt4AllActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TriviLauncherTheme {
                Gpt4AllScreen(
                    inputText = kotlinx.coroutines.flow.MutableSharedFlow(),
                    voiceMode = false,
                    onVoiceFinished = {}
                )
            }
        }
    }
}
