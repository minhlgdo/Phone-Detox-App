package com.minhlgdo.phonedetoxapp.data.repository

import com.minhlgdo.phonedetoxapp.data.local.dao.BlockedAppDao
import com.minhlgdo.phonedetoxapp.data.local.model.BlockedAppEntity
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class PhoneAppRepository @Inject constructor
    (private val appDao: BlockedAppDao) {

    suspend fun insertBlockedApps(phoneApp: BlockedAppEntity) {
        appDao.insertBlockedApps(phoneApp)
    }

    suspend fun deleteBlockedApps(phoneApp: BlockedAppEntity) {
        appDao.deleteBlockedApps(phoneApp)
    }

    fun getBlockedApps() = appDao.getBlockedApps()

    suspend fun getBlockedAppName(packageName: String): String {
        if (packageName.isEmpty()) return ""
        return appDao.getAppName(packageName).firstOrNull() ?: ""
    }




}