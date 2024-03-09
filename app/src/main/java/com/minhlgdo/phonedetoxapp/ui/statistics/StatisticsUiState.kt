package com.minhlgdo.phonedetoxapp.ui.statistics

import com.minhlgdo.phonedetoxapp.data.local.UsageResult

data class StatisticsUiState(
    val totalAttempts: Int = 0,
    val attempts: List<UsageResult> = emptyList(),
)