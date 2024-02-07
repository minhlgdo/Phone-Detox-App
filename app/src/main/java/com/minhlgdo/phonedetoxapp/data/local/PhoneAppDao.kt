package com.minhlgdo.phonedetoxapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PhoneAppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlockedApps(phoneApp: PhoneAppEntity)

    @Delete
    suspend fun deleteBlockedApps(phoneApp: PhoneAppEntity)

    // Using Flow to observe changes in the database
    @Query("SELECT * FROM phoneappentity")
    fun getBlockedApps(): Flow<List<PhoneAppEntity>>
}