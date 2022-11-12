package com.kardiachain.raramuri.presentation.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import com.kardiachain.raramuri.utils.AppConstants.MOBILE_CAPABILITY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) : ViewModel(), DataClient.OnDataChangedListener,
    MessageClient.OnMessageReceivedListener,
    CapabilityClient.OnCapabilityChangedListener {


    fun hashGps(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)
    }

    fun isLocationEnabled(context: Context): Boolean {
        val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Call your Alert message
            return false
        }
        return true
    }

    fun openLocationSetting(context: Context) {
        if (!isLocationEnabled(context)) {
            if (context is Activity) {
                context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        }
    }

    fun onQueryOtherDevicesClicked(
        context: Context,
        capabilityClient: CapabilityClient
    ) {
        viewModelScope.launch {
            try {
                val myNodes = Wearable.getNodeClient(context).connectedNodes.await()
                for (node in myNodes) {
                    Wearable.getMessageClient(context).sendMessage(node.id, "/location", "HAHAHA".toByteArray())
                }
                println(myNodes)
                val nodes = getCapabilitiesForReachableNodes(capabilityClient, context)
                    .filterValues { MOBILE_CAPABILITY in it }.keys
                displayNodes(nodes)
            } catch (cancellationException: CancellationException) {
                throw cancellationException
            } catch (exception: Exception) {
                Log.d("TAG", "Querying nodes failed: $exception")
            }
        }
    }


    private fun displayNodes(nodes: Set<Node>) {
        val message = if (nodes.isEmpty()) {
            println("NO DEVICES")
        } else {
            println(nodes.joinToString(", ") { it.displayName })
        }
    }

    private suspend fun getCapabilitiesForReachableNodes(
        capabilityClient: CapabilityClient,
        context: Context
    ): Map<Node, Set<String>> =
        capabilityClient.getAllCapabilities(CapabilityClient.FILTER_REACHABLE)
            .await()
            // Pair the list of all reachable nodes with their capabilities
            .flatMap { (capability, capabilityInfo) ->
                capabilityInfo.nodes.map { it to capability }
            }
            // Group the pairs by the nodes
            .groupBy(
                keySelector = { it.first },
                valueTransform = { it.second }
            )
            // Transform the capability list for each node into a set
            .mapValues { it.value.toSet() }

    override fun onDataChanged(p0: DataEventBuffer) {
        println("onDataChanged")
    }

    override fun onMessageReceived(p0: MessageEvent) {
        println("onMessageReceived")
    }

    override fun onCapabilityChanged(p0: CapabilityInfo) {
        println("onCapabilityChanged")
    }

}