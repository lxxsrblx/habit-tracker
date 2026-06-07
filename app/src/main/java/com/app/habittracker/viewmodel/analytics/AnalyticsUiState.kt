package com.app.habittracker.viewmodel.analytics

import androidx.compose.ui.graphics.vector.ImageVector

data class AnalyticsUiState(
    val overallCompletion: Float = 0f,
    val currentStreak: Int = 0,
    val consistencyGrade: String = "N/A",
    val mostConsistentHabit: String = "",
    val mostConsistentHabitCompletions: Int = 0,
    val last7DaysActivity: List<Boolean> = List(7) { false },
    val heatmapData: List<Long> = emptyList(),
    val unlockedAchievements: Int = 0,
    val totalAchievements: Int = 16,
    val achievements: List<AchievementUi> = emptyList()
)

data class AchievementUi(
    val title: String,
    val description: String,
    val unlocked: Boolean,
    val icon: ImageVector
)