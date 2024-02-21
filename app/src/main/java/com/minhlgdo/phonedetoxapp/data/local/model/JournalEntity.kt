package com.minhlgdo.phonedetoxapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "journal")
data class JournalEntity (
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val content: String,
    val time: Date = Date()
) {
    constructor(title: String, content: String): this(0, title, content)
}