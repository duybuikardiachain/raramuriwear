package com.whydah.raramuri.utils

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

object CommonUtils {
    fun customDistanceBetween(
        previousLat: Double,
        previousLng: Double,
        currentLat: Double,
        currentLng: Double,
        prevAltitude: Double,
        currentAltitude: Double
    ): Double {
        val radius = 6371 // Radius of the earth
        val latDistance = Math.toRadians(currentLat - previousLat)
        val lonDistance = Math.toRadians(currentLng - previousLng)
        val a = (sin(latDistance / 2) * sin(latDistance / 2)
                + (cos(Math.toRadians(previousLat)) * cos(Math.toRadians(currentLat))
                * sin(lonDistance / 2) * sin(lonDistance / 2)))
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        var distance = radius * c * 1000 // convert to meters
        val height = prevAltitude - currentAltitude
        distance = distance.pow(2.0) + height.pow(2.0)
        return sqrt(distance)
    }

    fun getPaceDetail(pace: Double): String {
        try {
            if (pace > 1000) {
                return AppConstants.DEFAULT_PACE_VALUE
            }

            if (pace < 1) {
                return String.format(
                    "%02d:%02d /km",
                    0,
                    (pace * 60).toInt()
                )
            }

            return String.format(
                "%02d:%02d /km",
                pace.toInt(),
                ((pace % (pace.toInt())) * 60).toInt()
            )
        } catch (e: java.lang.Exception) {
            return ""
        }
    }

    fun calculatePaceByDouble(
        startTime: Long,
        endTime: Long,
        distance: Double,
    ): Double {
        return try {
            if (distance == 0.0) {
                return 0.0
            }

            val time = endTime - startTime
            (time / 60.0) / (distance / 1000)
        } catch (e: Exception) {
            0.0
        }
    }
}