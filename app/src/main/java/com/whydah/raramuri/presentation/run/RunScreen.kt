package com.whydah.raramuri.presentation.run

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text

@Composable
fun RunScreen(
    navController: NavController
) {
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = 28.dp,
            start = 10.dp,
            end = 10.dp,
            bottom = 40.dp
        ),
        verticalArrangement = Arrangement.Center,
    ) {
        items(10) { index ->
            Chip(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                icon = {
                    Icon(
                        painter = painterResource(id = com.google.android.horologist.compose.tools.R.drawable.ic_100tb),
                        contentDescription = "Star",
                        modifier = Modifier
                            .size(24.dp)
                            .wrapContentSize(align = Alignment.Center),
                    )
                },
                label = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colors.onPrimary,
                        text = "Item ${index + 1}"
                    )
                },
                onClick = {
//                    navController.navigate("Detail")
                }
            )
        }
    }
}