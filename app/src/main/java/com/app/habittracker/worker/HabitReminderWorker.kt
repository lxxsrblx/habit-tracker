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

class HabitReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val db = AppDatabase.getInstance(applicationContext)

        val habitDao = db.habitDao()
        val habitLogDao = db.habitLogDao()
        val userDao = db.userDao()

        val user = userDao.getUser() ?: return Result.success()
        if (!user.notificationsEnabled) return Result.success()

        val habits = habitDao.getAllHabitsList()
        if (habits.isEmpty()) return Result.success()

        val start = startOfToday()
        val end = endOfToday()
        val logsToday = habitLogDao.getLogsForDay(start, end)
        val completedIds = logsToday.map { it.habitId }.toSet()

        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK).toString()
        val incompleteHabits = habits.filter { 
            !it.isArchived && 
            it.daysOfWeek.split(",").contains(currentDay) &&
            it.id !in completedIds 
        }

        if (incompleteHabits.isNotEmpty()) {
            showNotification(incompleteHabits.map { it.title })
        }

        user.let {
            HabitReminderScheduler.scheduleDailyReminder(
                applicationContext,
                it.reminderHour,
                it.reminderMinute
            )
        }

        return Result.success()
    }

    private fun showNotification(habitTitles: List<String>) {
        val count = habitTitles.size
        val habitText = if (count == 1) "habit" else "habits"
        val habitListText = if (count == 1) {
            "Your habit \"${habitTitles.first()}\" is waiting for you."
        } else {
            "You have $count $habitText to complete: ${habitTitles.joinToString(", ")}."
        }

        val builder = NotificationCompat.Builder(applicationContext, "HABIT_REMINDER_CHANNEL")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Don't break the streak!")
            .setContentText(habitListText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(habitListText))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(applicationContext)) {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                notify(1, builder.build())
            }
        }
    }
}