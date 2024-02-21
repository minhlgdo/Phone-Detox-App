package com.minhlgdo.phonedetoxapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.minhlgdo.phonedetoxapp.data.local.model.BlockedAppEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BlockedAppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlockedApps(phoneApp: BlockedAppEntity)

    @Delete
    suspend fun deleteBlockedApps(phoneApp: BlockedAppEntity)

    // Using Flow to observe changes in the database
    @Query("SELECT * FROM blockedappentity")
    fun getBlockedApps(): Flow<List<BlockedAppEntity>>

    @Query("SELECT name FROM blockedappentity WHERE packageName = :packageName")
    fun getAppName(packageName: String): Flow<String>
}