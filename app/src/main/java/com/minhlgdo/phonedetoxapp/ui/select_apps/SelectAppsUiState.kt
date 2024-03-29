package com.minhlgdo.phonedetoxapp.ui.select_apps

import com.minhlgdo.phonedetoxapp.data.local.PhoneApp

data class SelectAppsUiState (
    val loadedApp: Boolean = false,
    val selected: List<PhoneApp> = emptyList(),
    val unselected: List<PhoneApp> = emptyList(),
    val saved: Boolean = false
)
