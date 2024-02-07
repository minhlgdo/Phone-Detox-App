package com.minhlgdo.phonedetoxapp.ui.state

import com.minhlgdo.phonedetoxapp.data.local.PhoneApp
import com.minhlgdo.phonedetoxapp.data.local.PhoneAppEntity

data class SelectAppsUiState (
    val loadedApp: Boolean = false,
    val selected: List<PhoneApp> = emptyList(),
    val unselected: List<PhoneApp> = emptyList()
)
