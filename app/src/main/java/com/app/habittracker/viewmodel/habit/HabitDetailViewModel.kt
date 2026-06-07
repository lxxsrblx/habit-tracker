package com.app.habittracker.viewmodel.habit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.habittracker.repository.HabitLogRepository
import com.app.habittracker.repository.HabitRepository
import com.app.habittracker.utils.calculateBestStreak
import com.app.habittracker.utils.calculateStreak
import com.app.habittracker.utils.dayOfWeek
import com.app.habittracker.utils.endOfWeek
import com.app.habittracker.utils.startOfWeek
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HabitDetailViewModel(
    private val habitRepository: HabitRepository,
    private val logRepository: HabitLogRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HabitDetailUiState())
    val uiState: StateFlow<HabitDetailUiState> = _uiState

    fun loadHabit(habitId: Int) {

        viewModelScope.launch {

            if (_uiState.value.id != habitId) {
                _uiState.value = HabitDetailUiState()
            }

            val habit = habitRepository.getHabitById(habitId)
                ?: return@launch

            val logs = logRepository.getLogsForHabit(habitId)

            val weekLogs = logs.filter {
                it.completedAt >= startOfWeek() &&
                        it.completedAt <= endOfWeek()
            }

            val completedDays = weekLogs
                .map { dayOfWeek(it.completedAt) }
                .toSet()

            val timestamps = logs.map { it.completedAt }

            val currentStreak = calculateStreak(timestamps, habit.daysOfWeek)
            val bestStreak = calculateBestStreak(timestamps)

            _uiState.value = HabitDetailUiState(
                id = habit.id,
                title = habit.title,
                category = habit.category,
                iconName = habit.iconName,
                daysOfWeek = habit.daysOfWeek,
                isReminderEnabled = habit.isReminderEnabled,
                reminderHour = habit.reminderHour,
                reminderMinute = habit.reminderMinute,
                isArchived = habit.isArchived,
                currentStreak = currentStreak,
                bestStreak = bestStreak,
                totalCompleted = logs.size,
                weeklyCompleted = completedDays.size,
                completedDays = completedDays,
                recentCompletions = timestamps,
                logs = logs,
                createdAt = habit.createdAt,
                isLoading = false
            )
        }
    }

    fun updateTitle(title: String) {
        _uiState.value = _uiState.value.copy(title = title)
    }

    fun clearHabit() {
        _uiState.value = HabitDetailUiState()
    }
}