package com.minhlgdo.phonedetoxapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// This is the entity class for the usage reasons
@Entity(tableName = "reasons")
data class ReasonEntity(
    @PrimaryKey val id: Int,
    val reason: String,
    val icon: String
)