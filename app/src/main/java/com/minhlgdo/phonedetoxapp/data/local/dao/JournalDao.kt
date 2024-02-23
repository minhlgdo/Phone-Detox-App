package com.minhlgdo.phonedetoxapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.minhlgdo.phonedetoxapp.data.local.model.JournalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {
    @Upsert
    suspend fun insertJournal(journal: JournalEntity)

    @Delete
    suspend fun deleteJournal(journal: JournalEntity)

    @Query("SELECT COUNT(id) FROM journal WHERE DATE(time) = DATE(:currDate)")
    fun getJournalTodayCount(currDate: String): Flow<Int>

    @Query("SELECT * FROM journal ORDER BY DATETIME(time) DESC")
    fun getAllJournals(): Flow<List<JournalEntity>>
}