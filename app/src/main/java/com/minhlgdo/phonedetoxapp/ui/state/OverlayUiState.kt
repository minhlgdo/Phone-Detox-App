package com.minhlgdo.phonedetoxapp.ui.state

data class OverlayUiState (
    val accessCount: Int = 1,
    val appName: String = "",
    val journalPopup: Boolean = false,
    val journalTitle: String = "",
    val journalContent: String = ""
)