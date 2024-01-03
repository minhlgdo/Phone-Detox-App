package com.minhlgdo.phonedetoxapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PhoneAppEntity(
    val name: String,
    @PrimaryKey val packageName: String,
    val isBlocked: Boolean = false
)