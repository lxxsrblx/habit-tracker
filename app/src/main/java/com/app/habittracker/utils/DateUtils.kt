package com.app.habittracker.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun calculateJourneyDays(
    joinedAt: Long
): Int {

    val diff =
        System.currentTimeMillis() - joinedAt

    return (diff / (1000 * 60 * 60 * 24))
        .toInt() + 1
}

fun Long.toDateOnly(): Long {
    return Calendar.getInstance().apply {
        timeInMillis = this@toDateOnly
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
}

fun startOfToday(): Long {

    return Calendar.getInstance().apply {

        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)

    }.timeInMillis
}

fun endOfToday(): Long {

    return Calendar.getInstance().apply {

        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
        set(Calendar.MILLISECOND, 999)

    }.timeInMillis
}

fun startOfWeek(): Long {

    val calendar =
        Calendar.getInstance()

    calendar.set(
        Calendar.DAY_OF_WEEK,
        Calendar.SUNDAY
    )

    calendar.set(
        Calendar.HOUR_OF_DAY,
        0
    )

    calendar.set(
        Calendar.MINUTE,
        0
    )

    calendar.set(
        Calendar.SECOND,
        0
    )

    calendar.set(
        Calendar.MILLISECOND,
        0
    )

    return calendar.timeInMillis
}

fun endOfWeek(): Long {

    val calendar =
        Calendar.getInstance()

    calendar.set(
        Calendar.DAY_OF_WEEK,
        Calendar.SATURDAY
    )

    calendar.set(
        Calendar.HOUR_OF_DAY,
        23
    )

    calendar.set(
        Calendar.MINUTE,
        59
    )

    calendar.set(
        Calendar.SECOND,
        59
    )

    calendar.set(
        Calendar.MILLISECOND,
        999
    )

    return calendar.timeInMillis
}

fun dayOfWeek(
    timestamp: Long
): Int {

    val calendar =
        Calendar.getInstance()

    calendar.timeInMillis =
        timestamp

    return calendar.get(
        Calendar.DAY_OF_WEEK
    )
}

fun calculateStreak(timestamps: List<Long>, daysOfWeek: String = "1,2,3,4,5,6,7"): Int {
    if (timestamps.isEmpty()) return 0
    
    val doneDays = timestamps.map { it.toDateOnly() }.toSet()
    val scheduledDays = daysOfWeek.split(",").mapNotNull { it.toIntOrNull() }.toSet()
    
    val calendar = Calendar.getInstance()
    var streak = 0
    val today = startOfToday()
    
    // We start from today and go backwards
    while (true) {
        val currentDayTimestamp = calendar.timeInMillis.toDateOnly()
        val currentDayNum = calendar.get(Calendar.DAY_OF_WEEK)
        
        if (currentDayNum in scheduledDays) {
            if (currentDayTimestamp in doneDays) {
                streak++
            } else {
                // If it's today and not done yet, we don't break the streak
                // because the user still has time to complete it.
                // But if it's BEFORE today and was due but not done, streak ends.
                if (currentDayTimestamp < today) {
                    break
                }
            }
        }
        
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        
        // Safety break to prevent infinite loops if something is wrong with timestamps
        if (calendar.timeInMillis < (timestamps.minOrNull() ?: today) - (365L * 24 * 3600 * 1000)) break
    }
    
    return streak
}

fun calculateBestStreak(
    timestamps: List<Long>
): Int {

    if (timestamps.isEmpty()) return 0

    val oneDayMs = 24 * 60 * 60 * 1000L

    val days = timestamps
        .map { it.toDateOnly() }
        .distinct()
        .sorted()

    var best = 1
    var current = 1

    for (i in 1 until days.size) {

        if (days[i] - days[i - 1] == oneDayMs) {
            current++
            best = maxOf(best, current)
        } else {
            current = 1
        }
    }

    return best
}

fun formatDate(
    timestamp: Long
): String {

    return SimpleDateFormat(
        "EEEE, MMM d",
        Locale.getDefault()
    ).format(
        Date(timestamp)
    )
}
