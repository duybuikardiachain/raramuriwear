package com.whydah.raramuri.presentation.run

import android.Manifest
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.Pause
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.whydah.raramuri.R
import com.whydah.raramuri.extensions.formatThousandWithPostFix
import com.whydah.raramuri.extensions.toSecond
import com.whydah.raramuri.presentation.home.HomeViewModel
import com.whydah.raramuri.service.LocationBroadcastReceiver
import com.whydah.raramuri.service.LocationService
import com.whydah.raramuri.utils.CommonUtils
import java.util.Calendar

@OptIn(ExperimentalPermissionsApi::class, ExperimentalPagerApi::class)
@Composable
fun RunScreen(
    navController: NavController,
    homeViewModel: HomeViewModel,
    runViewModel: RunViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val backgroundLocationPermissionState = rememberPermissionState(permission = Manifest.permission.ACCESS_BACKGROUND_LOCATION)

    val runningTimeDataState by remember(runViewModel) { runViewModel.runningTimeDataState }.collectAsState()

    val distanceLabel = remember { mutableStateOf(0.0) }
    val avgPaceLabel = remember { mutableStateOf(0.0) }

    val heart = remember { mutableStateOf(86) }
    val tabPager = rememberPagerState(0)


    //listen broadcast
    LocationBroadcastReceiver(systemAction = LocationService.ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST, onLocationEvent = { intent ->
        val action = intent.action
        if (action == LocationService.ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST) {
            val distance = intent.getDoubleExtra(LocationService.CURRENT_DISTANCE, 0.0)
            val avgPace = intent.getDoubleExtra(LocationService.AVG_PACE, 0.0)

            distanceLabel.value = distance
            avgPaceLabel.value = avgPace
        }
    })

    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ),
        onPermissionsResult = { mapResults ->
            if (mapResults[Manifest.permission.ACCESS_COARSE_LOCATION] == true && mapResults[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                backgroundLocationPermissionState.launchPermissionRequest()
            }
        }
    )

    DisposableEffect(true) {
        onDispose {
            runViewModel.endTimerRunning()
        }
    }

    LaunchedEffect(true) {
        if (locationPermissionsState.allPermissionsGranted) {
            if (!backgroundLocationPermissionState.status.isGranted) {
                backgroundLocationPermissionState.launchPermissionRequest()
            }
            Intent(context, LocationService::class.java).apply {
                action = LocationService.ACTION_START
                context.startService(this)
            }

            val currentTime = Calendar.getInstance().timeInMillis.toSecond()
            runViewModel.startTimerRunning(endTime = Int.MAX_VALUE.toLong(), startTime = currentTime, currentTime = currentTime)
        } else {
            locationPermissionsState.launchMultiplePermissionRequest()
        }
    }

    if (homeViewModel.isLocationEnabled(context)) {
        HorizontalPager(
            count = 2,
            state = tabPager
        ) {
            when (it) {
                1 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 16.dp, horizontal = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(runningTimeDataState.runningTime, fontSize = 12.sp)

                        Spacer(modifier = Modifier.height(15.dp))

                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.Red)
                                .padding(10.dp)
                                .clickable {
                                    //stop running, send data to app
                                }
                        ) {
                            Icon(imageVector = Icons.Default.Pause, contentDescription = null, modifier = Modifier.size(15.dp))
                        }
                    }
                }

                0 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 16.dp, horizontal = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.HeartBroken,
                                contentDescription = "",
                                tint = colorResource(id = R.color.main_raramuri_color),
                                modifier = Modifier.size(15.dp)
                            )

                            Spacer(modifier = Modifier.width(3.dp))

                            Text(text = "${heart.value}")
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(runningTimeDataState.runningTime, fontSize = 12.sp)

                        Spacer(modifier = Modifier.height(12.dp))

                        Text( CommonUtils.getPaceDetail(avgPaceLabel.value), color = colorResource(id = R.color.main_raramuri_color), fontWeight = FontWeight.Bold, fontSize = 24.sp)

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(distanceLabel.value.formatThousandWithPostFix(2), fontSize = 10.sp)
                    }
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp, horizontal = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Localtion services are disabled. Would you like to enable them to record Raramuri activites?",
                textAlign = TextAlign.Center,
                modifier = Modifier
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .border(border = BorderStroke(0.5.dp, color = Color.White), shape = CircleShape)
                        .padding(2.dp)
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(color = Color.Red)
                        .border(border = BorderStroke(0.5.dp, color = Color.Red), shape = CircleShape)
                        .padding(2.dp)
                        .clickable { homeViewModel.openLocationSetting(context) }
                ) {
                    Icon(imageVector = Icons.Default.Done, contentDescription = "")
                }
            }
        }
    }
}