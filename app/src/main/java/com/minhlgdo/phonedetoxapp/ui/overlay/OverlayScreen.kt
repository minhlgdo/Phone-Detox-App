package com.minhlgdo.phonedetoxapp.ui.overlay

import androidx.compose.animation.Animatable
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun OverlayScreen(viewModel: OverlayViewModel) {
    // The first one second is black, then it will be white and the text will be shown
    var visible by remember { mutableStateOf(true) }
    val uiState by viewModel.uiState.collectAsState()
    val backgroundColor = remember { Animatable(Color.Black) }
    val reasons = viewModel.reasons.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        if (visible) {
            // Animate background color to white
            backgroundColor.animateTo(Color.DarkGray, tween(durationMillis = 3000))
            visible = false
        }
    }

    if (uiState.journalPopup) {
        JournalingPopup(viewModel::onEvent, uiState)
    }

    if (uiState.showBottomSheet) {
        ReasonsBottomSheet(
            reasonList = reasons.value,
            onEvent = viewModel::onEvent,
            state = uiState
        )
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
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = it.calculateBottomPadding()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = uiState.accessCount.toString(),
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "attempts of using ${uiState.appName} for today",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(300.dp))
                TextButton(
                    onClick = { viewModel.onEvent(OverlayEvent.ShowJournalPopup) }
                ) {
                    Text(text = "How about journaling instead?")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { viewModel.onEvent(OverlayEvent.NotEnterApp) }
                ) {
                    Text(text = "I don't want to use ${uiState.appName}")
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(
                    onClick = {
                        viewModel.onEvent(OverlayEvent.ShowBottomSheet)
//                        viewModel.onEvent(OverlayEvent.EnterBlockedApp)
                    }
                ) {
                    Text(text = "Continue using the app")
                }

            }
        }

    }
}