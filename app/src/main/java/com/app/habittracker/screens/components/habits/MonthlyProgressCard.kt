package com.app.habittracker.screens.components.habits

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.util.Calendar
import java.util.Locale

@Composable
fun MonthlyProgressCard(
    completionLogs: List<Long>
) {
    val today = Calendar.getInstance()
    var selectedYear by remember { mutableStateOf(today.get(Calendar.YEAR)) }
    var selectedMonth by remember { mutableStateOf(today.get(Calendar.MONTH)) }

    val completedDays = remember(completionLogs, selectedYear, selectedMonth) {
        completionLogs.map { timestamp ->
            Calendar.getInstance().apply { timeInMillis = timestamp }
        }.filter { cal ->
            cal.get(Calendar.YEAR) == selectedYear &&
                    cal.get(Calendar.MONTH) == selectedMonth
        }.map { cal ->
            cal.get(Calendar.DAY_OF_MONTH)
        }.toSet()
    }

    val firstDayOfMonth = Calendar.getInstance().apply {
        set(Calendar.YEAR, selectedYear)
        set(Calendar.MONTH, selectedMonth)
        set(Calendar.DAY_OF_MONTH, 1)
    }
    val firstDayOffset = firstDayOfMonth.get(Calendar.DAY_OF_WEEK) - 1

    val daysInMonth = firstDayOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH)

    val monthName = firstDayOfMonth.getDisplayName(
        Calendar.MONTH,
        Calendar.LONG,
        Locale.getDefault()
    ) ?: ""

    fun goToPreviousMonth() {
        if (selectedMonth == 0) {
            selectedMonth = 11
            selectedYear -= 1
        } else {
            selectedMonth -= 1
        }
    }

    fun goToNextMonth() {
        if (selectedMonth == 11) {
            selectedMonth = 0
            selectedYear += 1
        } else {
            selectedMonth += 1
        }
    }

    val cellSize = 30.dp

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF121212)
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$monthName $selectedYear",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "‹",
                    color = Color.Gray,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF2A2A2A))
                        .clickable { goToPreviousMonth() }
                        .padding(horizontal = 10.dp, vertical = 2.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "›",
                    color = Color.Gray,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF2A2A2A))
                        .clickable { goToNextMonth() }
                        .padding(horizontal = 10.dp, vertical = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("S", "M", "T", "W", "T", "F", "S").forEach { day ->
                    Box(
                        modifier = Modifier.size(cellSize),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day,
                            color = Color.Gray,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val cells: List<Int?> =
                MutableList<Int?>(firstDayOffset) { null } + (1..daysInMonth).toList()

            cells.chunked(7).forEach { week ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    repeat(7) { index ->
                        val day = week.getOrNull(index)

                        if (day == null) {
                            Spacer(modifier = Modifier.size(cellSize))
                        } else {
                            val isCompleted = day in completedDays
                            Box(
                                modifier = Modifier
                                    .size(cellSize)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isCompleted) MaterialTheme.colorScheme.primary
                                        else Color(0xFF2A2A2A)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = day.toString(),
                                    color = if (isCompleted) Color.Black else Color.White,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.primary)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Completed",
                    color = Color.Gray,
                    style = MaterialTheme.typography.labelSmall
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF2A2A2A))
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Missed",
                    color = Color.Gray,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}