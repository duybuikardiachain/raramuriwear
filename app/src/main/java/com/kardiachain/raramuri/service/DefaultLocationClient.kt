package com.kardiachain.raramuri.service

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.kardiachain.raramuri.extensions.hasLocationPermission
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class DefaultLocationClient(
    private val context: Context,
    private val client: FusedLocationProviderClient
) : LocationClient {
    var isServiceRunning: Boolean = false

    private lateinit var locationRequest: LocationRequest

    @SuppressLint("MissingPermission")
    override fun getLocationUpdates(interval: Long): Flow<Location> {
        return callbackFlow {
            if (!context.hasLocationPermission()) {
                println("PERMISSION ERROR")
            }

            if (!this@DefaultLocationClient::locationRequest.isInitialized) {
                println("INIT LOCATION REQUEST")
                locationRequest = LocationRequest.Builder(interval)
                    .setIntervalMillis(interval)
                    .setMinUpdateIntervalMillis(interval)
                    .setMaxUpdateDelayMillis(interval)
                    .setPriority(PRIORITY_HIGH_ACCURACY)
                    .setMinUpdateDistanceMeters(1f)
                    .setWaitForAccurateLocation(true)
                    .build()
            }


            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)

                    println("RECEIVE LOCATION")

                    result.locations.lastOrNull()?.let {
                        launch {
                            send(it)

                            println("SEND LOCATION")
                        }
                    }
                }
            }

            if (!isLocationServiceRunning()) {
                println("REQUEST LOCATION REQUEST")

                client.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
            }

            awaitClose {
                println("REMOVE LOCATION REQUEST")

                client.removeLocationUpdates(locationCallback)
            }
        }
    }

    override fun isLocationServiceRunning(): Boolean {
        return isServiceRunning
    }

    override fun setLocationServiceRunningStatus(value: Boolean) {
        isServiceRunning = value
    }

}