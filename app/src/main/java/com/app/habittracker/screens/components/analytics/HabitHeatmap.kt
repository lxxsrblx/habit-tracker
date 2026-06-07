package com.app.habittracker.screens.components.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.util.Calendar

@Composable
fun HabitHeatmap(
    completions: List<Long>
) {
    val primary = MaterialTheme.colorScheme.primary
    
    // Group logs by day
    val groupedLogs = completions.groupBy {
        val cal = Calendar.getInstance()
        cal.timeInMillis = it
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        cal.timeInMillis
    }.mapValues { it.value.size }

    // Last 12 weeks (84 days)
    val weeksToShow = 12
    val daysToShow = weeksToShow * 7
    
    val today = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
        // Adjust to the end of the current week (Saturday)
        while (get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
            add(Calendar.DAY_OF_YEAR, 1)
        }
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFF121212),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "ACTIVITY MAP",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // We'll draw 12 columns, each representing a week
                for (w in (weeksToShow - 1) downTo 0) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        for (d in 6 downTo 0) {
                            val dayCal = Calendar.getInstance().apply {
                                timeInMillis = today.timeInMillis
                                add(Calendar.DAY_OF_YEAR, -(w * 7 + d))
                            }
                            
                            val count = groupedLogs[dayCal.timeInMillis] ?: 0
                            
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .background(
                                        color = when {
                                            count >= 5 -> primary
                                            count >= 3 -> primary.copy(alpha = 0.7f)
                                            count >= 1 -> primary.copy(alpha = 0.3f)
                                            else -> Color(0xFF1E1E1E)
                                        },
                                        shape = RoundedCornerShape(3.dp)
                                    )
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Less",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
                
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    listOf(0f, 0.3f, 0.7f, 1f).forEach { alpha ->
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(
                                    if (alpha == 0f) Color(0xFF1E1E1E) else primary.copy(alpha = alpha),
                                    shape = RoundedCornerShape(2.dp)
                                )
                        )
                    }
                }
                
                Text(
                    text = "More",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
        }
    }
}
