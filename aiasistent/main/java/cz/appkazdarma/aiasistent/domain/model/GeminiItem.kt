package cz.appkazdarma.aiasistent.domain.model

data class GeminiItem(
    val response: String,
    val hasAnimated: Boolean = false,
    val apps: List<CompactAppInfo> = emptyList(),
)
