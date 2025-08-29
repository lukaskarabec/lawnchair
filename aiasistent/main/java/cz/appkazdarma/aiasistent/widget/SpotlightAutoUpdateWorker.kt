package cz.appkazdarma.aiasistent.widget

import android.content.Context
import android.content.Intent
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SpotlightAutoUpdateWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        // zde se vytvoří nová textová odpověď pro widget; v produkci by zde byla skutečná logika
        val newResponse = "Automaticky vygenerovaná odpověď: ${System.currentTimeMillis() / 1000}"

        // uložení nového textu do sdílených preferencí
        val sharedPrefs = context.getSharedPreferences("widget_prefs", Context.MODE_PRIVATE)
        sharedPrefs.edit().putString("spotlight_content", newResponse).apply()

        // odeslání broadcastu, který vyvolá aktualizaci widgetu
        val intent = Intent("cz.appkazdarma.aiasistent.ACTION_UPDATE_WIDGET")
        context.sendBroadcast(intent)

        Result.success() // hlášení úspěšného dokončení
    }
}