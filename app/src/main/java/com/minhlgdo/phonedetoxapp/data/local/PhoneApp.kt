package com.minhlgdo.phonedetoxapp.data.local

import android.graphics.drawable.Drawable
import com.minhlgdo.phonedetoxapp.data.local.model.BlockedAppEntity

data class PhoneApp(
    private val name: String,
    private val packageName: String,
    private val icon: Drawable?,
    private var isBlocked: Boolean = false
) {

    fun getName(): String {
        return name
    }

    fun getPackageName(): String {
        return packageName
    }

    fun getIcon(): Drawable? {
        return icon
    }

    fun isBlocked(): Boolean {
        return isBlocked
    }

    fun setBlocked(blocked: Boolean) {
        isBlocked = blocked
    }

    fun toEntity(): BlockedAppEntity {
        return BlockedAppEntity(name, packageName, isBlocked)
    }
}