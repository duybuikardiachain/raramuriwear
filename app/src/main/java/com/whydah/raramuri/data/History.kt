package com.whydah.raramuri.data

import android.location.Location

data class History(
    val currentDistance: Double = 0.0,
    val totalDistance: Double = 0.0,
    val previousGeoPoint: Location = Location(""),
    val currentGeoPoint: Location = Location(""),
    val timestamp: Long = 0,
    val elevation: Double = 0.0,
    val otherData: Map<String, Any> = mapOf()
)
