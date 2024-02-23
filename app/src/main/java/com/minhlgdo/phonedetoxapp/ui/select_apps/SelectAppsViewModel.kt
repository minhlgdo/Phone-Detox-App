package com.minhlgdo.phonedetoxapp.ui.select_apps

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minhlgdo.phonedetoxapp.data.local.PhoneApp
import com.minhlgdo.phonedetoxapp.data.local.model.BlockedAppEntity
import com.minhlgdo.phonedetoxapp.data.repository.PhoneAppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@HiltViewModel
class SelectAppsViewModel @Inject constructor(
    private val repository: PhoneAppRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    // UI state (selected apps)
    private val _uiState =
        MutableStateFlow(SelectAppsUiState()) // can be used to observe the state of the UI in the ViewModel only
    val uiState: StateFlow<SelectAppsUiState> = _uiState.asStateFlow()
    var phoneApps: List<PhoneApp> by mutableStateOf(emptyList())
        private set

    private var blockedApps: List<BlockedAppEntity> by mutableStateOf(emptyList())


    init {
        runBlocking {
            blockedApps = repository.getBlockedApps().first()
        }
        phoneApps = getPhoneApps(context)
        _uiState.update { currentState -> currentState.copy(loadedApp = true) }
    }

    fun onSaveApps() {
        viewModelScope.launch {
            for (app in uiState.value.selected) {
                repository.insertBlockedApps(app.toEntity())
            }
            for (app in uiState.value.unselected) {
                repository.deleteBlockedApps(app.toEntity())
            }
            _uiState.update { currentState -> currentState.copy(
                saved = true,
                selected = emptyList(),
                unselected = emptyList()
            ) }
        }
    }

    fun onAppSelected(app: PhoneApp, isSelected: Boolean) {
        if (isSelected) {
            _uiState.update { currentState ->
                currentState.copy(
                    saved = false,
                    selected = currentState.selected + app,
                    unselected = currentState.unselected - app
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    saved = false,
                    selected = currentState.selected - app,
                    unselected = currentState.unselected + app
                )
            }
        }
    }

    private fun getPhoneApps(context: Context): List<PhoneApp> {
        val appList = mutableListOf<PhoneApp>()

        val pm = context.packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)

        val resolvedInfos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pm.queryIntentActivities(
                mainIntent,
                PackageManager.ResolveInfoFlags.of(0L)
            )
        } else {
            pm.queryIntentActivities(mainIntent, 0)
        }

        // Get the name of each app from resolvedInfos
        for (resolveInfo in resolvedInfos) {
            val resources = pm.getResourcesForApplication(resolveInfo.activityInfo.applicationInfo)
            val appName = if (resolveInfo.activityInfo.labelRes != 0) {
                // getting proper label from resources
                resources.getString(resolveInfo.activityInfo.labelRes)
            } else {
                // getting it out of app info - equivalent to context.packageManager.getApplicationInfo
                resolveInfo.activityInfo.applicationInfo.loadLabel(pm).toString()
            }
            val packageName = resolveInfo.activityInfo.packageName
            val iconDrawable = resolveInfo.activityInfo.loadIcon(pm)

            // Check if the app is in selectedApps
            var isBlocked = false
            for (app in blockedApps) {
                if (app.packageName == packageName) {
                    isBlocked = true
                    break
                }
            }

            val app = PhoneApp(appName, packageName, iconDrawable, isBlocked)
            appList.add(app)
        }

        // Sort the list of apps alphabetically
        appList.sortBy { it.getName() }

        return appList
    }


}