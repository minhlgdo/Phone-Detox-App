package com.minhlgdo.phonedetoxapp.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minhlgdo.phonedetoxapp.data.repository.UsageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    usageRepo: UsageRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(StatisticsUiState())
    private val _attempts = usageRepo.getUsageLast7Days()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val uiState = combine(_uiState, _attempts) { state, attempts ->
        state.copy(
            attempts = attempts,
            totalAttempts = attempts.sumOf { it.count }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), StatisticsUiState())

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