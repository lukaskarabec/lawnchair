package cz.appkazdarma.aiasistent.domain.use_case.dismiss_notification

import cz.appkazdarma.aiasistent.domain.repository.NotificationRepository
import javax.inject.Inject

/**
 * Use case for marking a notification as dismissed.
 */
class DismissNotificationUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.dismissNotification(id)
    }
}
