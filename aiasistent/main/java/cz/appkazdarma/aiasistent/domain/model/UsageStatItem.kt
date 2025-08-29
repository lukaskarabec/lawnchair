package cz.appkazdarma.aiasistent.domain.model

/**
 * Aggregate statistics about app and browser usage.
 */
data class UsageStatItem(
    val id: String,
    val date: Long,
    val appStats: List<AppUsage>,
    val browserStats: List<BrowserUsage>
)

data class AppUsage(val packageName: String, val usageMs: Long)

data class BrowserUsage(val url: String, val usageMs: Long)
