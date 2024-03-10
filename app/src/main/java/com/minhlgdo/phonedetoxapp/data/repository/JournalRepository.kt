package com.minhlgdo.phonedetoxapp.data.repository

import com.minhlgdo.phonedetoxapp.data.local.dao.JournalDao
import com.minhlgdo.phonedetoxapp.data.local.model.JournalEntity
import kotlinx.coroutines.flow.Flow
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

    suspend fun deleteJournal(journal: JournalEntity) {
        journalDao.deleteJournal(journal)
    }

    fun getAllJournals() : Flow<List<JournalEntity>> {
        return journalDao.getAllJournals()
    }
}