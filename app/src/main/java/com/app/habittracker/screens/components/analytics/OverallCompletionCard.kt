package com.app.habittracker.screens.components.analytics

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
import androidx.compose.ui.unit.dp

@Composable
fun OverallCompletionCard(
    progress: Float
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF121212)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            Modifier.padding(20.dp)
        ) {

            Text(
                text = "OVERALL COMPLETION",
                color = Color.Gray,
                style = MaterialTheme.typography.labelMedium
            )

            Spacer(
                Modifier.height(12.dp)
            )

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp),
                color = MaterialTheme.colorScheme.primary,
            )

            Spacer(
                Modifier.height(8.dp)
            )

            Text(
                text = "${(progress * 100).toInt()}%",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}