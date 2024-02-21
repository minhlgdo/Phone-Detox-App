package com.minhlgdo.phonedetoxapp.utils

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Converters {
    @TypeConverter
    fun fromDate(value: Date): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
        return formatter.format(value)
    }

    @TypeConverter
    fun toDate(value: String): Date {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
        return formatter.parse(value)!!
    }
}