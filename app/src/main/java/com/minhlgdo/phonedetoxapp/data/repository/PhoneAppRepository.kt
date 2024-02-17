package com.minhlgdo.phonedetoxapp.data.repository

import com.minhlgdo.phonedetoxapp.data.local.dao.AppUsageDao
import com.minhlgdo.phonedetoxapp.data.local.dao.BlockedAppDao
import com.minhlgdo.phonedetoxapp.data.local.model.AppUsageEntity
import com.minhlgdo.phonedetoxapp.data.local.model.BlockedAppEntity
import javax.inject.Inject

class PhoneAppRepository @Inject constructor
    (private val appDao: BlockedAppDao, private val usageDao: AppUsageDao) {

    suspend fun insertBlockedApps(phoneApp: BlockedAppEntity) {
        appDao.insertBlockedApps(phoneApp)
    }

    suspend fun deleteBlockedApps(phoneApp: BlockedAppEntity) {
        appDao.deleteBlockedApps(phoneApp)
    }

    fun getBlockedApps() = appDao.getBlockedApps()

    fun getTodayUsage(app: String) = usageDao.getAppUsageTodayCount(app)

    suspend fun updateAppUsage(app: AppUsageEntity) {
        usageDao.upsertAppUsage(app)
    }

}