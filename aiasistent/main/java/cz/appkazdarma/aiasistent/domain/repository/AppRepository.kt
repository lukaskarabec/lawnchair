package cz.appkazdarma.aiasistent.domain.repository

import cz.appkazdarma.aiasistent.domain.model.CompactAppInfo

interface AppRepository {

    suspend fun getInstalledApps(): List<CompactAppInfo>

    suspend fun getAppByPackageName(packageName: String): CompactAppInfo?

    suspend fun getAppByLabel(label: String): CompactAppInfo?

}