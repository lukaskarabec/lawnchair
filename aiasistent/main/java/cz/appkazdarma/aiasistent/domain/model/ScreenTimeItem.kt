package cz.appkazdarma.aiasistent.domain.model

/**
 * Data class representing screen time information.
 *
 * @property totalTime Total screen time in milliseconds for the current day.
 * @property perApp List of pairs (app label to time in milliseconds).
 */
data class ScreenTimeItem(
    val totalTime: Long,
    val perApp: List<Pair<String, Long>> = emptyList()
)
