package com.app.habittracker.worker

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.app.habittracker.R
import com.app.habittracker.data.AppDatabase
import com.app.habittracker.utils.endOfToday
import com.app.habittracker.utils.startOfToday
import java.util.Calendar

class IndividualHabitWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val habitId = inputData.getInt("habitId", -1)
        val habitTitle = inputData.getString("habitTitle") ?: "Your habit"

        if (habitId == -1) return Result.success()

        val db = AppDatabase.getInstance(applicationContext)
        val habit = db.habitDao().getHabitById(habitId) ?: return Result.success()

        // Check if habit is archived or not scheduled for today
        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK).toString()
        if (habit.isArchived || !habit.daysOfWeek.split(",").contains(currentDay)) return Result.success()

        // Check if already completed today
        val existingLog = db.habitLogDao().getLogForDay(habitId, startOfToday(), endOfToday())
        if (existingLog != null) return Result.success()

        showNotification(habitId, habitTitle)

        // Reschedule for next day
        HabitReminderScheduler.scheduleHabitReminder(
            applicationContext,
            habitId,
            habitTitle,
            habit.reminderHour,
            habit.reminderMinute
        )

        return Result.success()
    }

    private fun showNotification(id: Int, title: String) {
        val builder = NotificationCompat.Builder(applicationContext, "HABIT_REMINDER_CHANNEL")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Habit Reminder")
            .setContentText("Time for: $title")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(applicationContext)) {
            if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                notify(id + 1000, builder.build()) // offset to avoid conflict with global reminder
            }
        }
    }
}
