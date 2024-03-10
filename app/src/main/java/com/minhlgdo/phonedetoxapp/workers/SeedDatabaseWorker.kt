package com.minhlgdo.phonedetoxapp.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.minhlgdo.phonedetoxapp.data.local.AppDatabase
import com.minhlgdo.phonedetoxapp.data.local.model.ReasonEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// A worker to seed the database with some initial data from json file in assets
class SeedDatabaseWorker(
    context: Context, workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    // withContext is used to switch to the IO dispatcher to perform the database operations
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            applicationContext.assets.open("reasons.json").use { inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->
                    val reasonType = object : TypeToken<List<ReasonEntity>>() {}.type
                    val reasonList: List<ReasonEntity> = Gson().fromJson(jsonReader, reasonType)

                    val database = AppDatabase.getInstance(applicationContext)
                    database.reasonDao().upsertAllReasons(reasonList)

                    println("Seeding database successfully completed")
                    Result.success()
                }
            }
        } catch (ex: Exception) {
            println("Error seeding database: ${ex.message}")
            Result.failure()
        }
    }
}