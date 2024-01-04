package com.minhlgdo.phonedetoxapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Checkbox
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.minhlgdo.phonedetoxapp.data.PhoneApp
import com.minhlgdo.phonedetoxapp.ui.theme.PhoneDetoxAppTheme
import kotlinx.coroutines.launch

class SelectAppsActivity : ComponentActivity() {
    private val viewModel by viewModels<SelectAppsViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PhoneDetoxAppTheme {
                var apps by remember { mutableStateOf(emptyList<PhoneApp>()) }
                val snackbarHostState = remember { SnackbarHostState() }

                LaunchedEffect(true) {
                    apps = viewModel.getPhoneApps()
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
                                IconButton(onClick = { finish() }) {
                                    Icon(Icons.Filled.ArrowBack, "Go back to main screen")
                                }
                            }
                        )
                    },
                    floatingActionButton = {
                        ExtendedFloatingActionButton(
                            text = { Text(text = "Save blocked apps") },
                            onClick = {
                                lifecycleScope.launch {
                                    viewModel.saveBlockedApps()
                                    // Snackbar to notify user that the apps are saved
                                    snackbarHostState.showSnackbar(
                                        message = "Saved",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            },
                            icon = { Icon(Icons.Filled.Done, "Done") },
                            shape = MaterialTheme.shapes.medium
                        )
                    },
                    content = { paddingValues ->
                        if (viewModel.isAppLoaded) {
                            AppList(apps = apps, padding = paddingValues)
                        } else {
                            // a circular loading indicator
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(paddingValues)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                    }
                )

            }
        }
    }

    @Composable
    fun AppList(apps: List<PhoneApp>, padding: PaddingValues) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(padding)
        ) {
            LazyColumn(
                content = {
                    items(apps.size) { index ->
                        val app = apps[index]
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            val isChecked = remember { mutableStateOf(app.isBlocked()) }
                            // display app icon, name and checkbox
                            Image(
                                painter = rememberDrawablePainter(drawable = app.getIcon()),
                                contentDescription = "App icon",
                                modifier = Modifier
                                    .width(64.dp)
                                    .padding(8.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = app.getName(),
                                softWrap = true,
                                modifier = Modifier.widthIn(max = 250.dp)
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Checkbox(checked = isChecked.value,
                                onCheckedChange = {
                                    isChecked.value = it
                                    viewModel.onAppSelected(app, it)
                                })
                        }
                        // add space at the bottom of the last item
                        if (index == apps.size - 1) {
                            Spacer(modifier = Modifier.height(60.dp))
                        }
                    }
                }, verticalArrangement = Arrangement.spacedBy(2.dp)
            )
        }
    }
}
