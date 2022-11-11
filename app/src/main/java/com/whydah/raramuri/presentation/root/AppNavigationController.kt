package com.whydah.raramuri.presentation.root

import androidx.compose.runtime.Composable
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.whydah.raramuri.presentation.home.HomeScreen
import com.whydah.raramuri.presentation.run.RunScreen

@Composable
fun AppNavigationController() {
    val navController = rememberSwipeDismissableNavController()

    val homeRoute = NavTarget.HomeScreen.route
    val runRoute = NavTarget.RunScreen.route


    SwipeDismissableNavHost(navController = navController, startDestination = NavTarget.HomeScreen.route) {
        composable(
            route = homeRoute
        ) {
            HomeScreen(navController)
        }
        
        composable(
            route = runRoute
        ) {
            RunScreen(navController = navController)
        }
    }
}

sealed class NavTarget(val route: String) {
    object HomeScreen : NavTarget(route = "HomeScreen")
    object RunScreen : NavTarget(route = "RunScreen")
}