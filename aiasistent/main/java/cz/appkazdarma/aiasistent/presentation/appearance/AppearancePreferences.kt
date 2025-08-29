package cz.appkazdarma.aiasistent.presentation.appearance

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

object AppearancePreferences {
    private const val PREFS_NAME = "appearance_prefs"

    const val LIGHT_PRIMARY = "light_primary"
    const val LIGHT_ON_PRIMARY = "light_on_primary"
    const val LIGHT_BACKGROUND = "light_background"
    const val LIGHT_ON_BACKGROUND = "light_on_background"

    const val DARK_PRIMARY = "dark_primary"
    const val DARK_ON_PRIMARY = "dark_on_primary"
    const val DARK_BACKGROUND = "dark_background"
    const val DARK_ON_BACKGROUND = "dark_on_background"

    fun getColor(context: Context, key: String, default: Color): Color {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return Color(prefs.getInt(key, default.toArgb()))
    }

    fun setColor(context: Context, key: String, color: Color) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(key, color.toArgb()).apply()
    }
}
