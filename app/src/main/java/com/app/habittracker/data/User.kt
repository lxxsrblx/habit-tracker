package com.app.habittracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val id: Int = 1,
    val name: String,
    val notificationsEnabled: Boolean = false,
    val reminderHour: Int = 20,
    val reminderMinute: Int = 0,
    val joinedAt: Long = System.currentTimeMillis(),
    val appTheme: String = "Amber",
    val xp: Int = 0,
    val level: Int = 1
)
