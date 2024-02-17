package com.minhlgdo.phonedetoxapp.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.minhlgdo.phonedetoxapp.data.local.model.AppUsageEntity
import com.minhlgdo.phonedetoxapp.data.repository.PhoneAppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class OverlayViewModel @Inject constructor(
    private val repository: PhoneAppRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _currForegroundApp = savedStateHandle.getLiveData<String>("currentApp")

    private val currForegroundApp: String
        get() = _currForegroundApp.value ?: ""

    init {
        println("OverlayViewModel initialized, current app: $currForegroundApp")
        runBlocking {
            updateAppUsage()
            getAppUsage()
        }
    }

    private suspend fun updateAppUsage() {
        // Update the app usage
        repository.updateAppUsage(AppUsageEntity(currForegroundApp))
        println("App usage updated")
    }

    private suspend fun getAppUsage() {
        if (currForegroundApp.isEmpty()) return
        val count = repository.getTodayUsage(currForegroundApp)
        println("App usage count: ${count.first()}")
    }

    fun dismissOverlay() {
        // Dismiss the overlay
    }

    fun enterApp() {
        // Enter the app
    }
}