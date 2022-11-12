package com.kardiachain.raramuri.presentation.home

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.DirectionsWalk
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.ScalingLazyColumn
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.Wearable
import com.kardiachain.raramuri.R
import com.kardiachain.raramuri.presentation.component.CustomChip
import com.kardiachain.raramuri.presentation.root.NavTarget
import com.kardiachain.raramuri.presentation.theme.RaramuriWearTheme
import com.kardiachain.raramuri.service.LocationService

@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel,
    capabilityClient: CapabilityClient
) {
    val context = LocalContext.current
    LaunchedEffect(true) {
        homeViewModel.hashGps(context)
    }

    RaramuriWearTheme {
        ScalingLazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                CustomChip(
                    content = "Walk",
                    imageVector = Icons.Rounded.DirectionsWalk,
                    onClick = {
                        homeViewModel.onQueryOtherDevicesClicked(context, capabilityClient)
                    },
                )
            }
            item {
                CustomChip(
                    content = "Run",
                    imageVector = Icons.Rounded.DirectionsRun,
                    onClick = { navController.navigate(NavTarget.RunScreen.route) },
                )
            }

            item {
                CustomChip(
                    content = "Notification",
                    imageVector = Icons.Rounded.Notifications,
                    onClick = {

                    },
                )
            }
            item {
                CustomChip(
                    content = "Group",
                    imageVector = Icons.Rounded.Group,
                    onClick = {

                    }
                )
            }
            item {
                CustomChip(
                    content = "Setting",
                    imageVector = Icons.Rounded.Settings,
                    onClick = {
                        navController.navigate(NavTarget.SettingScreen.route)
                    },
                )
            }
        }
    }
}