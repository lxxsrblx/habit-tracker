package com.app.habittracker.screens.components.analytics

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.habittracker.viewmodel.analytics.AchievementUi

@Composable
fun AchievementsCard(
    achievements: List<AchievementUi>,
    unlocked: Int,
    total: Int
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF121212)
        ),
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                Text(
                    text = "ACHIEVEMENTS",
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelMedium
                )

                Text(
                    text = "$unlocked/$total",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            achievements.forEachIndexed { index, item ->

                AchievementItem(
                    achievement = item
                )

                if (
                    index != achievements.lastIndex
                ) {
                    Spacer(
                        modifier =
                            Modifier.height(10.dp)
                    )
                }
            }
        }
    }
}