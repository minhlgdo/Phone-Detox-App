package com.minhlgdo.phonedetoxapp.ui.presentation.overlay

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.minhlgdo.phonedetoxapp.viewmodels.OverlayViewModel
import kotlinx.coroutines.delay

@Composable
fun OverlayScreen(viewModel: OverlayViewModel) {
    // The first one second is black, then it will be white and the text will be shown
    var visible by remember { mutableStateOf(true) }
    val backgroundColor = remember { Animatable(Color.Black) }

    LaunchedEffect(Unit) {
        if (visible) {
            // Animate background color to white
            backgroundColor.animateTo(Color.DarkGray, tween(durationMillis = 5000))
            // Set visibility to false after one second
            visible = false
        }
    }

    if (visible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor.value),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "It's time to take a break", color = Color.White)
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "You are using a blocked app")
            Spacer(modifier = Modifier.height(200.dp))
            Button(onClick = { viewModel.dismissOverlay() }) {
                Text(text = "Dismiss")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.enterApp() }) {
                Text(text = "Enter the app")
            }

        }
    }
}