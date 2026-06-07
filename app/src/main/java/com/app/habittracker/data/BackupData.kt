package com.app.habittracker.data

data class BackupData(
    val user: User?,
    val habits: List<Habit>,
    val logs: List<HabitLog>
)
