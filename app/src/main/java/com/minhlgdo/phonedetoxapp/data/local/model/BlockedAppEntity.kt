package com.minhlgdo.phonedetoxapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BlockedAppEntity(
    val name: String,
    @PrimaryKey val packageName: String,
    val isBlocked: Boolean = true
)