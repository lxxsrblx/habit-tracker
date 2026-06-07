package com.app.habittracker.screens.components.habits

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

@Composable
fun ProgressCard(
    progress: Float
) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF121212)
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                text = "TODAY'S PROGRESS",
                color = Color.Gray,
                style = MaterialTheme.typography.labelMedium
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = Color(0xFF2A2A2A),
                strokeCap = StrokeCap.Round
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = when {
                    progress == 0f -> "No habits completed yet"
                    progress >= 1f -> "All habits done for today! Well done!"
                    else -> "${(progress * 100).toInt()}% completed today"
                },
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}