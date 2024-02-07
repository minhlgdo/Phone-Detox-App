package com.minhlgdo.phonedetoxapp

import android.app.Service
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.room.Room
import com.minhlgdo.phonedetoxapp.data.local.PhoneAppDatabase
import com.minhlgdo.phonedetoxapp.data.repository.PhoneAppRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

/*
Monitoring the foreground app and appearing a full screen overlay if the foreground app is in the blocked apps list
 */
@AndroidEntryPoint
class AppMonitoringService : Service() {
    @Inject lateinit var phoneAppRepository: PhoneAppRepository

    private lateinit var currForegroundApp: String
    private var blockedApps : List<String>? = null
    private var job: Job? = null

    override fun onCreate() {
        super.onCreate()

        // Initialize and start the timer
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                checkForegroundApp()
            }
        }, 0, 2000) // Check every second (adjust as needed)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
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
                if (event.packageName != currForegroundApp) {
                    println("Foreground app changed: ${event.packageName}")
                    currForegroundApp = event.packageName

                    blockedApps?.let {
                        if (isBlockedApp()) {
                            showOverlay()
                        }
                    }
                }

            }
        }
    }

    private fun isBlockedApp(): Boolean {
        return blockedApps!!.contains(currForegroundApp)
    }

    private fun showOverlay() {

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
        }

        return START_STICKY
    }
}