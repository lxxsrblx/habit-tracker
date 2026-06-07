package com.app.habittracker.worker

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

object HabitReminderScheduler {

    fun scheduleDailyReminder(context: Context, hour: Int, minute: Int) {
        val workManager = WorkManager.getInstance(context)

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val now = Calendar.getInstance()
        if (calendar.before(now)) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        val delay = calendar.timeInMillis - now.timeInMillis

        val workRequest = OneTimeWorkRequestBuilder<HabitReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .addTag("HABIT_REMINDER_WORK")
            .build()

        workManager.enqueueUniqueWork(
            "HabitReminderWork",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    fun scheduleHabitReminder(context: Context, habitId: Int, title: String, hour: Int, minute: Int) {
        val workManager = WorkManager.getInstance(context)
        
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        
        val now = Calendar.getInstance()
        if (calendar.before(now)) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        
        val delay = calendar.timeInMillis - now.timeInMillis
        
        val inputData = Data.Builder()
            .putInt("habitId", habitId)
            .putString("habitTitle", title)
            .build()
            
        val workRequest = OneTimeWorkRequestBuilder<IndividualHabitWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .addTag("HABIT_${habitId}")
            .build()
            
        workManager.enqueueUniqueWork(
            "HabitReminder_$habitId",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    fun cancelHabitReminder(context: Context, habitId: Int) {
        WorkManager.getInstance(context).cancelUniqueWork("HabitReminder_$habitId")
    }

    fun cancelReminder(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork("HabitReminderWork")
    }
}
