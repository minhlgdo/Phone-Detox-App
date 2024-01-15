package com.minhlgdo.phonedetoxapp.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun MainScreenView(hasUsagePermission: Boolean) {
    val context = LocalContext.current

    val navController = rememberNavController()

    if (hasUsagePermission) {
        Scaffold(
            bottomBar = { BottomNavigationBar(navController = navController) }
        ) {
            // Content of the screen
            Box(modifier = Modifier.padding(it)) {
                NavigationGraph(navController = navController)
            }
        }
    } else {
        RequestPermissionsUI(context)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RequestPermissionsUI(context: Context) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Phone Detox App") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }
    ) {
        OpenSettingsContent(it) {
//                    permissionState.launchMultiplePermissionRequest()
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val uri: Uri = Uri.fromParts("package", context.packageName, null)
            intent.data = uri
            context.startActivity(intent)
        }
    }

}


@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Statistics,
        BottomNavItem.Journaling
    )
    BottomNavigation(
        modifier = Modifier.height(56.dp),
        backgroundColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { item ->
            val isSelected = item.screenRoute == currentDestination?.route
            BottomNavigationItem(
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.title,
                        modifier = Modifier
                            .width(26.dp)
                            .height(26.dp)
                    )
                },
                label = { Text(text = item.title, fontSize = 9.sp) },
                selected = isSelected,
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.onBackground,
                alwaysShowLabel = false,
                onClick = {
                    navController.navigate(item.screenRoute) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }

}

sealed class BottomNavItem(val title: String, val icon: ImageVector, val screenRoute: String) {
    object Home : BottomNavItem("Home", Icons.Filled.Home, "Home")
    object Statistics : BottomNavItem("Statistics", Icons.Filled.Info, "Statistics")
    object Journaling : BottomNavItem("Journaling", Icons.Filled.Create, "Journaling")
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "Home") {
        composable("Home") {
            HomeScreen()
        }
        composable("Statistics") {
            StatisticsScreen()
        }
        composable("Journaling") {
            JournalingScreen()
        }
    }
}

/**
 * The open settings composable is used to display a message to the user changing the permissions
 */
@Composable
private fun OpenSettingsContent(paddingValues: PaddingValues, onOpenSettingsClicked: () -> Unit) {
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
        Button(onClick = { onOpenSettingsClicked() }) {
            Text("Open Settings")
        }
    }
}

