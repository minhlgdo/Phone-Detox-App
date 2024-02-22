package com.minhlgdo.phonedetoxapp.ui.select_apps

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.minhlgdo.phonedetoxapp.viewmodels.SelectAppsViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectAppsScreen(
    viewModel: SelectAppsViewModel,
    onBack: () -> Unit
) {
//    var apps by remember { mutableStateOf(emptyList<PhoneApp>()) }
    val snackbarHostState = remember { SnackbarHostState() }
    var prevSavedState by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        // Show a snackbar when the apps are saved
        viewModel.uiState.collect {
            if (it.saved && !prevSavedState) {
                snackbarHostState.showSnackbar(
                    message = "Saved",
                    duration = SnackbarDuration.Short,
                    withDismissAction = true
                )
                delay(750)
            }
            prevSavedState = it.saved
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                title = { Text(text = "Select apps") },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.Filled.ArrowBack, "Go back to main screen")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = "Save blocked apps") },
                onClick = {
                    viewModel.onSaveApps()
                },
                icon = { Icon(Icons.Filled.Done, "Done") },
                shape = MaterialTheme.shapes.medium
            )
        }
    ) {
        if (uiState.loadedApp) {
            AppListContent(apps = viewModel.phoneApps, viewModel = viewModel)
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }

}
