package com.kardiachain.raramuri.service

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationClient {
    fun getLocationUpdates(interval: Long): Flow<Location>

    class LocationException(message: String) : Exception()

    fun isLocationServiceRunning(): Boolean

    fun setLocationServiceRunningStatus(value: Boolean)
}