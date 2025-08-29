package cz.appkazdarma.aiasistent.data.network

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.TrafficStats
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import cz.appkazdarma.aiasistent.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import cz.appkazdarma.aiasistent.data.remote.firebase.FirebaseLogger

class NetworkSpeedService : Service() {

    private val handler = Handler(Looper.getMainLooper())
    private var lastRxBytes: Long = 0
    private var lastTxBytes: Long = 0

    private var lastSpeedBytes: Long = -1

    override fun onCreate() {
        super.onCreate()
        lastRxBytes = TrafficStats.getTotalRxBytes()
        lastTxBytes = TrafficStats.getTotalTxBytes()
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(1, buildNotification("0 kb/s"))
        handler.post(updateRunnable)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateRunnable)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private val updateRunnable = object : Runnable {
        override fun run() {
            val newRx = TrafficStats.getTotalRxBytes()
            val newTx = TrafficStats.getTotalTxBytes()
            val diffRx = newRx - lastRxBytes
            val diffTx = newTx - lastTxBytes
            lastRxBytes = newRx
            lastTxBytes = newTx

            val speedBytes = diffRx + diffTx
            val speedText = formatSpeed(speedBytes)
            updateNotification(speedText)

            if (speedBytes != lastSpeedBytes) {
                lastSpeedBytes = speedBytes
                val timestampFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                val entry = mapOf(
                    "speedBytes" to speedBytes,
                    "timestamp" to timestampFormat.format(Date()),
                    "deviceModel" to Build.MODEL
                )
                FirebaseLogger.log("network_speed", entry)
            }


            handler.postDelayed(this, 1000)
        }
    }

    private fun updateNotification(speed: String) {
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(1, buildNotification(speed))
    }

    private fun buildNotification(speed: String): Notification {
        val channelId = "network_speed"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Network Speed", NotificationManager.IMPORTANCE_LOW)
            val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(channel)
        }
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(speed)
            .setSmallIcon(R.drawable.ic_send)
            .setOngoing(true)
            .build()
    }

    private fun formatSpeed(bytesPerSec: Long): String {
        val bits = bytesPerSec * 8
        val kb = bits / 1000.0
        val mb = kb / 1000.0
        val gb = mb / 1000.0
        return when {
            gb >= 1 -> String.format(Locale.getDefault(), "%.1f Gb/s", gb)
            mb >= 1 -> String.format(Locale.getDefault(), "%.1f Mb/s", mb)
            kb >= 1 -> String.format(Locale.getDefault(), "%.0f Kb/s", kb)
            else -> "$bits b/s"
        }
    }
}

