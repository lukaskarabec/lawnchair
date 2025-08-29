package cz.appkazdarma.aiasistent.presentation.nav.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import cz.appkazdarma.aiasistent.R

@Composable
fun HelpScreen() {
    Text(text = stringResource(R.string.help_screen_message)) // jednoduchý text s informací
}

@Preview(showBackground = true)
@Composable
fun HelpScreenPreview() {
    HelpScreen() // náhled komponenty pro vývojáře
}