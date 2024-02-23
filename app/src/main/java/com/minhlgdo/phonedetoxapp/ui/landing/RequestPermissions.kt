package com.minhlgdo.phonedetoxapp.ui.landing

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestPermissionsUI(
    context: Context,
    hasUsagePermission: Boolean,
    hasPopupPermission: Boolean
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Phone Detox App") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }
    ) {
        OpenSettingsContent(it, hasUsagePermission, hasPopupPermission,
            onUsageSettings = {
                val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                val uri: Uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                context.startActivity(intent)
            },
            onPopupSettings = {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                val uri: Uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                context.startActivity(intent)
            })

    }

}

/**
 * The open settings composable is used to display a message to the user changing the permissions
 */
@Composable
fun OpenSettingsContent(
    paddingValues: PaddingValues,
    usagePermission: Boolean,
    drawOverTopPermission: Boolean,
    onUsageSettings: () -> Unit,
    onPopupSettings: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Please open app settings to grant necessary permissions.",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onUsageSettings() }, enabled = !usagePermission) {
            if (usagePermission) {
                Text("Usage permission granted".uppercase(Locale.getDefault()))
            } else {
                Text("Usage permission settings".uppercase(Locale.getDefault()))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onPopupSettings() }, enabled = !drawOverTopPermission) {
            if (drawOverTopPermission) {
                Text("Draw over top permission granted".uppercase(Locale.getDefault()))
            } else {
                Text("Draw over top permission settings".uppercase(Locale.getDefault()))
            }

        }
    }
}