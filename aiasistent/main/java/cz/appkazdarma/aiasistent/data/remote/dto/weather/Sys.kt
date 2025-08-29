package cz.appkazdarma.aiasistent.data.remote.dto.weather


data class Sys(
    val country: String,
    val id: Int,
    val sunrise: Int,
    val sunset: Int,
    val type: Int
)