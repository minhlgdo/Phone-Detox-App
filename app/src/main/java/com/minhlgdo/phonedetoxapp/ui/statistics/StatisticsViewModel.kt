package com.minhlgdo.phonedetoxapp.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minhlgdo.phonedetoxapp.data.local.UsageResult
import com.minhlgdo.phonedetoxapp.data.repository.UsageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    usageRepo: UsageRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(StatisticsUiState())
    private val _attempts = usageRepo.getUsageLast7Days()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyMap())
    private val _quits = usageRepo.getQuitLast7Days()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), UsageResult(0,0f))
    private val _reasons = usageRepo.getReasonsLast7Days()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyMap())
    val uiState = combine(_uiState, _attempts, _quits, _reasons) { state, attempts, quits, reasons ->
        state.copy(
            attempts = attempts,
            totalAttempts = attempts.values.sum(),
            quitTimes = quits.value,
            quitRate = quits.prob,
            usageReason = reasons.toList() // convert map to list of pairs for easier use in the UI
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(3000), StatisticsUiState())

//    init {
//        viewModelScope.launch {
//            val attempts = usageRepo.getUsageLast7Days()
//            val totalAttempts = attempts.sumOf { it.count }
//            _uiState.update { currentState ->
//                currentState.copy(
//                    loaded = true, attempts = attempts, totalAttempts = totalAttempts
//                )
//            }
//        }
//    }
}