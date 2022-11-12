package com.kardiachain.raramuri.presentation.run

import android.Manifest
import android.content.Intent
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.RestoreFromTrash
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.kardiachain.raramuri.R
import com.kardiachain.raramuri.extensions.formatThousandWithPostFix
import com.kardiachain.raramuri.extensions.toSecond
import com.kardiachain.raramuri.presentation.home.HomeViewModel
import com.kardiachain.raramuri.presentation.root.NavTarget
import com.kardiachain.raramuri.service.LocationBroadcastReceiver
import com.kardiachain.raramuri.service.LocationService
import com.kardiachain.raramuri.utils.CommonUtils
import java.util.Calendar

@OptIn(ExperimentalPermissionsApi::class, ExperimentalPagerApi::class, ExperimentalAnimationApi::class)
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

    val loadingComposition: LottieComposition? by rememberLottieComposition(
        LottieCompositionSpec.Asset("running1.json")
    )

    fun startService() {
        Intent(context, LocationService::class.java).apply {
            action = LocationService.ACTION_START
            context.startService(this)
        }

        val currentTime = Calendar.getInstance().timeInMillis.toSecond()
        runViewModel.startTimerRunning(endTime = Int.MAX_VALUE.toLong(), startTime = currentTime, currentTime = currentTime)
    }

    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ),
        onPermissionsResult = { mapResults ->
            if (mapResults[Manifest.permission.ACCESS_COARSE_LOCATION] == true && mapResults[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                backgroundLocationPermissionState.launchPermissionRequest()

                startService()
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

            startService()
        } else {
            locationPermissionsState.launchMultiplePermissionRequest()
        }
    }

    val clickStop = remember { mutableStateOf(false) }

    fun finishRace() {
        //stop counter
        runViewModel.endTimerRunning()

        Intent(context, LocationService::class.java).apply {
            action = LocationService.ACTION_STOP
            context.startService(this)
        }

        navController.navigate(NavTarget.HomeScreen.route)
    }

    fun settings() {
        navController.navigate(NavTarget.HomeScreen.route)
    }

    fun cancel() {
        navController.navigate(NavTarget.HomeScreen.route)

        Intent(context, LocationService::class.java).apply {
            action = LocationService.ACTION_REMOVE
            context.startService(this)
        }
    }

    if (homeViewModel.isLocationEnabled(context)) {

        Column(modifier = Modifier.fillMaxSize()) {
            HorizontalPager(
                modifier = Modifier.weight(0.9f),
                count = 3,
                state = tabPager,
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
                            SmallLoading(loadingComposition = loadingComposition)

                            Spacer(modifier = Modifier.height(5.dp))

                            Text(runningTimeDataState.runningTime, fontSize = 12.sp)

                            Spacer(modifier = Modifier.height(10.dp))

                            if (!clickStop.value) {
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(Color.Red)
                                        .padding(10.dp)
                                        .clickable {
                                            clickStop.value = true
                                        }
                                ) {
                                    Icon(imageVector = Icons.Default.Pause, contentDescription = null, modifier = Modifier.size(15.dp))
                                }
                            } else {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(color = colorResource(id = R.color.color_282828))
                                            .padding(5.dp)
                                            .clickable { settings() }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(18.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(15.dp))

                                    Box(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(Color.Red)
                                            .padding(10.dp)
                                            .clickable { finishRace() }
                                    ) {
                                        Icon(imageVector = Icons.Default.Flag, contentDescription = null, modifier = Modifier.size(15.dp))
                                    }

                                    Spacer(modifier = Modifier.width(15.dp))

                                    Box(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(color = colorResource(id = R.color.color_282828))
                                            .padding(5.dp)
                                            .clickable { cancel() }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.RestoreFromTrash,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Box(
                                    modifier = Modifier
                                        .size(width = 50.dp, height = 20.dp)
                                        .clip(RoundedCornerShape(80))
                                        .background(color = colorResource(id = R.color.color_282828))
                                        .clickable { clickStop.value = false },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Resume", fontSize = 9.sp)
                                }
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

                            Text(
                                CommonUtils.getPaceDetail(avgPaceLabel.value), color = colorResource(id = R.color.main_raramuri_color),
                                fontWeight = FontWeight.Bold, fontSize = 24.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(distanceLabel.value.formatThousandWithPostFix(2), fontSize = 10.sp)

                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .weight(0.16f)
                    .fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                (0..1).forEach {
                    Box(
                        modifier = Modifier
                            .size(9.dp)
                            .clip(CircleShape)
                            .background(if (it == tabPager.currentPage) colorResource(id = R.color.main_raramuri_color) else Color.Gray)
                    )
                    if (it == 0) {
                        Spacer(modifier = Modifier.width(7.dp))
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

@Composable
fun SmallLoading(
    modifier: Modifier = Modifier,
    loadingComposition: LottieComposition?,
    backgroundColor: Color = Color.Transparent,
    scale: Float = 1f,
    loadingSize: Dp = 30.dp
) {
    Box(
        modifier = modifier
            .background(backgroundColor),
        contentAlignment = Alignment.BottomCenter
    ) {
        LottieAnimation(
            composition = loadingComposition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier
                .size(loadingSize)
                .scale(scale)
        )
    }
}