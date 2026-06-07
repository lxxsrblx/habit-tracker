package com.app.habittracker.viewmodel.habit

import com.app.habittracker.data.HabitLog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class HabitDetailUiState(
    val id: Int = 0,
    val title: String = "",
    val category: String = "Other",
    val iconName: String = "TaskAlt",
    val daysOfWeek: String = "1,2,3,4,5,6,7",
    val isReminderEnabled: Boolean = false,
    val reminderHour: Int = 20,
    val reminderMinute: Int = 0,
    val isArchived: Boolean = false,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val totalCompleted: Int = 0,
    val weeklyCompleted: Int = 0,
    val completedDays: Set<Int> = emptySet(),
    val recentCompletions: List<Long> = emptyList(),
    val logs: List<HabitLog> = emptyList(),
    val createdAt: Long = 0L,
    val isLoading: Boolean = false
) {
    val formattedCreatedAt: String
        get() = if (createdAt > 0) {
            SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                .format(Date(createdAt))
        } else {
            ""
        }
}