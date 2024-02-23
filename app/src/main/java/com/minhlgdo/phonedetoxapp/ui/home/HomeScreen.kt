package com.minhlgdo.phonedetoxapp.ui.home

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.minhlgdo.phonedetoxapp.ui.journaling.JournalingScreen
import com.minhlgdo.phonedetoxapp.ui.select_apps.SelectAppsScreen

@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    HomeScreenNavHost(navController = navController)
}

@Composable
private fun HomeScreenNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainHome(navController = navController)
        }
        composable("journaling") {
            JournalingScreen(navController = navController)
        }
        composable("select_apps") {
            SelectAppsScreen(navController = navController)
        }
    }
}
