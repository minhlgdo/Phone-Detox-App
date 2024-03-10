package com.minhlgdo.phonedetoxapp.data.repository

import com.minhlgdo.phonedetoxapp.data.local.UsageResult
import com.minhlgdo.phonedetoxapp.data.local.dao.AppUsageDao
import com.minhlgdo.phonedetoxapp.data.local.dao.ReasonDao
import com.minhlgdo.phonedetoxapp.data.local.model.AppUsageEntity
import kotlinx.coroutines.flow.Flow
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

    // get the usage result for the blocked apps within last 7 days
    fun getUsageLast7Days() : Flow<Map<String, Int>> {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val currentDate = LocalDate.now().format(formatter)
        return usageDao.getWeeklyUsageCount(currentDate)
    }

    // Get the number of times the app was quit after overlay was shown within the last 7 days
    fun getQuitLast7Days() : Flow<UsageResult> {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val currentDate = LocalDate.now().format(formatter)
        return usageDao.getWeeklyAppExitResult(currentDate)
    }

    // Get the reasons of app usage for the last 7 days, and the probability of each reason
    fun getReasonsLast7Days() : Flow<Map<String, UsageResult>> {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val currentDate = LocalDate.now().format(formatter)
        return usageDao.getReasonsForLast7Days(currentDate)
    }
}