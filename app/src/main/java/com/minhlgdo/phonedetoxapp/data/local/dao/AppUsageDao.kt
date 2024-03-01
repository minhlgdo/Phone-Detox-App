package com.minhlgdo.phonedetoxapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.minhlgdo.phonedetoxapp.data.local.model.AppUsageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppUsageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAppUsage(appUsage: AppUsageEntity) : Long

//    // Get the app usage within a specific week grouped by day and ordered by time
//    @Query("SELECT DATE_FORMAT(time, '%Y/%m/%d') as date, COUNT(time) AS count FROM appusageentity WHERE name = :app AND WEEK(time) = :day GROUP BY DATE_FORMAT(time, '%Y/%m/%d')  ORDER BY time")
//    fun getAppUsageWeeklyCount(app: String, day: String): Flow<List<AppUsageResult>>

    @Query("SELECT COUNT(time) FROM usage_table WHERE packageName = :app AND DATE(time) = DATE(:date)")
    fun getAppUsageTodayCount(app: String, date: String): Flow<Int>

    // update the reason for app usage
    @Query("UPDATE usage_table SET reason = :reason WHERE id = :id")
    suspend fun updateUsageReason(id: Long, reason: String)
}