package com.minhlgdo.phonedetoxapp

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.hilt.navigation.compose.hiltViewModel
import com.minhlgdo.phonedetoxapp.ui.overlay.OverlayScreen
import com.minhlgdo.phonedetoxapp.ui.theme.PhoneDetoxAppTheme
import com.minhlgdo.phonedetoxapp.viewmodels.OverlayViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OverlayActivity : ComponentActivity() {
    // variable to store instance of the service class
    private var mService: AppMonitoringService? = null
    // Boolean to check if our activity is bound to service or not
    private var mBound: Boolean = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as AppMonitoringService.ServiceBinder
            mService = binder.getService()
            mBound = true
        }
        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel : OverlayViewModel by viewModels()

        setContent {
            PhoneDetoxAppTheme {
                OverlayScreen(viewModel = viewModel)
            }
        }

        // observe viewModel.allowAppOpen. if value changes, send data to service
        viewModel.allowsAppOpen.observe(this) {
            mService?.setAppOpen(it)
        }
    }

    override fun onStart() {
        super.onStart()
        // Bind to AppMonitoringService
        Intent(this, AppMonitoringService::class.java).also { intent ->
            bindService(intent, connection, BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        if (mBound) {
            unbindService(connection)
            mBound = false
        }
    }


    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // go back to the home screen
        val startMain = Intent(Intent.ACTION_MAIN)
        startMain.addCategory(Intent.CATEGORY_HOME)
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(startMain)
    }


}