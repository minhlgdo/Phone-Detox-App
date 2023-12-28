package com.minhlgdo.phonedetoxapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.minhlgdo.phonedetoxapp.ui.theme.PhoneDetoxAppTheme

@Composable
fun MainApp(viewModel: MainViewModel) {
    PhoneDetoxAppTheme {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background),
        ) {
            Button(onClick = { viewModel.selectApps() }) {
                Text(text = "Select apps")
            }
        }
    }
}