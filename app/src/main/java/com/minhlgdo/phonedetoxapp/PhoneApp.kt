package com.minhlgdo.phonedetoxapp

import android.graphics.drawable.Drawable

class PhoneApp(
    private val name: String,
    private val packageName: String,
    private val icon: Drawable
) {

    fun getName(): String {
        return name
    }

    fun getPackageName(): String {
        return packageName
    }

    fun getIcon(): Drawable {
        return icon
    }
}