package com.minhlgdo.phonedetoxapp.data.local.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dagger.Provides
import java.sql.Timestamp
import java.text.DateFormat
import java.time.format.DateTimeFormatter
import java.util.Date

@Entity(tableName = "usage_table")
data class AppUsageEntity (
    @PrimaryKey(autoGenerate = true) val id: Int,
    var packageName: String,
    val time: Date = Date()
) {
    constructor(packageName: String): this(0, packageName)
}
