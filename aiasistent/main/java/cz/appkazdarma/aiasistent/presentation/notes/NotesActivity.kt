package cz.appkazdarma.aiasistent.presentation.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import cz.appkazdarma.aiasistent.ui.theme.TriviLauncherTheme
import cz.appkazdarma.aiasistent.presentation.notes.components.NotesScreen

@AndroidEntryPoint
class NotesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TriviLauncherTheme {
                NotesScreen()
            }
        }
    }
}
