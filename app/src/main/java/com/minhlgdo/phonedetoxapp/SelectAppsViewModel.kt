package com.minhlgdo.phonedetoxapp

import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.room.Room
import com.minhlgdo.phonedetoxapp.data.PhoneApp
import com.minhlgdo.phonedetoxapp.data.PhoneAppDatabase
import com.minhlgdo.phonedetoxapp.data.PhoneAppEntity

class SelectAppsViewModel(application: Application) : AndroidViewModel(application) {

    private val db =
        Room.databaseBuilder(application, PhoneAppDatabase::class.java, "blocked_app_db").build()
    private val dao = db.dao
    private val _selectedApps: MutableList<PhoneApp> = mutableListOf()
    private val _unselectedApps: MutableList<PhoneApp> = mutableListOf()
    var isAppLoaded by mutableStateOf(false)

    // Exclude system apps
    suspend fun getPhoneApps(): List<PhoneApp> {
        val appList = mutableListOf<PhoneApp>()
        val selectedApps = dao.getBlockedApps()

        val pm = getApplication<Application>().packageManager
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
            for (app in selectedApps) {
                if (app.packageName == packageName) {
                    isBlocked = true
                    break
                }
            }

            val app = PhoneApp(appName, packageName, iconDrawable, isBlocked)
            if (isBlocked) {
                _selectedApps.add(app)
            }
            appList.add(app)
        }

        // Sort the list of apps alphabetically
        appList.sortBy { it.getName() }
        Log.d("SelectAppViewModel", "getPhoneApps: ${appList.size}")
        isAppLoaded = true
        return appList
    }

    fun onAppSelected(app: PhoneApp, isSelected: Boolean) {
        if (isSelected) {
            _unselectedApps.remove(app)
            _selectedApps.add(app)
        } else {
            _selectedApps.remove(app)
            _unselectedApps.add(app)
        }
    }

    suspend fun saveBlockedApps() {
        for (app in _selectedApps) {
            val appEntity = app.toEntity()
            dao.insertBlockedApps(appEntity)
        }
        // Remove unselected apps
        for (app in _unselectedApps) {
            val appEntity = app.toEntity()
            dao.deleteBlockedApps(appEntity)
        }
        // Print the list of blocked apps
        val blockedApps = dao.getBlockedApps()
        for (app in blockedApps) {
            Log.d("SelectAppViewModel", "Blocked app: ${app.name}")
        }
    }
}