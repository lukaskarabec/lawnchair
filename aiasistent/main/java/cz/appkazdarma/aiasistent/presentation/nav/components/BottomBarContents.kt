package cz.appkazdarma.aiasistent.presentation.nav.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cz.appkazdarma.aiasistent.R
import cz.appkazdarma.aiasistent.presentation.nav.Path
import cz.appkazdarma.aiasistent.presentation.nav.ScreenState

@Composable
fun BottomBarContents(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    screenState: ScreenState,
    onDoneAction: (String) -> Unit
) {
    var text by remember { mutableStateOf("") } // aktuální text v poli

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    fun submitGeminiRequest() {
        onDoneAction(text) // předání textu volajícímu
        text = "" // vyčištění textu po odeslání
        keyboardController?.hide() // schování klávesnice
        focusManager.clearFocus() // odstranění focusu z textového pole
    }

    Row(
        modifier.padding(14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // podle aktuální obrazovky zobrazíme různé prvky
        when (screenState) {
            ScreenState.HOME -> {
                val targetDestination =
                    if (text.isNotEmpty()) Path.GeminiScreen else Path.AppsScreen // určení cílové obrazovky

                InputTextField(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    text = text,
                    placeholder = stringResource(R.string.ask_gemini_placeholder),
                    onValueChange = { input ->
                        text = input // aktualizace textu při psaní
                    },
                    onDoneAction = {
                        if (text.isNotEmpty()) {
                            submitGeminiRequest()
                            navController.navigate(targetDestination) // navigace na odpovídající obrazovku
                        }
                    }
                )

                CircleButton(
                    modifier = Modifier
                        .fillMaxHeight(),
                    iconId = if (text.isNotEmpty()) R.drawable.rounded_arrow_forward_48 else R.drawable.rounded_apps_48,
                    onClick = {
                        if (text.isNotEmpty()) {
                            submitGeminiRequest() // odeslání dotazu
                        }
                        navController.navigate(targetDestination) // navigace podle obsahu
                    }
                )
            }

            ScreenState.APPS -> {
                // TEMPORARY APPROACH
                val context = LocalContext.current
                val sharedPrefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

                CircleButton(
                    modifier = Modifier
                        .fillMaxHeight(),
                    iconId = R.drawable.rounded_settings_48,
                    onClick = {
                        sharedPrefs.edit().putBoolean("onboarding_completed", false).apply() // reset stavu onboarding

                        Toast.makeText(
                            context,
                            context.getString(R.string.restart_app_to_complete_onboarding),
                            Toast.LENGTH_LONG
                        ).show()

                        navController.popBackStack(Path.HomeScreen, false) // návrat na domovskou obrazovku
                    }
                )

                InputTextField(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    text = "",
                    placeholder = stringResource(R.string.search_apps_placeholder),
                    onValueChange = {
                        //search // zde by byla logika vyhledávání
                    },
                    onDoneAction = {
                        //search
                    }
                )

                CircleButton(
                    modifier = Modifier
                        .fillMaxHeight(),
                    iconId = R.drawable.rounded_arrow_downward_48,
                    onClick = {
                        navController.popBackStack(Path.HomeScreen, false) // návrat zpět
                    }
                )
            }

            ScreenState.GEMINI -> {

                InputTextField(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    text = text,
                    placeholder = stringResource(R.string.ask_gemini_placeholder),
                    onValueChange = { input ->
                        text = input // aktualizace textu
                    },
                    onDoneAction = {
                        if (text.isNotEmpty()) {
                            submitGeminiRequest() // odeslání dotazu
                        }
                    }
                )

                CircleButton(
                    modifier = Modifier
                        .fillMaxHeight(),
                    iconId = if (text.isNotEmpty()) R.drawable.rounded_arrow_forward_48 else R.drawable.rounded_arrow_downward_48,
                    onClick = {
                        if (text.isNotEmpty()) {
                            submitGeminiRequest() // odeslání dotazu
                        } else {
                            navController.popBackStack(Path.HomeScreen, false) // návrat na domovskou obrazovku
                        }
                    }
                )
            }
        }
    }
}

