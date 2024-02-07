package com.minhlgdo.phonedetoxapp.ui.presentation.select_apps

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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.minhlgdo.phonedetoxapp.view_model.SelectAppsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectAppsScreen(
    viewModel: SelectAppsViewModel,
    onBack: () -> Unit
) {
//    var apps by remember { mutableStateOf(emptyList<PhoneApp>()) }
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

//    // Load apps from the phone when the screen is created
//    LaunchedEffect(true) {
//        apps = viewModel.getPhoneApps(context)
//    }

    Scaffold (
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
//                    lifecycleScope.launch {
//                        viewModel.saveBlockedApps()
//                        // Snackbar to notify user that the apps are saved
//                        snackbarHostState.showSnackbar(
//                            message = "Saved",
//                            duration = SnackbarDuration.Short
//                        )
//                    }
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
