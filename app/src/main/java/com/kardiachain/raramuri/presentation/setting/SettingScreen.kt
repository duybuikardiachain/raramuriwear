package com.whydah.raramuri.presentation.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import com.whydah.raramuri.presentation.component.CustomDoubleTextChip
import com.whydah.raramuri.presentation.component.CustomSingleTextChip

@Composable
fun SettingScreen(
    navController: NavController
) {
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Text(text = "Settings")

        }

        item {
            Spacer(modifier = Modifier.height(15.dp))
        }

        item {
            CustomDoubleTextChip(
                firstContent = "Unsynced events",
                secondContent = "5",
                onClick = {

                }
            )
        }

        item {
            CustomDoubleTextChip(
                firstContent = "Version",
                secondContent = "1.0.0 (1)",
                onClick = {

                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(15.dp))
        }

        item {
            CustomSingleTextChip(
                content = "Log out",
                onClick = {

                }
            )
        }
    }
}