package cz.appkazdarma.aiasistent.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cz.appkazdarma.aiasistent.R
import cz.appkazdarma.aiasistent.domain.model.ScreenTimeItem
import androidx.compose.ui.res.stringResource

private fun formatDuration(ms: Long): String {
    val minutes = ms / 60000
    val hours = minutes / 60
    val mins = minutes % 60
    return "${hours}h ${mins}m"
}

@Composable
fun ScreenTimeWidget(item: ScreenTimeItem, detailed: Boolean, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
    ) {
        Text(
            text = stringResource(R.string.screen_time_today, formatDuration(item.totalTime)),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (detailed) {
            item.perApp.take(5).forEach { (label, time) ->
                Text(
                    text = "$label: ${formatDuration(time)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
