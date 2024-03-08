package com.minhlgdo.phonedetoxapp.ui.statistics

interface StatisticsEvents {
    data class SelectTimeRange(val timeRange: String) : StatisticsEvents

}