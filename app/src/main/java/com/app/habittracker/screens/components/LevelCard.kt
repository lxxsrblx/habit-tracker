package com.app.habittracker.screens.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.habittracker.utils.getProgressToNextLevel
import com.app.habittracker.utils.getXpForLevel

@Composable
fun LevelCard(
    level: Int,
    totalXp: Int
) {
    val progress = getProgressToNextLevel(totalXp)
    val xpRequired = getXpForLevel(level)

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF121212)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "LEVEL",
                        color = Color.Gray,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = "$level",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Surface(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "$totalXp XP TOTAL",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = Color(0xFF2A2A2A)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Next level at ${xpRequired} XP in current level",
                color = Color.Gray,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
