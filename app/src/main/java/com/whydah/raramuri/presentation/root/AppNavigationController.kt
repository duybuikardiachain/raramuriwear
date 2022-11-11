package com.whydah.raramuri.presentation.root

import androidx.compose.runtime.Composable
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.whydah.raramuri.presentation.home.HomeScreen
import com.whydah.raramuri.presentation.run.RunScreen
import com.whydah.raramuri.presentation.splash.SplashScreen

@Composable
fun AppNavigationController() {
    val navController = rememberSwipeDismissableNavController()

    val splashRoute = NavTarget.SplashScreen.route
    val homeRoute = NavTarget.HomeScreen.route
    val runRoute = NavTarget.RunScreen.route


    SwipeDismissableNavHost(navController = navController, startDestination = NavTarget.SplashScreen.route) {
        composable(
            route = splashRoute
        ) {
            SplashScreen(navController = navController)
        }

        composable(route = homeRoute) {
            HomeScreen(navController = navController)
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

    object SplashScreen : NavTarget(route = "SplashScreen")
}