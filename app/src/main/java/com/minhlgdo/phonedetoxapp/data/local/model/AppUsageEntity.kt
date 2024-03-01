package com.minhlgdo.phonedetoxapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "usage_table")
data class AppUsageEntity (
    @PrimaryKey(autoGenerate = true) val id: Long,
    var packageName: String,
    val reason: String? = null,
    val time: Date = Date()
) {
    constructor(packageName: String): this(0, packageName)
//    constructor(id: Int, packageName: String, reason: String, time: Date): this(id, packageName, reason, time)
}
