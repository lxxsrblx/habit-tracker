package com.app.habittracker.screens.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Whatshot
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun JourneyChip(
    day: Int
) {
    val primary = MaterialTheme.colorScheme.primary

    Surface(
        color = primary.copy(
            alpha = 0.08f
        ),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.border(
            width = 1.dp,
            color = primary.copy(
                alpha = 0.35f
            ),
            shape = RoundedCornerShape(24.dp)
        )
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(
                horizontal = 18.dp,
                vertical = 10.dp
            )
        ) {
            Icon(
                imageVector = Icons.Rounded.Whatshot,
                contentDescription = null,
                tint = primary,
                modifier = Modifier.size(13.dp)
            )

            Text(
                text = "Day $day Journey",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}