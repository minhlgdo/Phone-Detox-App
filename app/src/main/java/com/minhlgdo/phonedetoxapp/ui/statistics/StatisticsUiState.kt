package com.minhlgdo.phonedetoxapp.ui.statistics

import com.minhlgdo.phonedetoxapp.data.local.UsageResult

data class StatisticsUiState(
    val totalAttempts: Int = 0,
    val attempts: Map<String, Int> = emptyMap(),
    val quitTimes: Int = 0,
    val quitRate: Float = 0f,
    val usageReason: List<Pair<String, UsageResult>> = emptyList(),
)