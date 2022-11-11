package com.whydah.raramuri.presentation.home

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
import com.whydah.raramuri.R
import com.whydah.raramuri.presentation.component.CustomChip
import com.whydah.raramuri.presentation.root.NavTarget
import com.whydah.raramuri.presentation.theme.RaramuriWearTheme
import com.whydah.raramuri.service.LocationService

@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel
) {
    val context = LocalContext.current
//    LaunchedEffect(true) {
//        homeViewModel.hashGps(context)
//    }
//
    val state = rememberLazyListState()

    RaramuriWearTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp)
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                CustomChip(
                    modifier = Modifier.padding(bottom = 3.dp),
                    content = "Walk",
                    imageVector = Icons.Rounded.DirectionsWalk,
                    onClick = {

                    },
                )
            }
            item {
                CustomChip(
                    modifier = Modifier.padding(bottom = 3.dp),
                    content = "Run",
                    imageVector = Icons.Rounded.DirectionsRun,
                    onClick = { navController.navigate(NavTarget.RunScreen.route) },
                )
            }
            item {
                CustomChip(
                    modifier = Modifier.padding(bottom = 3.dp),
                    content = "Run",
                    imageVector = Icons.Rounded.DirectionsRun,
                    onClick = { navController.navigate(NavTarget.RunScreen.route) },
                )
            }
            item {
                CustomChip(
                    modifier = Modifier.padding(bottom = 3.dp),
                    content = "Notification",
                    imageVector = Icons.Rounded.Notifications,
                    onClick = {

                    },
                )
            }
            item {
                CustomChip(
                    modifier = Modifier.padding(bottom = 3.dp),
                    content = "Group",
                    imageVector = Icons.Rounded.Group,
                    onClick = {

                    }
                )
            }
            item {
                CustomChip(
                    modifier = Modifier.padding(bottom = 5.dp),
                    content = "Setting",
                    imageVector = Icons.Rounded.Settings,
                    onClick = {

                    },
                )
            }
        }
    }
}