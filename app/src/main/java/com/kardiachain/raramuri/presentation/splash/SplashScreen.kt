package com.kardiachain.raramuri.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.material.Scaffold
import com.google.android.gms.wearable.CapabilityClient
import com.kardiachain.raramuri.R
import com.kardiachain.raramuri.presentation.home.HomeScreen
import com.kardiachain.raramuri.presentation.home.HomeViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController, homeViewModel: HomeViewModel, capabilityClient: CapabilityClient) {
    val showSplashScreenContent = rememberSaveable { mutableStateOf(true) }

    Scaffold {
        if (showSplashScreenContent.value) {
            SplashScreenContent { showSplashScreenContent.value = false }
        } else {
            HomeScreen(navController = navController, homeViewModel = homeViewModel, capabilityClient = capabilityClient)
        }
    }
}

@Composable
fun SplashScreenContent(
    modifier: Modifier = Modifier,
    onTimeout: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        val currentOnTimeout by rememberUpdatedState(onTimeout)

        LaunchedEffect(true) {
            delay(1500)
            currentOnTimeout.invoke()
        }

        Image(painterResource(id = R.drawable.iv_raramuri_logo), contentDescription = null, modifier = Modifier.padding(30.dp))

    }
}