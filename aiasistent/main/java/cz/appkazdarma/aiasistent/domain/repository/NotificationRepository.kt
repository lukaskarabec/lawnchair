package cz.appkazdarma.aiasistent.domain.repository

import cz.appkazdarma.aiasistent.data.local.notifications.entity.NotificationEntity
import cz.appkazdarma.aiasistent.domain.model.NotificationItem

interface NotificationRepository {

    suspend fun getAllNotifications(): List<NotificationItem>

    suspend fun insertNotification(notification: NotificationEntity)

    suspend fun deleteNotifications()

    suspend fun dismissNotification(id: Long)

}