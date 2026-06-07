package com.app.habittracker.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import com.app.habittracker.data.Habit
import com.app.habittracker.data.HabitLog
import com.app.habittracker.viewmodel.analytics.AchievementUi
import java.util.Calendar
import kotlin.collections.find

fun calculateConsistencyGrade(
    completion: Float
): String {

    return when {
        completion >= 0.90f -> "A+"
        completion >= 0.80f -> "A"
        completion >= 0.70f -> "B"
        completion >= 0.60f -> "C"
        else -> "D"
    }
}

fun buildLast7Days(
    logs: List<HabitLog>
): List<Boolean> {

    val today =
        Calendar.getInstance()

    today.set(
        Calendar.HOUR_OF_DAY,
        0
    )

    today.set(
        Calendar.MINUTE,
        0
    )

    today.set(
        Calendar.SECOND,
        0
    )

    today.set(
        Calendar.MILLISECOND,
        0
    )

    return (6 downTo 0).map { daysAgo ->

        val targetDay =
            Calendar.getInstance().apply {

                timeInMillis =
                    today.timeInMillis

                add(
                    Calendar.DAY_OF_YEAR,
                    -daysAgo
                )
            }

        logs.any { log ->

            val logDay =
                Calendar.getInstance().apply {
                    timeInMillis =
                        log.completedAt

                    set(
                        Calendar.HOUR_OF_DAY,
                        0
                    )

                    set(
                        Calendar.MINUTE,
                        0
                    )

                    set(
                        Calendar.SECOND,
                        0
                    )

                    set(
                        Calendar.MILLISECOND,
                        0
                    )
                }

            logDay.timeInMillis ==
                    targetDay.timeInMillis
        }
    }
}

fun buildAchievements(
    streak: Int,
    completion: Float,
    totalLogs: Int,
    habitsCount: Int,
    journeyDays: Int
): List<AchievementUi> {

    return listOf(

        AchievementUi(
            title = "First Step",
            description = "Complete your first habit",
            unlocked = totalLogs >= 1,
            icon = Icons.Default.Eco
        ),

        AchievementUi(
            title = "Getting Serious",
            description = "Complete 10 habits",
            unlocked = totalLogs >= 10,
            icon = Icons.Default.Insights
        ),

        AchievementUi(
            title = "Century Club",
            description = "Complete 100 habits",
            unlocked = totalLogs >= 100,
            icon = Icons.Default.EmojiEvents
        ),

        AchievementUi(
            title = "Habit Legend",
            description = "Complete 500 habits",
            unlocked = totalLogs >= 500,
            icon = Icons.Default.Diamond
        ),

        // Streaks

        AchievementUi(
            title = "On Fire",
            description = "Reach a 7 day streak",
            unlocked = streak >= 7,
            icon = Icons.Default.LocalFireDepartment
        ),

        AchievementUi(
            title = "Unstoppable",
            description = "Reach a 30 day streak",
            unlocked = streak >= 30,
            icon = Icons.Default.Bolt
        ),

        AchievementUi(
            title = "Iron Discipline",
            description = "Reach a 100 day streak",
            unlocked = streak >= 100,
            icon = Icons.Default.Shield
        ),

        // Consistency

        AchievementUi(
            title = "Consistent",
            description = "Maintain 70% completion",
            unlocked = completion >= 0.70f,
            icon = Icons.Default.CheckCircle
        ),

        AchievementUi(
            title = "Dedicated",
            description = "Maintain 85% completion",
            unlocked = completion >= 0.85f,
            icon = Icons.Default.Star
        ),

        AchievementUi(
            title = "Consistency Master",
            description = "Maintain 95% completion",
            unlocked = completion >= 0.95f,
            icon = Icons.Default.AutoAwesome
        ),

        // Habit Count

        AchievementUi(
            title = "Explorer",
            description = "Create 3 habits",
            unlocked = habitsCount >= 3,
            icon = Icons.Default.Explore
        ),

        AchievementUi(
            title = "Builder",
            description = "Create 5 habits",
            unlocked = habitsCount >= 5,
            icon = Icons.Default.Construction
        ),

        AchievementUi(
            title = "Architect",
            description = "Create 10 habits",
            unlocked = habitsCount >= 10,
            icon = Icons.Default.AccountTree
        ),

        // Journey

        AchievementUi(
            title = "One Week In",
            description = "Stay for 7 days",
            unlocked = journeyDays >= 7,
            icon = Icons.Default.CalendarMonth
        ),

        AchievementUi(
            title = "One Month Strong",
            description = "Stay for 30 days",
            unlocked = journeyDays >= 30,
            icon = Icons.Default.DateRange
        ),

        AchievementUi(
            title = "Veteran",
            description = "Stay for 100 days",
            unlocked = journeyDays >= 100,
            icon = Icons.Default.MilitaryTech
        )
    )
}

fun calculateOverallCompletion(
    habits: List<Habit>,
    totalLogs: Int
): Float {

    if (habits.isEmpty()) {
        return 0f
    }

    val today = System.currentTimeMillis()

    var totalPossible = 0

    habits.forEach { habit ->

        val daysActive =
            calculateJourneyDays(
                habit.createdAt
            )

        totalPossible += daysActive
    }

    if (totalPossible <= 0) {
        return 0f
    }

    return (
            totalLogs.toFloat() /
                    totalPossible.toFloat()
            ).coerceIn(0f, 1f)
}

fun calculateMostConsistentHabit(
    habits: List<Habit>,
    logs: List<HabitLog>
): Pair<String, Int> {

    val grouped =
        logs.groupBy { it.habitId }

    val winner =
        grouped.maxByOrNull {
            it.value.size
        }

    val habit =
        habits.find {
            it.id == winner?.key
        }

    return Pair(
        habit?.title ?: "None",
        winner?.value?.size ?: 0
    )
}