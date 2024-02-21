package com.minhlgdo.phonedetoxapp.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.minhlgdo.phonedetoxapp.data.local.dao.AppUsageDao
import com.minhlgdo.phonedetoxapp.data.local.dao.BlockedAppDao
import com.minhlgdo.phonedetoxapp.data.local.model.AppUsageEntity
import com.minhlgdo.phonedetoxapp.data.local.model.BlockedAppEntity
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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

    suspend fun getBlockedAppName(packageName: String): String {
        if (packageName.isEmpty()) return ""
        return appDao.getAppName(packageName).firstOrNull() ?: ""
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getTodayUsage(app: String) : Int {
        if (app.isEmpty()) return 0
        // Get the current date
        // (as DATE('now') in SQLite returns the date in UTC timezone, which is not what we want)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val currentDate = LocalDate.now().format(formatter)
        return usageDao.getAppUsageTodayCount(app, currentDate).firstOrNull() ?: 0
    }

    suspend fun updateAppUsage(app: AppUsageEntity) {
        usageDao.upsertAppUsage(app)
    }

}