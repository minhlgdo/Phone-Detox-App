package com.minhlgdo.phonedetoxapp.ui.statistics

import androidx.lifecycle.ViewModel
import com.minhlgdo.phonedetoxapp.data.repository.PhoneAppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val phoneRepo: PhoneAppRepository
) : ViewModel() {

}