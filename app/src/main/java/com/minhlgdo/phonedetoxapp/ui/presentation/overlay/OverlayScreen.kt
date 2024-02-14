package com.minhlgdo.phonedetoxapp.ui.presentation.overlay

import android.content.Context
import android.view.View
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.delay

@Composable
fun OverlayScreen() {
    // The first one second is black, then it will be white and the text will be shown
    var visible by remember { mutableStateOf(true) }
    val backgroundColor = remember { Animatable(Color.Black) }

    LaunchedEffect(visible) {
        if (visible) {
            // Animate background color to white
            backgroundColor.animateTo(Color.Red, tween(durationMillis = 5000, easing = LinearOutSlowInEasing))
            // Wait for one second
            delay(1000)
            // Set visibility to false after one second
            visible = false
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(color = backgroundColor.value)
    ) {
        // Content to be overlaid can be added here
        if (!visible) {
            Text(text = "Overlay Screen")
        } else {
            Text(text = "Visible Overlay Screen")
        }

    }
}