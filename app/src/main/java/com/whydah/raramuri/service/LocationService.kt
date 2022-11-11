package com.whydah.raramuri.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationServices
import com.whydah.raramuri.R
import com.whydah.raramuri.extensions.formatThousandWithPostFix
import com.whydah.raramuri.extensions.toSecond
import com.whydah.raramuri.presentation.MainActivity
import com.whydah.raramuri.utils.CommonUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class LocationService : Service() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var notificationManager: NotificationManager

    private lateinit var locationClient: LocationClient

    private lateinit var currentLocation: Location

    private lateinit var previousLocation: Location

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val mainScope = CoroutineScope(Dispatchers.Main)

    private var totalDistance: Double = 0.0

    private var avgPace = 0.0

    private var startTime: Long = 0

    companion object {
        const val ACTION_START = "ACTION_START"

        const val ACTION_STOP = "ACTION_STOP"

        private const val NOTIFICATION_ID = 987654321

        const val NOTIFICATION_CHANNEL_ID = "while_in_use_channel_01"
    }

    override fun onCreate() {
        super.onCreate()

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
        startTime = Calendar.getInstance().timeInMillis.toSecond()
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {
        when (intent?.action) {
            ACTION_START -> {
                startRegister()
            }

            ACTION_STOP -> {
                stopRegister()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun updateNotification() {
        val notification = generateNotification()
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun startRegister() {
        try {
            locationClient.getLocationUpdates(5000)
                .catch {
                    println(it)
                }.onEach { location ->
                    //start foreground service
                    locationClient.setLocationServiceRunningStatus(true)

                    //apply new location
                    currentLocation = location
                    if (!this::previousLocation.isInitialized) {
                        //set previous location to current location
                        previousLocation = currentLocation
                    }

                    //calculate distance and total distance
                    val distance = currentLocation.distanceTo(previousLocation)
                    totalDistance += distance

                    //calculate avg pace
                    avgPace = CommonUtils.calculatePaceByDouble(
                        startTime = startTime, endTime = Calendar.getInstance().timeInMillis.toSecond(), distance = totalDistance
                    )

                    updateNotification()

                    println(totalDistance)
                    mainScope.launch {
                        Toast.makeText(this@LocationService, totalDistance.toString(), Toast.LENGTH_SHORT).show()
                    }


                }.launchIn(serviceScope)
        } catch (e: Exception) {
            println(e.message)
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun generateNotification(): Notification {
        // 0. Get data
        val titleText = "Raramuri"
        val subText = "${totalDistance.formatThousandWithPostFix(maxDigit = 2)} - ${CommonUtils.getPaceDetail(pace = avgPace)}"
        println(subText)

        // 1. Create Notification Channel for O+ and beyond devices (26+).

        val notificationChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID, titleText, NotificationManager.IMPORTANCE_LOW
        )

        // Adds NotificationChannel to system. Attempting to create an
        // existing notification channel with its original values performs
        // no operation, so it's safe to perform the below sequence.
        notificationChannel.vibrationPattern = null
        notificationChannel.enableVibration(false)
        notificationChannel.enableLights(true)
        notificationChannel.setSound(null, null)
        notificationManager.createNotificationChannel(notificationChannel)

        // 2. Build the BIG_TEXT_STYLE.
        val bigTextStyle = NotificationCompat.BigTextStyle().bigText(subText).setBigContentTitle(titleText)

        // 3. Set up main Intent/Pending Intents for notification.
        val launchActivityIntent = Intent(this, MainActivity::class.java).apply {

        }

        val activityPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                this, System.currentTimeMillis().toInt(), launchActivityIntent, PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getActivity(
                this, System.currentTimeMillis().toInt(), launchActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        // 4. Build and issue the notification.
        // Notification Channel Id is ignored for Android pre O (26).
        val notificationCompatBuilder = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)

        return notificationCompatBuilder.setStyle(bigTextStyle).setContentTitle(titleText).setContentText(subText)
            .setSmallIcon(R.drawable.ic_notification).setDefaults(Notification.DEFAULT_LIGHTS).setOngoing(true).setVibrate(null)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC).setContentIntent(activityPendingIntent).build()
    }

    private fun stopRegister() {
        try {
            locationClient.setLocationServiceRunningStatus(false)

            stopForeground(STOP_FOREGROUND_DETACH)
            stopSelf()
        } catch (e: Exception) {
            println(e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        serviceScope.cancel()
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}