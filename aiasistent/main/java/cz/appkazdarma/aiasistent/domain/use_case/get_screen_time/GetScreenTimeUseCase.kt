package cz.appkazdarma.aiasistent.domain.use_case.get_screen_time

import android.app.usage.UsageStatsManager
import android.content.Context
import cz.appkazdarma.aiasistent.domain.model.ScreenTimeItem
import cz.appkazdarma.aiasistent.domain.use_case.get_app.GetAppUseCase
import cz.appkazdarma.aiasistent.domain.use_case.get_app.StringType
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

/**
 * Use case for retrieving today\'s screen time information.
 */
class GetScreenTimeUseCase @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val getApp: GetAppUseCase,
) {
    suspend operator fun invoke(): ScreenTimeItem {
        val usageStatsManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        val endTime = System.currentTimeMillis()
        val startTime = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000

        val stats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            startTime,
            endTime
        ).filter { it.totalTimeInForeground > 0 }

        val total = stats.sumOf { it.totalTimeInForeground }

        val perApp = stats.mapNotNull { usage ->
            val label = try {
                getApp(usage.packageName, StringType.APP_PACKAGE_NAME)?.label
            } catch (e: Exception) {
                null
            }
            label?.let { it to usage.totalTimeInForeground }
        }.sortedByDescending { it.second }

        return ScreenTimeItem(total, perApp)
    }
}
