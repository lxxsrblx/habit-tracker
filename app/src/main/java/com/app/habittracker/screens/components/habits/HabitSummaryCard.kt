package com.app.habittracker.screens.components.habits

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.habittracker.screens.components.ThemedChip

@Composable
fun HabitSummaryCard(
    currentStreak: Int,
    bestStreak: Int
) {

    val consistency =
        if (bestStreak == 0)
            0f
        else
            currentStreak.toFloat() / bestStreak.toFloat()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                shape = RoundedCornerShape(24.dp)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(
                alpha = 0.08f
            )
        )
    ) {

        Column(
            modifier = Modifier.padding(24.dp)
        ) {

            Text(
                text = "CURRENT STREAK",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = currentStreak.toString(),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = MaterialTheme.typography.displayLarge.fontSize,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text =
                        if (currentStreak == 1)
                            "day"
                        else
                            "days",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.weight(1f)
                )

                ThemedChip(
                    text = "${(consistency * 100).toInt()}% of best streak"
                )
            }

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text = "Best ever: $bestStreak ${
                    if (bestStreak == 1) "day" else "days"
                }",
                color = Color.Gray
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.primary.copy(
                    alpha = 0.15f
                ),
                thickness = 1.dp
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Row {

                Text(
                    text = "Consistency",
                    color = Color.Gray
                )

                Spacer(
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "${(consistency * 100).toInt()}%",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            LinearProgressIndicator(
                progress = { consistency },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = Color(0xFF1A1A1F)
            )
        }
    }
}