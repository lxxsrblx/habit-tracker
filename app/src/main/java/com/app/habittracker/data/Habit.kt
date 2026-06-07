package com.app.habittracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class Habit(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val category: String = "Other",
    val iconName: String = "TaskAlt",
    val isArchived: Boolean = false,
    val daysOfWeek: String = "1,2,3,4,5,6,7", // 1=Sun, 7=Sat
    val isReminderEnabled: Boolean = false,
    val reminderHour: Int = 20,
    val reminderMinute: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)