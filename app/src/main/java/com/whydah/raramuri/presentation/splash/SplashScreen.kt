package com.whydah.raramuri.presentation.splash

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.material.Scaffold
import com.whydah.raramuri.R
import com.whydah.raramuri.presentation.home.HomeScreen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val showSplashScreenContent = remember { mutableStateOf(true) }

    Scaffold {
        if (showSplashScreenContent.value) {
            SplashScreenContent {
                showSplashScreenContent.value = false
            }
        } else {
            HomeScreen(navController = navController)
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