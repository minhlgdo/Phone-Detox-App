package com.minhlgdo.phonedetoxapp.activities

import android.app.AppOpsManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.minhlgdo.phonedetoxapp.AppMonitoringService
import com.minhlgdo.phonedetoxapp.data.local.ServiceManager
import com.minhlgdo.phonedetoxapp.ui.landing.MainScreenView
import com.minhlgdo.phonedetoxapp.ui.theme.PhoneDetoxAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var serviceManager: ServiceManager
    private var usagePermission by mutableStateOf(false)
    private var drawOverAppPermission by mutableStateOf(false)

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

        setContent {
            PhoneDetoxAppTheme {
                LaunchedEffect(Unit) {
                    usagePermission = hasUsageStatsPermission()
                    drawOverAppPermission = hasDrawOverAppPermission()
                }
                MainScreenView(usagePermission, drawOverAppPermission)
            }
        }

        // Start the service if it's not running
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                serviceManager.getIsRunning.collect { isRunning ->
                    if (!isRunning) {
                        startService(Intent(this@MainActivity, AppMonitoringService::class.java))
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Check if the usage access permission has been granted
        usagePermission = hasUsageStatsPermission()
        drawOverAppPermission = hasDrawOverAppPermission()
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

    // Check if the app has the overlay permission
    private fun hasDrawOverAppPermission(): Boolean {
        return Settings.canDrawOverlays(this)
    }
}


