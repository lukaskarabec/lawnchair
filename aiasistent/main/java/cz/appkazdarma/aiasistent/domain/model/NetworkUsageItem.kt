package cz.appkazdarma.aiasistent.domain.model

/**
 * Data class representing network usage for the current day.
 *
 * @property mobileBytes Data transferred over the mobile network in bytes.
 * @property wifiBytes Data transferred over Wi-Fi in bytes.
 */
data class NetworkUsageItem(
    val mobileBytes: Long,
    val wifiBytes: Long
)
