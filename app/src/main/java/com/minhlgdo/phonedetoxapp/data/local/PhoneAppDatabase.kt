package com.minhlgdo.phonedetoxapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [PhoneAppEntity::class],
    version = 1,
)
abstract class PhoneAppDatabase() : RoomDatabase() {
    abstract val dao: PhoneAppDao

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