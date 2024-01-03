package com.minhlgdo.phonedetoxapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PhoneAppEntity::class],
    version = 1,
)
abstract class PhoneAppDatabase(): RoomDatabase() {
    abstract val dao: PhoneAppDao
}