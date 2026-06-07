package com.app.habittracker.viewmodel.dashboard

import com.app.habittracker.data.Habit

data class DashboardUiState(
    val userName: String = "",
    val journeyDays: Int = 1,
    val activeHabits: Int = 0,
    val completedToday: Int = 0,
    val progress: Float = 0f,
    val habits: List<Habit> = emptyList(),
    val completedHabitIds: Set<Int> = emptySet(),
    val skippedHabitIds: Set<Int> = emptySet(),
    val completedHabitDays: Map<Int, Set<Int>> = emptyMap(),
    val habitStreaks: Map<Int, Int> = emptyMap(),
    val totalXp: Int = 0,
    val level: Int = 1,
    val searchQuery: String = "",
    val selectedCategory: String = "All",
    val showAllHabits: Boolean = false
)