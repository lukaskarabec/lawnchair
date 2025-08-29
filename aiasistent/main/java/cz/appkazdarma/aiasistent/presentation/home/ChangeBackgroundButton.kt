// app/src/main/java/cz/appkazdarma/aiasistent/presentation/home/components/ChangeBackgroundButton.kt
package cz.appkazdarma.aiasistent.presentation.home.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import cz.appkazdarma.aiasistent.R
import cz.appkazdarma.aiasistent.presentation.home.HomeViewModel
import cz.appkazdarma.aiasistent.presentation.appearance.AppearanceSettingsActivity
import kotlinx.coroutines.launch

@Composable
fun ChangeBackgroundButton(viewModel: HomeViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            scope.launch {
                viewModel.setBackgroundImagePath(it.toString())
                viewModel.saveBackgroundImagePath(context)
            }
        }
    }

    val settingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            (context as? Activity)?.recreate()
        }
    }

    Row {
        Button(onClick = { launcher.launch("image/*") }) {
            Text(text = stringResource(R.string.select_custom_wallpaper))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = {
            val intent = Intent(context, AppearanceSettingsActivity::class.java)
            settingsLauncher.launch(intent)
        }) {
            Text(text = stringResource(R.string.edit_display))
        }
    }
}