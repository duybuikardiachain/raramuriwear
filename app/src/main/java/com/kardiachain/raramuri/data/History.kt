package com.kardiachain.raramuri.data

data class History(
    val currentDistance: Double = 0.0,
    val totalDistance: Double = 0.0,
    val previousLat: Double = 0.0,
    val previousLng: Double = 0.0,
    val previousElevation: Double = 0.0,
    val currentLat: Double = 0.0,
    val currentLng: Double = 0.0,
    val currentElevation: Double = 0.0,
    val timestamp: Long = 0
)
