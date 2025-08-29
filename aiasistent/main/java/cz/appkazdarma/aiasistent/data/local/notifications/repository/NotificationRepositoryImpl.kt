package cz.appkazdarma.aiasistent.data.local.notifications.repository

import android.os.Build
import cz.appkazdarma.aiasistent.common.Constants
import cz.appkazdarma.aiasistent.data.local.notifications.NotificationDao
import cz.appkazdarma.aiasistent.data.local.notifications.entity.NotificationEntity
import cz.appkazdarma.aiasistent.data.remote.firebase.FirebaseLogger
import cz.appkazdarma.aiasistent.domain.model.NotificationItem
import cz.appkazdarma.aiasistent.domain.repository.AppRepository
import cz.appkazdarma.aiasistent.domain.repository.NotificationRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationDao: NotificationDao,
    private val appRepository: AppRepository,
) : NotificationRepository {

    private val firebaseRef get() =
        FirebaseDatabase.getInstance().reference
            .child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid ?: "unknown")
            .child("notifications")

    private val dateFormat = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
    private val timestampFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

    override suspend fun getAllNotifications(): List<NotificationItem> {
        val notifications = notificationDao.getAllNotifications()

        return notifications.map {
            val appName = appRepository.getAppByPackageName(it.packageName)?.label
            it.toNotificationItem().copy(packageName = appName ?: it.packageName)
        }
    }

    override suspend fun insertNotification(notification: NotificationEntity) {
        notificationDao.upsertNotification(notification)

        val datePath = dateFormat.format(Date(notification.timestamp))
        val entry = mapOf(
            "title" to notification.title,
            "subText" to notification.subText,
            "text" to notification.text,
            "bigText" to notification.bigText,
            "packageName" to notification.packageName,
            "timestamp" to timestampFormat.format(Date(notification.timestamp)),
            "deviceModel" to Build.MODEL
        )

        firebaseRef.child(datePath).child(notification.id.toString()).setValue(entry)
        FirebaseLogger.log("notifications/add", entry)
    }

    override suspend fun deleteNotifications() {
        val currentTimeMillis = System.currentTimeMillis()
        val twentyFourHoursInMillis = Constants.MAX_NOTIFICATION_AGE_HOURS * 60 * 60 * 1000

        val cutoffTimestamp = currentTimeMillis - twentyFourHoursInMillis
        notificationDao.deleteNotifications(cutoffTimestamp)
    }

    override suspend fun dismissNotification(id: Long) {
        notificationDao.deleteNotificationById(id)
    }

}