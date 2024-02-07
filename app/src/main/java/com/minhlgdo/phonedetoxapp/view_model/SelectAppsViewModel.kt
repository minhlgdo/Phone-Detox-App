package com.minhlgdo.phonedetoxapp.view_model

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
import com.minhlgdo.phonedetoxapp.data.local.PhoneAppEntity
import com.minhlgdo.phonedetoxapp.data.repository.PhoneAppRepository
import com.minhlgdo.phonedetoxapp.ui.state.SelectAppsUiState
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

//class SelectAppsViewModel(application: Application) : AndroidViewModel(application) {
//
//    private val db =
//        Room.databaseBuilder(application, PhoneAppDatabase::class.java, "blocked_app_db").build()
//    private val dao = db.dao
//    private val _selectedApps: MutableList<PhoneApp> = mutableListOf()
//    private val _unselectedApps: MutableList<PhoneApp> = mutableListOf()
//    var isAppLoaded by mutableStateOf(false)
//
//    // Exclude system apps
//    suspend fun getPhoneApps(): List<PhoneApp> {
//        val appList = mutableListOf<PhoneApp>()
//        val selectedApps = dao.getBlockedApps()
//
//        val pm = getApplication<Application>().packageManager
//        val mainIntent = Intent(Intent.ACTION_MAIN, null)
//        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
//
//        val resolvedInfos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            pm.queryIntentActivities(
//                mainIntent,
//                PackageManager.ResolveInfoFlags.of(0L)
//            )
//        } else {
//            pm.queryIntentActivities(mainIntent, 0)
//        }
//
//        // Get the name of each app from resolvedInfos
//        for (resolveInfo in resolvedInfos) {
//            val resources = pm.getResourcesForApplication(resolveInfo.activityInfo.applicationInfo)
//            val appName = if (resolveInfo.activityInfo.labelRes != 0) {
//                // getting proper label from resources
//                resources.getString(resolveInfo.activityInfo.labelRes)
//            } else {
//                // getting it out of app info - equivalent to context.packageManager.getApplicationInfo
//                resolveInfo.activityInfo.applicationInfo.loadLabel(pm).toString()
//            }
//            val packageName = resolveInfo.activityInfo.packageName
//            val iconDrawable = resolveInfo.activityInfo.loadIcon(pm)
//
//            // Check if the app is in selectedApps
//            var isBlocked = false
//            for (app in selectedApps) {
//                if (app.packageName == packageName) {
//                    isBlocked = true
//                    break
//                }
//            }
//
//            val app = PhoneApp(appName, packageName, iconDrawable, isBlocked)
//            if (isBlocked) {
//                _selectedApps.add(app)
//            }
//            appList.add(app)
//        }
//
//        // Sort the list of apps alphabetically
//        appList.sortBy { it.getName() }
//        Log.d("SelectAppViewModel", "getPhoneApps: ${appList.size}")
//        isAppLoaded = true
//        return appList
//    }
//
//    fun onAppSelected(app: PhoneApp, isSelected: Boolean) {
//        if (isSelected) {
//            _unselectedApps.remove(app)
//            _selectedApps.add(app)
//        } else {
//            _selectedApps.remove(app)
//            _unselectedApps.add(app)
//        }
//    }
//
//    suspend fun saveBlockedApps() {
//        for (app in _selectedApps) {
//            val appEntity = app.toEntity()
//            dao.insertBlockedApps(appEntity)
//        }
//        // Remove unselected apps
//        for (app in _unselectedApps) {
//            val appEntity = app.toEntity()
//            dao.deleteBlockedApps(appEntity)
//        }
//        // Print the list of blocked apps
//        val blockedApps = dao.getBlockedApps()
//        for (app in blockedApps) {
//            Log.d("SelectAppViewModel", "Blocked app: ${app.name}")
//        }
//    }
//}

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

    private var blockedApps: List<PhoneAppEntity> by mutableStateOf(emptyList())


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
        }
    }

    fun onAppSelected(app: PhoneApp, isSelected: Boolean) {
        if (isSelected) {
            _uiState.update { currentState ->
                currentState.copy(
                    selected = currentState.selected + app,
                    unselected = currentState.unselected - app
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
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