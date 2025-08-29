package cz.appkazdarma.aiasistent.data.remote.dto.forecast

import cz.appkazdarma.aiasistent.domain.model.WeatherItem


data class ForecastDto(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<WeatherComp>,
    val message: Int
) {
    fun toWeatherItems() : List<WeatherItem> {
        return list.map { it.toWeatherItem() }
    }
}