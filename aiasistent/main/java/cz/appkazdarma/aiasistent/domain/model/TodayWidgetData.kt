package cz.appkazdarma.aiasistent.domain.model

/**
 * Data class for the "Today" widget contents.
 */
data class TodayWidgetData(
    val date: Long,
    val nameDay: String,
    val holiday: String?,
    val weather: WeatherItem?,
    val newNotifications: Int
)
