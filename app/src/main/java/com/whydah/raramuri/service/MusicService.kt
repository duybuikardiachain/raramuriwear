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
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationServices
import com.whydah.raramuri.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class MusicService : Service(), SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var notificationManager: NotificationManager
    private lateinit var locationClient: LocationClient

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val mainServiceScope = CoroutineScope(Dispatchers.Main)

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

    private fun updateNotification(text: String) {
        val notification = generateNotification(text)
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun startRegister() {
        try {
            //start foreground service
            updateNotification("getString(R.string.waiting_to_MC_signals)")

            locationClient.getLocationUpdates(5000)
                .catch {
                    println(it)
                }.onEach { location ->
                    println(location)
                }.launchIn(serviceScope)

//            firestoreServiceManager.listenTalkSnapshot(eventId).flowOn(Dispatchers.IO).catch {
//                println(it)
//            }.onEach { talk ->
//                mainServiceScope.launch {
//                    talksQueue.add(talk)
//                    if (!isPlayingAnnouncement) {
//                        playMedia(talk.audioLink)
//                    }
//                }
//            }.launchIn(serviceScope)

            //register shared changed
            sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        } catch (e: Exception) {
            println(e.message)
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun generateNotification(notificationText: String): Notification {
        // 0. Get data
        val titleText = getString(R.string.app_name)

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
        val bigTextStyle = NotificationCompat.BigTextStyle().bigText(notificationText).setBigContentTitle(titleText)

        // 3. Set up main Intent/Pending Intents for notification.
        val launchActivityIntent = Intent()

        val activityPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                this, System.currentTimeMillis().toInt(), launchActivityIntent, PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getActivity(
                this, System.currentTimeMillis().toInt(), launchActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val cancelIntent = Intent(this, MusicService::class.java)
        cancelIntent.action = ACTION_STOP

        val serviceCancelIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getService(
                this, 10, cancelIntent, PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getService(
                this, 10, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        }


        // 4. Build and issue the notification.
        // Notification Channel Id is ignored for Android pre O (26).
        val notificationCompatBuilder = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)

        return notificationCompatBuilder.setStyle(bigTextStyle).setContentTitle(titleText).setContentText(notificationText)
            .setSmallIcon(R.drawable.ic_notification).setDefaults(Notification.DEFAULT_LIGHTS).setOngoing(true).setVibrate(null)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC).setContentIntent(activityPendingIntent).build()
    }

    private fun stopRegister() {
        try {

            stopForeground(STOP_FOREGROUND_DETACH)
            stopSelf()

            sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        } catch (_: Exception) {

        }
    }

    override fun onDestroy() {
        super.onDestroy()

        serviceScope.cancel()
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onSharedPreferenceChanged(
        p0: SharedPreferences?,
        key: String?
    ) {

    }

}