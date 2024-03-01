package com.minhlgdo.phonedetoxapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.minhlgdo.phonedetoxapp.data.local.model.ReasonEntity
import kotlinx.coroutines.flow.Flow

// This is the Data Access Object (DAO) for the ReasonEntity
@Dao
interface ReasonDao {
    @Upsert
    suspend fun upsertAllReasons(reasons: List<ReasonEntity>)

    @Query("SELECT * FROM reasons")
    fun getReasons(): Flow<List<ReasonEntity>>
}
