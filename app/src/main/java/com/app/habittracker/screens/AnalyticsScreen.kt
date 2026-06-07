package com.app.habittracker.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.habittracker.screens.components.AppBackground
import com.app.habittracker.screens.components.BottomNavBar
import com.app.habittracker.screens.components.HeaderChip
import com.app.habittracker.viewmodel.analytics.AnalyticsUiState
import com.app.habittracker.screens.components.analytics.OverallCompletionCard
import com.app.habittracker.screens.components.StatCard
import com.app.habittracker.screens.components.analytics.AchievementsCard
import com.app.habittracker.screens.components.analytics.Last7DaysCard
import com.app.habittracker.screens.components.analytics.MostConsistentHabitCard
import com.app.habittracker.screens.components.analytics.HabitHeatmap

@Composable
fun AnalyticsScreen(
    state: AnalyticsUiState,
    onDashboardClick: () -> Unit = {},
    onStatsClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
) {
    AppBackground {

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(24.dp)
                    .padding(bottom = 100.dp)
            ) {
                Spacer(
                    modifier = Modifier.height(50.dp)
                )

                HeaderChip(
                    icon = Icons.Default.Analytics,
                    text = "Your Progress"
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Text(
                    text = "Statistics &",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "Insights.",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = "Track consistency. Build momentum.",
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(24.dp))

                OverallCompletionCard(
                    progress = state.overallCompletion
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "CURRENT STREAK",
                        value = state.currentStreak.toString(),
                        subtitle = "days",
                        valueColor = Color.White
                    )

                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "CONSISTENCY",
                        value = state.consistencyGrade,
                        subtitle = "overall grade",
                        valueColor = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                MostConsistentHabitCard(
                    title = state.mostConsistentHabit,
                    completions = state.mostConsistentHabitCompletions
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Last7DaysCard(
                    activity = state.last7DaysActivity
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                HabitHeatmap(
                    completions = state.heatmapData
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                AchievementsCard(
                    achievements = state.achievements,
                    unlocked = state.unlockedAchievements,
                    total = state.totalAchievements
                )

            }

            BottomNavBar(
                selected = "stats",
                onDashboardClick = onDashboardClick,
                onStatsClick = onStatsClick,
                onSettingsClick = onSettingsClick,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(
                        horizontal = 24.dp,
                        vertical = 50.dp
                    )
            )
        }
    }
}