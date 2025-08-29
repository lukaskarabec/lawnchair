package cz.appkazdarma.aiasistent.domain.repository

import cz.appkazdarma.aiasistent.domain.model.WeatherItem
import cz.appkazdarma.aiasistent.domain.model.WeatherWidgetItem

interface WeatherRepository {

    suspend fun getWeather(lon: Double, lat: Double) : WeatherItem

    suspend fun getWeatherWidget(lon: Double, lat: Double) : WeatherWidgetItem

    suspend fun getForecast(lon: Double, lat: Double) : List<WeatherItem>

}