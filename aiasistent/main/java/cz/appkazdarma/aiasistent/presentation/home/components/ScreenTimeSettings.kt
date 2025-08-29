package cz.appkazdarma.aiasistent.presentation.home.components

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.appkazdarma.aiasistent.R
import cz.appkazdarma.aiasistent.presentation.home.HomeViewModel
import cz.appkazdarma.aiasistent.presentation.home.components.FeatureShortcut

@Composable
fun ScreenTimeSettings(viewModel: HomeViewModel) {
    val context = LocalContext.current
    val show = viewModel.showScreenTime.value
    val detailed = viewModel.screenTimeDetailed.value

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(R.string.show_screen_time), modifier = Modifier.weight(1f))
            Switch(checked = show, onCheckedChange = {
                viewModel.setShowScreenTime(context, it)
                viewModel.loadScreenTime()
            })
        }
        if (show) {
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.detailed_mode), modifier = Modifier.weight(1f))
                Switch(checked = detailed, onCheckedChange = {
                    viewModel.setScreenTimeDetailed(context, it)
                    viewModel.loadScreenTime()
                })
            }

//            Spacer(modifier = Modifier.height(8.dp))
//            FeatureShortcut(
//                label = stringResource(R.string.gpt4all_launch),
//                icon = Icons.Filled.Android,
//                onClick = {
//                    context.startActivity(android.content.Intent(context, cz.appkazdarma.aiasistent.presentation.gpt4all.Gpt4AllActivity::class.java))
//                }
//            )
        }
    }
}
