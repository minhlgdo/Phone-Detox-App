package com.minhlgdo.phonedetoxapp

import android.app.AppOpsManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import com.minhlgdo.phonedetoxapp.ui.presentation.home.MainScreenView
import com.minhlgdo.phonedetoxapp.ui.theme.PhoneDetoxAppTheme

class MainActivity : ComponentActivity() {
    private var usagePermission by mutableStateOf(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Ask for permission to show notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.FOREGROUND_SERVICE,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ),
                0
            )
        }

        // Start background service
        startService(Intent(this, AppMonitoringService::class.java))

        setContent {
            PhoneDetoxAppTheme {
                LaunchedEffect(Unit) {
                    usagePermission = hasUsageStatsPermission()
                }
                MainScreenView(usagePermission)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Check if the usage access permission has been granted
        usagePermission = hasUsageStatsPermission()
    }


    /*
    Check if the app has the usage stats permission (special permissions => cannot use AndroidX's Permission API)
     */
    private fun hasUsageStatsPermission(): Boolean {
        val appOps = getSystemService(APP_OPS_SERVICE) as AppOpsManager
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), packageName
            )
        } else {
            appOps.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), packageName
            )
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }
}


