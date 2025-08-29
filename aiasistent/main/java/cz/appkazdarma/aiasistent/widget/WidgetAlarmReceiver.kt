package cz.appkazdarma.aiasistent.widget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class WidgetAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        // ověření, zda přijatý broadcast odpovídá našemu plánovanému alarmu
        if (intent?.action == "cz.appkazdarma.aiasistent.ACTION_ALARM_UPDATE_WIDGET") {
            val updateIntent = Intent("cz.appkazdarma.aiasistent.ACTION_UPDATE_WIDGET") // vytvoření vlastního broadcastu pro widget
            context.sendBroadcast(updateIntent) // rozeslání broadcastu, který aktualizuje widget
        }
    }
}