package com.app.habittracker.viewmodel.habit

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.habittracker.data.Habit
import com.app.habittracker.data.HabitLog
import com.app.habittracker.repository.HabitLogRepository
import com.app.habittracker.repository.HabitRepository
import com.app.habittracker.utils.endOfToday
import com.app.habittracker.utils.endOfWeek
import com.app.habittracker.utils.startOfToday
import com.app.habittracker.utils.startOfWeek
import com.app.habittracker.worker.HabitReminderScheduler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HabitViewModel(
    private val repository: HabitRepository,
    private val logRepository: HabitLogRepository,
    private val context: Context
) : ViewModel() {

    val habits = repository
        .getAllHabits()
        .stateIn(
            viewModelScope,
            SharingStarted.Companion.WhileSubscribed(5000),
            emptyList()
        )

    val archivedHabits = repository
        .getAllHabits()
        .map { habits -> habits.filter { it.isArchived } }
        .stateIn(
            viewModelScope,
            SharingStarted.Companion.WhileSubscribed(5000),
            emptyList()
        )

    fun addHabit(
        title: String,
        category: String = "Other",
        iconName: String = "TaskAlt",
        daysOfWeek: String = "1,2,3,4,5,6,7",
        isReminderEnabled: Boolean = false,
        reminderHour: Int = 20,
        reminderMinute: Int = 0
    ) {

        viewModelScope.launch {

            val habit = Habit(
                title = title,
                category = category,
                iconName = iconName,
                daysOfWeek = daysOfWeek,
                isReminderEnabled = isReminderEnabled,
                reminderHour = reminderHour,
                reminderMinute = reminderMinute
            )

            val habitId = repository.insertHabitWithId(habit)

            if (isReminderEnabled) {
                HabitReminderScheduler.scheduleHabitReminder(context, habitId.toInt(), title, reminderHour, reminderMinute)
            }
        }
    }

    fun updateHabitFull(
        habitId: Int,
        title: String,
        category: String,
        iconName: String,
        daysOfWeek: String,
        isReminderEnabled: Boolean,
        reminderHour: Int,
        reminderMinute: Int,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            repository.getHabitById(habitId)?.let { habit ->
                val updatedHabit = habit.copy(
                    title = title,
                    category = category,
                    iconName = iconName,
                    daysOfWeek = daysOfWeek,
                    isReminderEnabled = isReminderEnabled,
                    reminderHour = reminderHour,
                    reminderMinute = reminderMinute
                )
                repository.updateHabit(updatedHabit)

                if (isReminderEnabled) {
                    HabitReminderScheduler.scheduleHabitReminder(context, habitId, title, reminderHour, reminderMinute)
                } else {
                    HabitReminderScheduler.cancelHabitReminder(context, habitId)
                }

                onSuccess()
            }
        }
    }

    fun deleteHabit(
        habit: Habit
    ) {

        viewModelScope.launch {
            HabitReminderScheduler.cancelHabitReminder(context, habit.id)
            repository.deleteHabit(
                habit
            )
        }
    }

    fun archiveHabit(habitId: Int, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.getHabitById(habitId)?.let { habit ->
                HabitReminderScheduler.cancelHabitReminder(context, habitId)
                repository.updateHabit(habit.copy(isArchived = true))
                onComplete()
            }
        }
    }

    fun unarchiveHabit(habitId: Int, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.getHabitById(habitId)?.let { habit ->
                repository.updateHabit(habit.copy(isArchived = false))
                // If it was enabled before, re-schedule?
                // For simplicity, let's keep it disabled on restore or user can edit.
                onComplete()
            }
        }
    }

    fun toggleHabit(
        habitId: Int,
        note: String? = null,
        onResult: (Boolean) -> Unit = {}
    ) {

        viewModelScope.launch {

            val existingLog =
                logRepository.getLogForDay(
                    habitId,
                    startOfToday(),
                    endOfToday()
                )

            if (existingLog == null) {

                logRepository.insertLog(
                    HabitLog(
                        habitId = habitId,
                        note = note
                    )
                )
                onResult(true)

            } else {

                logRepository.deleteLog(
                    existingLog
                )
                onResult(false)
            }
        }
    }

    fun skipHabit(habitId: Int, onFail: (String) -> Unit) {
        viewModelScope.launch {
            // Check if already skipped this week
            val start = startOfWeek()
            val end = endOfWeek()
            val weekLogs = logRepository.getLogsForHabit(habitId).filter {
                it.completedAt in start..end
            }

            if (weekLogs.any { it.isSkipped }) {
                onFail("You already used your lifeline for this habit this week!")
                return@launch
            }

            // Check if already logged for today
            val todayLog = logRepository.getLogForDay(habitId, startOfToday(), endOfToday())
            if (todayLog != null) {
                onFail("Day already logged!")
                return@launch
            }

            logRepository.insertLog(
                HabitLog(
                    habitId = habitId,
                    isSkipped = true
                )
            )
        }
    }

    fun updateLogNote(logId: Int, note: String, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            logRepository.getLogById(logId)?.let { log ->
                logRepository.updateLog(log.copy(note = note))
                onComplete()
            }
        }
    }

    fun deleteHabitById(
        habitId: Int,
        onDeleted: () -> Unit = {}
    ) {
        viewModelScope.launch {

            val habit = repository.getHabitById(habitId)
                ?: return@launch

            HabitReminderScheduler.cancelHabitReminder(context, habitId)
            repository.deleteHabit(habit)

            onDeleted()
        }
    }
}