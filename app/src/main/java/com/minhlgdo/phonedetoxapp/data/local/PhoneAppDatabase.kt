package com.minhlgdo.phonedetoxapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.minhlgdo.phonedetoxapp.data.local.dao.AppUsageDao
import com.minhlgdo.phonedetoxapp.data.local.dao.BlockedAppDao
import com.minhlgdo.phonedetoxapp.data.local.dao.JournalDao
import com.minhlgdo.phonedetoxapp.data.local.dao.ReasonDao
import com.minhlgdo.phonedetoxapp.data.local.model.AppUsageEntity
import com.minhlgdo.phonedetoxapp.data.local.model.BlockedAppEntity
import com.minhlgdo.phonedetoxapp.data.local.model.JournalEntity
import com.minhlgdo.phonedetoxapp.data.local.model.ReasonEntity
import com.minhlgdo.phonedetoxapp.utils.Converters
import com.minhlgdo.phonedetoxapp.workers.SeedDatabaseWorker

@Database(
    entities = [BlockedAppEntity::class, AppUsageEntity::class, JournalEntity::class, ReasonEntity::class],
    version = 3,
)
@TypeConverters(Converters::class)
abstract class PhoneAppDatabase() : RoomDatabase() {
    abstract fun appDao(): BlockedAppDao
    abstract fun usageDao(): AppUsageDao
    abstract fun journalDao(): JournalDao
    abstract fun reasonDao(): ReasonDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time
        @Volatile
        private var instance: PhoneAppDatabase? = null

        fun getInstance(context: Context): PhoneAppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): PhoneAppDatabase {
            return Room.databaseBuilder(context, PhoneAppDatabase::class.java, "detox.db")
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // pre-populate the database after onCreate
                        val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
                        WorkManager.getInstance(context).enqueue(request)

                    }
                }).build()

        }

        // Migration from version 1 to version 2
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // create new journal table
                db.execSQL("CREATE TABLE journal (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, time TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP, title TEXT NOT NULL, content TEXT NOT NULL)")
            }
        }

        // Migration from version 2 to version 3
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // add new column reason nullable to usage_table
                db.execSQL("ALTER TABLE usage_table ADD COLUMN reason TEXT")
            }
        }
    }
}