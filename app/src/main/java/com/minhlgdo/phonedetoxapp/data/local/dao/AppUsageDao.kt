package com.minhlgdo.phonedetoxapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.minhlgdo.phonedetoxapp.data.local.model.AppUsageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppUsageDao {
    @Upsert
    suspend fun upsertAppUsage(appUsage: AppUsageEntity)

//    // Get the app usage within a specific week grouped by day and ordered by time
//    @Query("SELECT DATE_FORMAT(time, '%Y/%m/%d') as date, COUNT(time) AS count FROM appusageentity WHERE name = :app AND WEEK(time) = :day GROUP BY DATE_FORMAT(time, '%Y/%m/%d')  ORDER BY time")
//    fun getAppUsageWeeklyCount(app: String, day: String): Flow<List<AppUsageResult>>

    @Query("SELECT COUNT(DISTINCT time) FROM appusageentity WHERE packageName = :app AND DATE(time) = DATE('now')")
    fun getAppUsageTodayCount(app: String): Flow<Int>
}