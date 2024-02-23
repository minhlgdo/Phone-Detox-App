package com.minhlgdo.phonedetoxapp.ui.journaling

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minhlgdo.phonedetoxapp.data.repository.JournalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JournalingViewModel @Inject constructor(
    private val journalRepository: JournalRepository
) : ViewModel() {

    val journals = journalRepository.getAllJournals()

    fun onEvent(event: JournalingEvent) {
        when(event){
            is JournalingEvent.DeleteJournal -> {
                // delete the journal
                viewModelScope.launch {
                    journalRepository.deleteJournal(event.journal)

                }
            }
        }
    }

}