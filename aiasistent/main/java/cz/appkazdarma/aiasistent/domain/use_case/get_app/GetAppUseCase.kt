package cz.appkazdarma.aiasistent.domain.use_case.get_app

import cz.appkazdarma.aiasistent.domain.model.CompactAppInfo
import cz.appkazdarma.aiasistent.domain.repository.AppRepository
import javax.inject.Inject

// move this somewhere else
enum class StringType {
    APP_NAME,
    APP_PACKAGE_NAME,
}

class GetAppUseCase @Inject constructor(
    private val appRepository: AppRepository
) {

    suspend operator fun invoke(packageName: String, type: StringType): CompactAppInfo? {
        return when (type) {
            StringType.APP_NAME -> appRepository.getAppByLabel(packageName)
            StringType.APP_PACKAGE_NAME -> appRepository.getAppByPackageName(packageName)
        }
    }

}