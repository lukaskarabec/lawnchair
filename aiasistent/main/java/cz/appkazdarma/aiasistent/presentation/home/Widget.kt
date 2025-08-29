// app/src/main/java/cz/appkazdarma/aiasistent/presentation/home/Widget.kt
package cz.appkazdarma.aiasistent.presentation.home

import java.util.UUID

data class Widget(
    val id: UUID = UUID.randomUUID(),
    val type: String,
    val data: String
)