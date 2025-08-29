package cz.appkazdarma.aiasistent.data.remote.dto.weather


import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("1h")
    val h: Double
)