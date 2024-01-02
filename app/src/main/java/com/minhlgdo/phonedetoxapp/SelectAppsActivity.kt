package com.minhlgdo.phonedetoxapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.minhlgdo.phonedetoxapp.ui.theme.PhoneDetoxAppTheme

class SelectAppsActivity : ComponentActivity() {
    private val selectAppViewModel by viewModels<SelectAppViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apps = selectAppViewModel.getPhoneApps()

        setContent {
            PhoneDetoxAppTheme {
                // A surface container using the 'background' color from the theme
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ), title = { Text(text = "Select apps") }, actions = {
                                IconButton(onClick = { /*TODO*/ }) {
                                    Icon(Icons.Filled.Search, "Search apps")
                                }
                            })
                        }, floatingActionButton = {
                            ExtendedFloatingActionButton(
                                text = { Text(text = "Save blocked apps") },
                                onClick = { /*TODO*/ },
                                icon = { Icon(Icons.Filled.Done, "Done") },
                                shape = MaterialTheme.shapes.medium
                            )
                        }, content = { paddingValues ->
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(paddingValues)
                            ) {
                                AppList(apps = apps)
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun AppList(apps: List<PhoneApp>) {
        Box(modifier = Modifier.fillMaxHeight()) {
            LazyColumn(
                content = {
                    items(apps.size) { index ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            val isChecked = remember { mutableStateOf(false) }
                            // insert app icon here
                            Image(
                                painter = rememberDrawablePainter(drawable = apps[index].getIcon()),
                                contentDescription = "App icon",
                                modifier = Modifier
                                    .width(64.dp)
                                    .padding(8.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = apps[index].getName(),
                                softWrap = true,
                                modifier = Modifier.widthIn(max = 250.dp)
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Checkbox(checked = isChecked.value,
                                onCheckedChange = { isChecked.value = it })
                        }
                        if (index == apps.size - 1) {
                            Spacer(modifier = Modifier.height(60.dp))
                        }
                    }
                }, verticalArrangement = Arrangement.spacedBy(2.dp)
            )
        }
    }
}
