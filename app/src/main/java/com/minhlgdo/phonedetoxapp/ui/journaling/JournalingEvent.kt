package com.minhlgdo.phonedetoxapp.ui.journaling

import com.minhlgdo.phonedetoxapp.data.local.model.JournalEntity

sealed interface JournalingEvent {
    data class DeleteJournal(val journal: JournalEntity) : JournalingEvent

}