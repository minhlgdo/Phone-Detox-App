package com.minhlgdo.phonedetoxapp.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.minhlgdo.phonedetoxapp.data.local.dao.JournalDao
import com.minhlgdo.phonedetoxapp.data.local.model.JournalEntity
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject


class JournalRepository @Inject constructor(
    private val journalDao: JournalDao
){
    suspend fun insertJournal(journal: JournalEntity) {
        journalDao.insertJournal(journal)
    }

    suspend fun getJournalTodayCount() : Int {
        // Get the current date
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val currentDate = LocalDate.now().format(formatter)
        return journalDao.getJournalTodayCount(currentDate).firstOrNull() ?: 0
    }
}