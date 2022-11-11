package com.whydah.raramuri.presentation.component

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Switch
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.ToggleChip
import androidx.wear.compose.material.ToggleChipDefaults
import com.whydah.raramuri.R

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