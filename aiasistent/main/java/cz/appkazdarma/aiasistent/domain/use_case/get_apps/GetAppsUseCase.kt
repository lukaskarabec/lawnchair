package cz.appkazdarma.aiasistent.domain.use_case.get_apps

import cz.appkazdarma.aiasistent.domain.model.CompactAppInfo
import cz.appkazdarma.aiasistent.domain.repository.AppRepository
import javax.inject.Inject

class GetAppsUseCase @Inject constructor(
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(): List<CompactAppInfo> {
        return appRepository.getInstalledApps()
            .filter { it.isLaunchable }
            .sortedBy { it.label.lowercase() }
    }
}