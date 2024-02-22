package com.minhlgdo.phonedetoxapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "usage_table")
data class AppUsageEntity (
    @PrimaryKey(autoGenerate = true) val id: Int,
    var packageName: String,
    val time: Date = Date()
) {
    constructor(packageName: String): this(0, packageName)
}
