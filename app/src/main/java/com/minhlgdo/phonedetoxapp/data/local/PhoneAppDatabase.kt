package com.minhlgdo.phonedetoxapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.minhlgdo.phonedetoxapp.data.local.dao.AppUsageDao
import com.minhlgdo.phonedetoxapp.data.local.dao.BlockedAppDao
import com.minhlgdo.phonedetoxapp.data.local.model.AppUsageEntity
import com.minhlgdo.phonedetoxapp.data.local.model.BlockedAppEntity
import com.minhlgdo.phonedetoxapp.utils.Converters

@Database(
    entities = [BlockedAppEntity::class, AppUsageEntity::class],
    version = 1,
)
@TypeConverters(Converters::class)
abstract class PhoneAppDatabase() : RoomDatabase() {
    abstract val appDao: BlockedAppDao
    abstract val usageDao: AppUsageDao

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
                .build()

        }
    }
}