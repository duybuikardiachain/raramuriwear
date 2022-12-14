package com.kardiachain.raramuri.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
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
import com.kardiachain.raramuri.R

@Composable
fun CustomChip(
    modifier: Modifier = Modifier,
    content: String,
    imageVector: ImageVector,
    onClick: () -> Unit,
) {
    Chip(
        modifier = modifier
            .width(150.dp)
            .height(34.dp)
            .padding(bottom = 4.dp),
        colors = ChipDefaults.chipColors(backgroundColor = colorResource(id = R.color.color_282828)),
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
                tint = colorResource(id = R.color.main_raramuri_color),
                modifier = Modifier.size(20.dp)
            )
        },
    )
}

@Composable
fun CustomDoubleTextChip(
    modifier: Modifier = Modifier,
    firstContent: String,
    secondContent: String,
    onClick: () -> Unit,
) {
    Chip(
        modifier = modifier
            .width(150.dp)
            .height(40.dp)
            .padding(bottom = 4.dp),
        colors = ChipDefaults.chipColors(backgroundColor = colorResource(id = R.color.color_282828)),
        onClick = { onClick() },
        label = {
            Text(
                text = firstContent,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.SemiBold,
                fontSize = 9.sp
            )
        },
        secondaryLabel = {
            Text(
                text = secondContent,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 8.sp,
                color = Color.Gray
            )
        }
    )
}

@Composable
fun CustomSingleTextChip(
    modifier: Modifier = Modifier,
    content: String,
    onClick: () -> Unit,
) {
    Chip(
        modifier = modifier
            .width(150.dp)
            .height(34.dp)
            .padding(bottom = 3.dp),
        colors = ChipDefaults.chipColors(backgroundColor = colorResource(id = R.color.color_282828)),
        onClick = { onClick() },
        label = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = content,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 9.sp
                )
            }
        },
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