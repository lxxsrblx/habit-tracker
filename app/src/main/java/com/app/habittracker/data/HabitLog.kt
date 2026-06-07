package com.app.habittracker.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "habit_logs",
    foreignKeys = [
        ForeignKey(
            entity = Habit::class,
            parentColumns = ["id"],
            childColumns = ["habitId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("habitId")
    ]
)
data class HabitLog(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val habitId: Int,

    val completedAt: Long = System.currentTimeMillis(),

    val isSkipped: Boolean = false,

    val note: String? = null
)