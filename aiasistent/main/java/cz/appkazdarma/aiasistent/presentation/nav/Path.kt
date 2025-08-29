package cz.appkazdarma.aiasistent.presentation.nav

import kotlinx.serialization.Serializable

sealed class Path {
    @Serializable
    object AppsScreen : Path()
    @Serializable
    object GeminiScreen : Path()
    @Serializable
    object HomeScreen : Path()
}