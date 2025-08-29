package cz.appkazdarma.aiasistent.domain.model

data class WeatherWidgetItem(
    val temperature: String,
    val iconCode: String,
    val iconResource: Int? = null,
)
