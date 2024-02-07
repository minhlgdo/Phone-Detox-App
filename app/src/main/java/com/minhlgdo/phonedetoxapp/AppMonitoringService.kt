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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import java.util.Timer
import java.util.TimerTask

/*
Monitoring the foreground app and appearing a full screen overlay if the foreground app is in the blocked apps list
 */
class AppMonitoringService : Service() {
    private val handler = Handler(Looper.getMainLooper())
//    private lateinit var prevForegroundApp: String
//    private lateinit var currForegroundApp: String
//    private lateinit var appDb : PhoneAppDatabase
//    private lateinit var blockedApps : List<String>

    override fun onCreate() {
        super.onCreate()
//        appDb = Room.databaseBuilder(applicationContext, PhoneAppDatabase::class.java, "blocked_app_db").build()
//        runBlocking {
//            blockedApps = appDb.dao.getBlockedApps().map { it.packageName }
//        }
        // Initialize and start the timer
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                checkForegroundApp()
            }
        }, 0, 1000) // Check every second (adjust as needed)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun checkForegroundApp() {
        val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val currTime = System.currentTimeMillis()
        val usageEvents = usageStatsManager.queryEvents(currTime - 1000 * 10, currTime)

        while (usageEvents.hasNextEvent()) {
            val event = UsageEvents.Event()
            usageEvents.getNextEvent(event)
            if (event.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                println(event.packageName)
//                if (isBlockedApp()) {
//                    handler.post {
//                        Toast.makeText(this, "Blocked app: $currForegroundApp", Toast.LENGTH_SHORT).show()
//                    }
//                }
            }
        }
    }

//    private fun isBlockedApp(): Boolean {
//        return blockedApps.contains(currForegroundApp)
//    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = NotificationCompat.Builder(this, "default")
            .setContentTitle("Phone Detox")
            .setContentText("App monitoring service is running")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
        startForeground(1, notification)

        return START_STICKY
    }
}