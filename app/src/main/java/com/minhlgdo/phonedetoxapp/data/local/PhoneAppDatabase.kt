package com.minhlgdo.phonedetoxapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.minhlgdo.phonedetoxapp.data.local.dao.AppUsageDao
import com.minhlgdo.phonedetoxapp.data.local.dao.BlockedAppDao
import com.minhlgdo.phonedetoxapp.data.local.dao.JournalDao
import com.minhlgdo.phonedetoxapp.data.local.model.AppUsageEntity
import com.minhlgdo.phonedetoxapp.data.local.model.BlockedAppEntity
import com.minhlgdo.phonedetoxapp.data.local.model.JournalEntity
import com.minhlgdo.phonedetoxapp.utils.Converters

@Database(
    entities = [BlockedAppEntity::class, AppUsageEntity::class, JournalEntity::class],
    version = 2,
)
@TypeConverters(Converters::class)
abstract class PhoneAppDatabase() : RoomDatabase() {
    abstract val appDao: BlockedAppDao
    abstract val usageDao: AppUsageDao
    abstract val journalDao: JournalDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time
        @Volatile
        private var instance: PhoneAppDatabase? = null

        fun getInstance(context: Context): PhoneAppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): PhoneAppDatabase {
            return Room.databaseBuilder(context, PhoneAppDatabase::class.java, "detox.db")
                .addMigrations(MIGRATION_1_2)
                .build()

        }

        // Migration from version 1 to version 2
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // create new journal table
                db.execSQL("CREATE TABLE journal (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, time TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP, title TEXT NOT NULL, content TEXT NOT NULL)")
//                database.execSQL("ALTER TABLE appusageentity ADD COLUMN time TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP")
            }
        }
    }
}