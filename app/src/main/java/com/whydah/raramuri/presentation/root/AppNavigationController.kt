package com.whydah.raramuri.presentation.root

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.google.android.gms.wearable.CapabilityClient
import com.whydah.raramuri.presentation.home.HomeScreen
import com.whydah.raramuri.presentation.home.HomeViewModel
import com.whydah.raramuri.presentation.run.RunScreen
import com.whydah.raramuri.presentation.setting.SettingScreen
import com.whydah.raramuri.presentation.splash.SplashScreen

@Composable
fun AppNavigationController(
    homeViewModel: HomeViewModel,
    capabilityClient: CapabilityClient
) {
    val navController = rememberSwipeDismissableNavController()

    val splashRoute = NavTarget.SplashScreen.route
    val homeRoute = NavTarget.HomeScreen.route
    val runRoute = NavTarget.RunScreen.route
    val settingRoute = NavTarget.SettingScreen.route

    SwipeDismissableNavHost(navController = navController, startDestination = NavTarget.SplashScreen.route) {
        composable(
            route = splashRoute
        ) {
            SplashScreen(navController = navController, homeViewModel = homeViewModel, capabilityClient = capabilityClient)
        }

        composable(route = homeRoute) {
            HomeScreen(navController = navController, homeViewModel = homeViewModel, capabilityClient = capabilityClient)
        }

        composable(
            route = runRoute
        ) {
            RunScreen(navController = navController, homeViewModel = homeViewModel)
        }

        composable(
            route = settingRoute
        ) {
            SettingScreen(navController = navController)
        }
    }
}

sealed class NavTarget(val route: String) {
    object HomeScreen : NavTarget(route = "HomeScreen")
    object RunScreen : NavTarget(route = "RunScreen")
    object SplashScreen : NavTarget(route = "SplashScreen")
    object SettingScreen : NavTarget(route = "SettingScreen")
}