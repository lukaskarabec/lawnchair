package cz.appkazdarma.aiasistent.domain.use_case.add_notification

import android.util.Log
import cz.appkazdarma.aiasistent.data.local.notifications.entity.NotificationEntity
import cz.appkazdarma.aiasistent.domain.repository.NotificationRepository
import javax.inject.Inject

class AddNotificationUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {

    private val TAG = "AddNotificationUseCase"

    suspend operator fun invoke(notification: NotificationEntity) {
        try {
            notificationRepository.insertNotification(notification)
        } catch (e: Exception) {
            Log.e(TAG, "Error adding notification: ${e.message}", e)
        }

        try {
            notificationRepository.deleteNotifications()
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting notifications: ${e.message}", e)
        }
    }
}