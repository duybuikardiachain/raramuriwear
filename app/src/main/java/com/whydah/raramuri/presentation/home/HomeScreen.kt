package com.whydah.raramuri.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.DirectionsWalk
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Switch
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.ToggleChip
import androidx.wear.compose.material.ToggleChipDefaults
import com.whydah.raramuri.R
import com.whydah.raramuri.presentation.theme.RaramuriWearTheme


@Composable
fun HomeScreen(
    navController: NavController
) {
    RaramuriWearTheme {
        ScalingLazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp)
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                CustomChip(
                    modifier = Modifier.padding(bottom = 3.dp),
                    content = "Walk",
                    imageVector = Icons.Rounded.DirectionsWalk,
                    onClick = {
                        
                    },
                    backgroundRes = R.drawable.iv_cloud1
                )
            }
            item {
                CustomChip(
                    modifier = Modifier.padding(bottom = 3.dp),
                    content = "Run",
                    imageVector = Icons.Rounded.DirectionsRun,
                    onClick = {

                    },
                    backgroundRes = R.drawable.iv_cloud2
                )
            }
            item {
                CustomChip(
                    modifier = Modifier.padding(bottom = 3.dp),
                    content = "Notification",
                    imageVector = Icons.Rounded.Notifications,
                    onClick = {

                    },
                    backgroundRes = R.drawable.iv_cloud3
                )
            }
            item {
                CustomChip(
                    modifier = Modifier.padding(bottom = 3.dp),
                    content = "Group",
                    imageVector = Icons.Rounded.Group,
                    onClick = {

                    }
                )
            }
            item {
                CustomChip(
                    modifier = Modifier.padding(bottom = 5.dp),
                    content = "Setting",
                    imageVector = Icons.Rounded.Settings,
                    onClick = {

                    },
                    backgroundRes = R.drawable.iv_cloud2
                )
            }
        }
    }
}

@Composable
fun CustomChip(
    modifier: Modifier = Modifier,
    content: String,
    imageVector: ImageVector,
    onClick: () -> Unit,
    backgroundRes: Int = R.drawable.iv_cloud1,
) {
    Chip(
        modifier = modifier
            .width(150.dp)
            .height(32.dp),
        onClick = { onClick() },
        label = {
            Text(
                text = content,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 9.sp
            )
        },
        icon = {
            Icon(
                imageVector = imageVector,
                contentDescription = "triggers meditation action",
                modifier = Modifier,
                tint = colorResource(id = R.color.main_raramuri_color)
            )
        },
        colors = ChipDefaults.imageBackgroundChipColors(
            backgroundImagePainter = painterResource(id = backgroundRes)
        ),
    )
}

@Composable
fun CustomToggleChip(
    modifier: Modifier = Modifier,
) {
    var checked by remember { mutableStateOf(false) }

    ToggleChip(
        modifier = Modifier,
        checked = checked,
        toggleControl = {
            Switch(
                checked = checked,
                modifier = Modifier.semantics {
                    this.contentDescription = if (checked) "On" else "Off"
                }
            )
        },
        onCheckedChange = {
            checked = !checked
        },
        label = {
            Text(text = "Music: On")
        },
        colors = ToggleChipDefaults.toggleChipColors()
    )
}