package cz.appkazdarma.aiasistent.presentation.events

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import cz.appkazdarma.aiasistent.presentation.events.components.EventsScreen
import cz.appkazdarma.aiasistent.ui.theme.TriviLauncherTheme

@AndroidEntryPoint
class EventsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TriviLauncherTheme {
                EventsScreen()
            }
        }
    }
}
