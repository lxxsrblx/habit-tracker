package com.app.habittracker.screens.components.habits

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.habittracker.utils.formatDate

@Composable
fun RecentCompletionsCard(
    total: Int,
    dates: List<Long>
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF121212)
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Completion History",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "$total total",
                    color = Color.Gray
                )
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            if (dates.isEmpty()) {

                Text(
                    text = "No completions yet",
                    color = Color.Gray
                )

            } else {

                Column(
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    dates
                        .sortedDescending()
                        .take(5)
                        .forEach { timestamp ->

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(
                                            MaterialTheme.colorScheme.primary
                                        )
                                )

                                Spacer(
                                    modifier = Modifier.width(12.dp)
                                )

                                Text(
                                    text = formatDate(
                                        timestamp
                                    ),
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                }
            }
        }
    }
}