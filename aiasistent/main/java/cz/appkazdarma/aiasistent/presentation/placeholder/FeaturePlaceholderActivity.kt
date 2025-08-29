package cz.appkazdarma.aiasistent.presentation.placeholder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cz.appkazdarma.aiasistent.R
import cz.appkazdarma.aiasistent.ui.theme.TriviLauncherTheme

const val EXTRA_FEATURE_NAME = "feature_name"

class FeaturePlaceholderActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val feature = intent.getStringExtra(EXTRA_FEATURE_NAME) ?: getString(R.string.default_feature_name)
        setContent {
            TriviLauncherTheme {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = stringResource(R.string.feature_placeholder_message, feature), style = MaterialTheme.typography.headlineMedium)
                }
            }
        }
    }
}
