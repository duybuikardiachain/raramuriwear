package com.whydah.raramuri.presentation.root

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.whydah.raramuri.presentation.home.HomeScreen
import com.whydah.raramuri.presentation.home.HomeViewModel
import com.whydah.raramuri.presentation.run.RunScreen
import com.whydah.raramuri.presentation.splash.SplashScreen

@Composable
fun AppNavigationController(
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val navController = rememberSwipeDismissableNavController()

    val splashRoute = NavTarget.SplashScreen.route
    val homeRoute = NavTarget.HomeScreen.route
    val runRoute = NavTarget.RunScreen.route


    SwipeDismissableNavHost(navController = navController, startDestination = NavTarget.SplashScreen.route) {
        composable(
            route = splashRoute
        ) {
            SplashScreen(navController = navController, homeViewModel = homeViewModel)
        }

        composable(route = homeRoute) {
            HomeScreen(navController = navController, homeViewModel = homeViewModel)
        }

        composable(
            route = runRoute
        ) {
            RunScreen(navController = navController, homeViewModel = homeViewModel)
        }
    }
}

sealed class NavTarget(val route: String) {
    object HomeScreen : NavTarget(route = "HomeScreen")
    object RunScreen : NavTarget(route = "RunScreen")

    object SplashScreen : NavTarget(route = "SplashScreen")
}