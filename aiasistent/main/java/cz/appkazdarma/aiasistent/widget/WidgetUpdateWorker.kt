package cz.appkazdarma.aiasistent.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.widget.RemoteViews
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import cz.appkazdarma.aiasistent.R

class WidgetUpdateWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val appWidgetManager = AppWidgetManager.getInstance(context) // získání instance správce widgetů
        val widgetComponent = ComponentName(context, SpotlightWidgetProvider::class.java) // odkaz na třídu widgetu
        val appWidgetIds = appWidgetManager.getAppWidgetIds(widgetComponent) // vyhledání všech ID widgetu
        val views = RemoteViews(context.packageName, R.layout.widget_spotlight) // příprava layoutu widgetu
        val spotlightText = context.getSharedPreferences("widget_prefs", Context.MODE_PRIVATE)
            .getString("spotlight_content", "Ukázkový obsah Spotlight") // načtení textu z preferencí
        views.setTextViewText(R.id.spotlight_text, spotlightText) // zobrazení textu ve widgetu
        appWidgetManager.updateAppWidget(appWidgetIds, views) // aktualizace widgetu v systému
        return Result.success() // informování WorkManageru o úspěšném dokončení
    }
}