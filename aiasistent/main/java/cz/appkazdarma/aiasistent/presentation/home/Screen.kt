// app/src/main/java/cz/appkazdarma/aiasistent/presentation/home/Screen.kt
package cz.appkazdarma.aiasistent.presentation.home

import java.util.UUID

data class Screen(
    val id: UUID = UUID.randomUUID(),
    val backgroundImagePath: String? = null,
    val widgets: List<Widget> = emptyList()
)