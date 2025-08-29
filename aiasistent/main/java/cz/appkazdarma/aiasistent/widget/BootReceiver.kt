package cz.appkazdarma.aiasistent.widget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        // reakce na dokončení bootu zařízení
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            SpotlightWidgetProvider.setAlarm(context) // znovunastavení alarmu po restartu
            // spuštění periodické práce pro generování odpovědí do widgetu
            val workRequest = PeriodicWorkRequestBuilder<SpotlightAutoUpdateWorker>(15, TimeUnit.MINUTES)
                .build()
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "SpotlightAutoUpdate",
                ExistingPeriodicWorkPolicy.UPDATE,
                workRequest
            ) // naplánování periodické práce
        }
    }
}