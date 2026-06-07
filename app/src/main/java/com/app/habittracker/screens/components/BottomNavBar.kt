package com.app.habittracker.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.BarChart

@Composable
fun BottomNavBar(
    selected: String,
    onDashboardClick: () -> Unit,
    onStatsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFF121212)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 12.dp,
                    vertical = 10.dp
                ),
            horizontalArrangement =
                Arrangement.SpaceEvenly
        ) {

            NavItem(
                icon = Icons.Default.BarChart,
                selected = selected == "stats",
                onClick = onStatsClick
            )

            NavItem(
                icon = Icons.Default.Home,
                selected = selected == "dashboard",
                onClick = onDashboardClick
            )

            NavItem(
                icon = Icons.Default.Settings,
                selected = selected == "settings",
                onClick = onSettingsClick
            )
        }
    }
}