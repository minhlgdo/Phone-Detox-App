package com.minhlgdo.phonedetoxapp.ui.overlay

sealed interface OverlayEvent {
    data class SetJournalTitle(val title: String) : OverlayEvent
    data class SetJournalContent(val content: String) : OverlayEvent
    object ShowJournalPopup : OverlayEvent
    object HideJournalPopup : OverlayEvent
    object SaveJournal : OverlayEvent
    object ShowBottomSheet : OverlayEvent
    object HideBottomSheet : OverlayEvent
    object EnterBlockedApp : OverlayEvent
    object NotEnterApp : OverlayEvent
    object UpdateAccessLog : OverlayEvent
    data class SelectReason(val reason: String) : OverlayEvent
}