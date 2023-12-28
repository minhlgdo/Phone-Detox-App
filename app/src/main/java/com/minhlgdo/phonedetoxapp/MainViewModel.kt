package com.minhlgdo.phonedetoxapp

import android.app.Application
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel

class MainViewModel(application: Application) : AndroidViewModel(application) {

    fun selectApps() {
        // Get the phone's list of apps
        val appList = getPhoneApps()

        // Display the list of apps

        // Allow the user to select apps
        // Save the list of apps
    }

    // Exclude system apps
    fun getPhoneApps() : List<PhoneApp> {
        val appList = mutableListOf<PhoneApp>()

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
            val resources =  pm.getResourcesForApplication(resolveInfo.activityInfo.applicationInfo)
            val appName = if (resolveInfo.activityInfo.labelRes != 0) {
                // getting proper label from resources
                resources.getString(resolveInfo.activityInfo.labelRes)
            } else {
                // getting it out of app info - equivalent to context.packageManager.getApplicationInfo
                resolveInfo.activityInfo.applicationInfo.loadLabel(pm).toString()
            }
            val packageName = resolveInfo.activityInfo.packageName
            val iconDrawable = resolveInfo.activityInfo.loadIcon(pm)
            Log.d("MainViewModel debug", "appName: $appName")

            val app = PhoneApp(appName, packageName, iconDrawable)
            appList.add(app)
        }
        return appList
    }
}