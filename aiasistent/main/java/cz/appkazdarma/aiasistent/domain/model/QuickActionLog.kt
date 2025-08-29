package cz.appkazdarma.aiasistent.domain.model

/**
 * Log entry for a quick action performed by the user.
 */
data class QuickActionLog(
    val id: String,
    val action: String,
    val target: String,
    val timestamp: Long
)
