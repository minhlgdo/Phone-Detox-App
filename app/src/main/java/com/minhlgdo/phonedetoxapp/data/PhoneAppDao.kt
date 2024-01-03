package com.minhlgdo.phonedetoxapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PhoneAppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlockedApps(phoneApp: PhoneAppEntity)

    @Delete
    suspend fun deleteBlockedApps(phoneApp: PhoneAppEntity)

    @Query("SELECT * FROM phoneappentity")
    suspend fun getBlockedApps(): List<PhoneAppEntity>
}