package cz.appkazdarma.aiasistent.presentation.appearance

import android.content.Context
import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import cz.appkazdarma.aiasistent.ui.theme.TriviLauncherTheme

class AppearanceSettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TriviLauncherTheme {
                AppearanceSettingsScreen()
            }
        }
    }
}

@Composable
private fun ColorField(label: String, color: Color, onColorChange: (Color) -> Unit) {
    var showPicker by remember { mutableStateOf(false) }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showPicker = true },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color)
            )
        }

        if (showPicker) {
            val controller = rememberColorPickerController()
            LaunchedEffect(Unit) { controller.selectByColor(color, false) }
            AlertDialog(
                onDismissRequest = { showPicker = false },
                confirmButton = {
                    TextButton(onClick = { showPicker = false }) { Text("OK") }
                },
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        HsvColorPicker(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            controller = controller,
                            onColorChanged = { envelope ->
                                onColorChange(envelope.color)
                            },
                            initialColor = color
                        )
                        AlphaSlider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            controller = controller
                        )
                        BrightnessSlider(
                            modifier = Modifier.fillMaxWidth(),
                            controller = controller
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun AppearanceSettingsScreen(context: Context = LocalContext.current) {
    val lightPrimaryDefault = MaterialTheme.colorScheme.primary
    val lightOnPrimaryDefault = MaterialTheme.colorScheme.onPrimary
    val lightBackgroundDefault = MaterialTheme.colorScheme.background
    val lightOnBackgroundDefault = MaterialTheme.colorScheme.onBackground

    val darkPrimaryDefault = MaterialTheme.colorScheme.primary
    val darkOnPrimaryDefault = MaterialTheme.colorScheme.onPrimary
    val darkBackgroundDefault = MaterialTheme.colorScheme.background
    val darkOnBackgroundDefault = MaterialTheme.colorScheme.onBackground

    var lightPrimary by remember { mutableStateOf(AppearancePreferences.getColor(context, AppearancePreferences.LIGHT_PRIMARY, lightPrimaryDefault)) }
    var lightOnPrimary by remember { mutableStateOf(AppearancePreferences.getColor(context, AppearancePreferences.LIGHT_ON_PRIMARY, lightOnPrimaryDefault)) }
    var lightBackground by remember { mutableStateOf(AppearancePreferences.getColor(context, AppearancePreferences.LIGHT_BACKGROUND, lightBackgroundDefault)) }
    var lightOnBackground by remember { mutableStateOf(AppearancePreferences.getColor(context, AppearancePreferences.LIGHT_ON_BACKGROUND, lightOnBackgroundDefault)) }

    var darkPrimary by remember { mutableStateOf(AppearancePreferences.getColor(context, AppearancePreferences.DARK_PRIMARY, darkPrimaryDefault)) }
    var darkOnPrimary by remember { mutableStateOf(AppearancePreferences.getColor(context, AppearancePreferences.DARK_ON_PRIMARY, darkOnPrimaryDefault)) }
    var darkBackground by remember { mutableStateOf(AppearancePreferences.getColor(context, AppearancePreferences.DARK_BACKGROUND, darkBackgroundDefault)) }
    var darkOnBackground by remember { mutableStateOf(AppearancePreferences.getColor(context, AppearancePreferences.DARK_ON_BACKGROUND, darkOnBackgroundDefault)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Světlý režim", style = MaterialTheme.typography.titleMedium)
        ColorField("Primární", lightPrimary) { lightPrimary = it }
        ColorField("Text na primární", lightOnPrimary) { lightOnPrimary = it }
        ColorField("Pozadí", lightBackground) { lightBackground = it }
        ColorField("Text na pozadí", lightOnBackground) { lightOnBackground = it }

        Text(text = "Tmavý režim", style = MaterialTheme.typography.titleMedium)
        ColorField("Primární", darkPrimary) { darkPrimary = it }
        ColorField("Text na primární", darkOnPrimary) { darkOnPrimary = it }
        ColorField("Pozadí", darkBackground) { darkBackground = it }
        ColorField("Text na pozadí", darkOnBackground) { darkOnBackground = it }

        val activity = context as? Activity

        Button(onClick = {
            AppearancePreferences.setColor(context, AppearancePreferences.LIGHT_PRIMARY, lightPrimary)
            AppearancePreferences.setColor(context, AppearancePreferences.LIGHT_ON_PRIMARY, lightOnPrimary)
            AppearancePreferences.setColor(context, AppearancePreferences.LIGHT_BACKGROUND, lightBackground)
            AppearancePreferences.setColor(context, AppearancePreferences.LIGHT_ON_BACKGROUND, lightOnBackground)
            AppearancePreferences.setColor(context, AppearancePreferences.DARK_PRIMARY, darkPrimary)
            AppearancePreferences.setColor(context, AppearancePreferences.DARK_ON_PRIMARY, darkOnPrimary)
            AppearancePreferences.setColor(context, AppearancePreferences.DARK_BACKGROUND, darkBackground)
            AppearancePreferences.setColor(context, AppearancePreferences.DARK_ON_BACKGROUND, darkOnBackground)
            activity?.setResult(Activity.RESULT_OK)
            activity?.finish()
        }) {
            Text("Uložit")
        }
    }
}