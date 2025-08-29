package cz.appkazdarma.aiasistent.domain.use_case.get_notifications

import android.util.Log
import cz.appkazdarma.aiasistent.domain.repository.NotificationRepository
import javax.inject.Inject

class GetNotificationsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    private val TAG = "GetNotificationsUseCase"

    suspend operator fun invoke(): String {
        try {
            notificationRepository.deleteNotifications()
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting notifications: ${e.message}")
        }

        return try {
            val notifications =  notificationRepository.getAllNotifications()

            if (notifications.isEmpty()) {
                return "No notifications found"
            }

            //maybe add a limit to the number of notifications returned
            // only add non null package names
            val result = buildString {
                notifications
//                    .take(50)
                    .forEach { notification ->
                    appendLine(notification)
                }
            }

            result
        } catch (e: Exception) {
            Log.e(TAG, "Error getting notifications: ${e.message}", e)
            "Could not get notifications"
        }
    }

}