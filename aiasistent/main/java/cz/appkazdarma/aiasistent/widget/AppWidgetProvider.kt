package cz.appkazdarma.aiasistent.widget

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.widget.RemoteViews
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import cz.appkazdarma.aiasistent.R
import cz.appkazdarma.aiasistent.presentation.MainActivity
import java.util.concurrent.TimeUnit

class SpotlightWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // systém volá tuto metodu při potřebě překreslit widget
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_spotlight) // vytvoření vzdálených pohledů dle layoutu
            val spotlightText = getSpotlightContent(context) // načtení textu z uložených dat
            views.setTextViewText(R.id.spotlight_text, spotlightText) // naplnění textového pole obsahem
            val intent = Intent(context, MainActivity::class.java) // intent pro otevření hlavní aktivity po kliknutí
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE) // zabalení intentu do PendingIntentu
            views.setOnClickPendingIntent(R.id.widget_root, pendingIntent) // přiřazení akce kliknutí
            appWidgetManager.updateAppWidget(appWidgetId, views) // odeslání aktualizovaných dat systému
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        // zachycení broadcastu pro manuální či plánovanou aktualizaci widgetu
        if (intent.action == "cz.appkazdarma.aiasistent.ACTION_UPDATE_WIDGET" ||
            intent.action == "cz.appkazdarma.aiasistent.ACTION_ALARM_UPDATE_WIDGET"
        ) {
            val appWidgetManager = AppWidgetManager.getInstance(context) // získání správce widgetů
            val widgetComponent = ComponentName(context, SpotlightWidgetProvider::class.java) // identifikace našeho widgetu
            val appWidgetIds = appWidgetManager.getAppWidgetIds(widgetComponent) // všechny instance widgetu
            val views = RemoteViews(context.packageName, R.layout.widget_spotlight) // nové vzdálené pohledy
            val spotlightText = getSpotlightContent(context) // načtení aktuálního textu
            views.setTextViewText(R.id.spotlight_text, spotlightText) // nastavení textu
            appWidgetManager.updateAppWidget(appWidgetIds, views) // aktualizace všech widgetů
        }
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        setAlarm(context) // po prvním přidání widgetu nastavíme opakovaný alarm
        val workRequest = PeriodicWorkRequestBuilder<SpotlightAutoUpdateWorker>(15, TimeUnit.MINUTES)
            .build() // definice periodické práce pro automatické získání obsahu
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "SpotlightAutoUpdate",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        ) // naplánování periodické práce
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        cancelAlarm(context) // při odebrání posledního widgetu zrušíme alarm
    }

    companion object {
        fun setAlarm(context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager // získání systému alarmů
            val intent = Intent(context, WidgetAlarmReceiver::class.java).apply {
                action = "cz.appkazdarma.aiasistent.ACTION_ALARM_UPDATE_WIDGET" // akce pro aktualizaci
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            ) // vytvoření PendingIntentu pro broadcast
            alarmManager.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP, // typ alarmu - probudit zařízení
                SystemClock.elapsedRealtime() + 60_000L, // první spuštění za minutu
                60_000L, // opakování každou minutu
                pendingIntent // akce alarmu
            )
        }

        fun cancelAlarm(context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager // získání systému alarmů
            val intent = Intent(context, WidgetAlarmReceiver::class.java).apply {
                action = "cz.appkazdarma.aiasistent.ACTION_ALARM_UPDATE_WIDGET" // akce alarmu
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            ) // identifikace alarmu, který chceme zrušit
            alarmManager.cancel(pendingIntent) // zrušení opakovaného alarmu
        }
    }

    private fun getSpotlightContent(context: Context): String {
        val sharedPrefs = context.getSharedPreferences("widget_prefs", Context.MODE_PRIVATE) // přístup k uloženým preferencím
        return sharedPrefs.getString("spotlight_content", "Ukázkový obsah Spotlight") ?: "Ukázkový obsah Spotlight" // načtení textu, případně výchozí hodnota
    }
}