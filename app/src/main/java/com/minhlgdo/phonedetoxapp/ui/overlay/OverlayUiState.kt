package com.minhlgdo.phonedetoxapp.ui.overlay

data class OverlayUiState (
    val accessCount: Int = 1,
    val appName: String = "",
    val journalPopup: Boolean = false,
    val journalTitle: String = "",
    val journalContent: String = "",
    val showBottomSheet : Boolean = false,
    val selectedReason: String = ""
)