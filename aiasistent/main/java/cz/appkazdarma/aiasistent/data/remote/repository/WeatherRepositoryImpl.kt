package cz.appkazdarma.aiasistent.data.remote.repository

import cz.appkazdarma.aiasistent.data.remote.OpenWeatherApi
import cz.appkazdarma.aiasistent.domain.model.WeatherItem
import cz.appkazdarma.aiasistent.domain.model.WeatherWidgetItem
import cz.appkazdarma.aiasistent.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: OpenWeatherApi
) : WeatherRepository {

    override suspend fun getWeather(lon: Double, lat: Double): WeatherItem {
        return weatherApi.getWeather(lon = lon, lat = lat).toWeatherItem()
    }

    override suspend fun getWeatherWidget(lon: Double, lat: Double): WeatherWidgetItem {
        return weatherApi.getWeather(lon = lon, lat = lat).toWeatherWidgetItem()
    }

    override suspend fun getForecast(lon: Double, lat: Double): List<WeatherItem> {
        return weatherApi.getForecast(lon = lon, lat = lat).toWeatherItems()
    }
}