package com.minhlgdo.phonedetoxapp

import android.app.Service
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.minhlgdo.phonedetoxapp.activities.OverlayActivity
import com.minhlgdo.phonedetoxapp.data.local.ServiceManager
import com.minhlgdo.phonedetoxapp.data.repository.PhoneAppRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

/*
Monitoring the foreground app and appearing a full screen overlay if the foreground app is in the blocked apps list
 */
@AndroidEntryPoint
class AppMonitoringService : Service() {
    @Inject lateinit var phoneAppRepository: PhoneAppRepository
    @Inject lateinit var serviceManager: ServiceManager

    private lateinit var currForegroundApp: String
    private var blockedApps : List<String>? = null
    private var job: Job? = null
    private val binder = ServiceBinder()
    private var allowsAppOpen = false

    override fun onCreate() {
        super.onCreate()

        // Initialize and start the timer
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                checkForegroundApp()
                blockedApps?.let {
                    if (isBlockedApp() && !allowsAppOpen) {
                        showOverlay()
                    }
                }
            }
        }, 0, 1000) // Check every second (adjust as needed)
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    private fun checkForegroundApp() {
        val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val currTime = System.currentTimeMillis()
        val usageEvents = usageStatsManager.queryEvents(currTime - 1000 * 5, currTime)

        while (usageEvents.hasNextEvent()) {
            val event = UsageEvents.Event()
            usageEvents.getNextEvent(event)
            if (event.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                // update currForegroundApp when the foreground app changes
                if (!this::currForegroundApp.isInitialized) {
                    currForegroundApp = event.packageName
                }
//                println("Current foreground app: ${event.packageName}, currForegroundApp: $currForegroundApp")
                if ((event.packageName != currForegroundApp) && (event.packageName != "com.minhlgdo.phonedetoxapp")) {
                    println("Foreground app changed: ${event.packageName}")
                    currForegroundApp = event.packageName
                    allowsAppOpen = false // reset the flag when the foreground app changes
                }

            }
        }
    }

    private fun isBlockedApp(): Boolean {
        return this::currForegroundApp.isInitialized && blockedApps!!.contains(currForegroundApp)
    }

    private fun showOverlay() {
        val intent = Intent(this, OverlayActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
        intent.putExtra("currentAppPackage", currForegroundApp)
        startActivity(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = NotificationCompat.Builder(this, "default")
            .setContentTitle("Phone Detox")
            .setContentText("App monitoring service is running")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
        startForeground(1, notification)

        job = CoroutineScope(Dispatchers.Main).launch {
//            blockedApps = phoneAppRepository.getBlockedApps().first().map { it.packageName }
            phoneAppRepository.getBlockedApps().collect { newData ->
                blockedApps = newData.map { it.packageName }
            }
            serviceManager.setIsRunning(true)
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        CoroutineScope(Dispatchers.Main).launch {
            serviceManager.setIsRunning(false)
        }
        job?.cancel()
    }

    // Binder given to other clients
    inner class ServiceBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        fun getService(): AppMonitoringService = this@AppMonitoringService
    }

    // Public method for clients to modify isBlockedAppOpen
    fun setAppOpen(isOpen: Boolean) {
        allowsAppOpen = isOpen
    }
}