package cz.appkazdarma.aiasistent.presentation.home.components

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.appkazdarma.aiasistent.R
import cz.appkazdarma.aiasistent.presentation.home.HomeViewModel

@Composable
fun NetworkSpeedSettings(viewModel: HomeViewModel) {
    val context = LocalContext.current
    val show = viewModel.showNetworkSpeed.value
    val usage = viewModel.networkUsageState.value

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(R.string.show_network_speed), modifier = Modifier.weight(1f))
            Switch(checked = show, onCheckedChange = {
                viewModel.setShowNetworkSpeed(context, it)
                viewModel.loadNetworkUsage(context)
            })
        }
        if (show && usage != null) {
            Spacer(modifier = Modifier.height(4.dp))
            NetworkUsageWidget(item = usage)
        }
    }
}

