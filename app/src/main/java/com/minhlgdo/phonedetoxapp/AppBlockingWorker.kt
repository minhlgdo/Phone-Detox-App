package com.minhlgdo.phonedetoxapp

import android.app.ActivityManager
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.runBlocking

class AppBlockingWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    /*
    * Blocking the foreground app by showing a full screen overlay
     */
    override fun doWork(): Result {
        // TODO: Check if the foreground app is in the selected apps list
        // TODO: Implement showing a full screen overlay
        return Result.success()
    }


}