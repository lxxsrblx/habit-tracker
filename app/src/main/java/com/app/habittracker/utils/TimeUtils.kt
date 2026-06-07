package com.app.habittracker.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {
    fun formatTime12Hour(hour: Int, minute: Int): String {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }
        return SimpleDateFormat("h:mm a", Locale.getDefault()).format(calendar.time)
    }
}
