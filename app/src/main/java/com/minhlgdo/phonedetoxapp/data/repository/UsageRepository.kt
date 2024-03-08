package com.minhlgdo.phonedetoxapp.data.repository

import com.minhlgdo.phonedetoxapp.data.local.dao.AppUsageDao
import com.minhlgdo.phonedetoxapp.data.local.dao.ReasonDao
import com.minhlgdo.phonedetoxapp.data.local.model.AppUsageEntity
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class UsageRepository @Inject constructor(
    private val usageDao: AppUsageDao, private val reasonDao: ReasonDao
) {
    suspend fun getTodayUsage(app: String): Int {
        if (app.isEmpty()) return 0
        // Get the current date
        // (as DATE('now') in SQLite returns the date in UTC timezone, which is not what we want)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val currentDate = LocalDate.now().format(formatter)
        return usageDao.getAppUsageDailyCount(app, currentDate).firstOrNull() ?: 0
    }

    // Return the id of the inserted or updated app usage
    suspend fun upsertAppUsage(app: AppUsageEntity) : Long {
        return usageDao.upsertAppUsage(app)
    }

    suspend fun updateUsageReason(logId: Long, reason: String) {
        return usageDao.updateUsageReason(logId, reason)
    }

    // Get reasons for app blocking
    fun getReasons() = reasonDao.getReasons()
}