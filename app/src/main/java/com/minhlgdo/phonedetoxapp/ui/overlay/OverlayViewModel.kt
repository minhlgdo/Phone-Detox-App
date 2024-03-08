package com.minhlgdo.phonedetoxapp.ui.overlay

import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minhlgdo.phonedetoxapp.data.local.model.AppUsageEntity
import com.minhlgdo.phonedetoxapp.data.local.model.JournalEntity
import com.minhlgdo.phonedetoxapp.data.repository.JournalRepository
import com.minhlgdo.phonedetoxapp.data.repository.PhoneAppRepository
import com.minhlgdo.phonedetoxapp.data.repository.UsageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class OverlayViewModel @Inject constructor(
    private val usageRepo: UsageRepository,
    private val phoneRepo: PhoneAppRepository,
    private val journalRepo: JournalRepository,
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _currForegroundApp = savedStateHandle.getLiveData<String>("currentAppPackage")
    private val currForegroundApp: String
        get() = _currForegroundApp.value ?: ""

    private val _uiState =
        MutableStateFlow(OverlayUiState()) // can be used to observe the state of the UI in the ViewModel only
    val uiState: StateFlow<OverlayUiState> = _uiState.asStateFlow()

    val allowsAppOpen = MutableLiveData(false)
    private var logId : Long = 0

    val reasons = usageRepo.getReasons()

    init {
        println("OverlayViewModel initialized, current app: $currForegroundApp")
        runBlocking {
            getAppName()
            getAppUsage()
        }
    }

    private fun getAppName() {
        if (currForegroundApp.isEmpty()) return
        viewModelScope.launch {
            val name = phoneRepo.getBlockedAppName(currForegroundApp)
            _uiState.update { currentState ->
                currentState.copy(
                    appName = name
                )
            }
        }
    }

    private suspend fun getAppUsage() {
        // Update the app usage
        if (currForegroundApp.isEmpty()) return
//        accessLog =
        logId = usageRepo.upsertAppUsage(AppUsageEntity(currForegroundApp))
        println("App usage updated, logId: $logId")

        // Get the app usage
        val count = usageRepo.getTodayUsage(currForegroundApp)
//        println("App usage: $count")
        _uiState.update { currentState ->
            currentState.copy(
                accessCount = count
            )
        }
    }

    fun onEvent(event: OverlayEvent) {
        when (event) {
            is OverlayEvent.SetJournalTitle -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        journalTitle = event.title
                    )
                }
            }

            is OverlayEvent.SetJournalContent -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        journalContent = event.content
                    )
                }
            }

            is OverlayEvent.ShowJournalPopup -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        journalPopup = true
                    )
                }
            }

            is OverlayEvent.HideJournalPopup -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        journalPopup = false
                    )
                }
            }

            is OverlayEvent.SaveJournal -> {
                saveJournal()
            }

            is OverlayEvent.EnterBlockedApp -> {
                enterApp()
            }

            is OverlayEvent.NotEnterApp -> {
                // Go back to the phone's home screen
                returnHome()
            }
            is OverlayEvent.ShowBottomSheet -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        showBottomSheet = true
                    )
                }
            }

            is OverlayEvent.HideBottomSheet -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        showBottomSheet = false
                    )
                }
            }

            is OverlayEvent.UpdateAccessLog -> {
                updateAccessLog()
            }

            is OverlayEvent.SelectReason -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        selectedReason = event.reason
                    )
                }
            }

        }
    }

    private fun updateAccessLog() {
        val reason = _uiState.value.selectedReason
        if (reason.isEmpty()) return
        viewModelScope.launch {
            usageRepo.updateUsageReason(logId = logId, reason = reason)
            _uiState.update { currentState ->
                currentState.copy(
                    selectedReason = "",
                    showBottomSheet = false
                )
            }
        }
    }

    private fun saveJournal() {
        val title = _uiState.value.journalTitle
        val content = _uiState.value.journalContent
        if (title.isEmpty() || content.isEmpty()) return
        viewModelScope.launch {
            journalRepo.insertJournal(JournalEntity(title, content))
            _uiState.update { currentState ->
                currentState.copy(
                    journalPopup = false,
                    journalTitle = "",
                    journalContent = ""
                )
            }
        }
//        returnHome()
    }

    private fun returnHome() {
        // Go back to the phone's home screen
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }

    // Method to enter the blocked app
    private fun enterApp() {
        // Make allowsAppOpen in the service to true
        allowsAppOpen.value = true
        // Open the previous app
        val intent = context.packageManager.getLaunchIntentForPackage(currForegroundApp)
        context.startActivity(intent)
    }
}