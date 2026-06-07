package com.app.habittracker.viewmodel.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.habittracker.repository.HabitLogRepository
import com.app.habittracker.repository.HabitRepository
import com.app.habittracker.repository.UserRepository
import com.app.habittracker.utils.buildAchievements
import com.app.habittracker.utils.buildLast7Days
import com.app.habittracker.utils.calculateConsistencyGrade
import com.app.habittracker.utils.calculateJourneyDays
import com.app.habittracker.utils.calculateOverallCompletion
import com.app.habittracker.utils.calculateStreak
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class AnalyticsViewModel(
    private val userRepository: UserRepository,
    private val habitRepository: HabitRepository,
    private val habitLogRepository: HabitLogRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(AnalyticsUiState())

    val uiState: StateFlow<AnalyticsUiState> =
        _uiState

    init {
        loadAnalytics()
    }

    private fun loadAnalytics() {

        viewModelScope.launch {

            combine(
                habitRepository.getAllHabits(),
                habitLogRepository.getAllLogsFlow()
            ) { habits, logs ->

                val user = userRepository.getUser()

                val journeyDays =
                    user?.let {
                        calculateJourneyDays(it.joinedAt)
                    } ?: 1

                val overallCompletion =
                    calculateOverallCompletion(
                        habits = habits,
                        totalLogs = logs.size
                    )

                val streak =
                    calculateStreak(
                        logs.map { it.completedAt }
                    )

                val grade =
                    calculateConsistencyGrade(
                        overallCompletion
                    )

                val grouped =
                    logs.groupBy { it.habitId }

                val bestHabit =
                    grouped.maxByOrNull {
                        it.value.size
                    }

                val habitName =
                    habits.find {
                        it.id == bestHabit?.key
                    }?.title ?: ""

                val completions =
                    bestHabit?.value?.size ?: 0

                val last7Days =
                    buildLast7Days(logs)

                val achievements =
                    buildAchievements(
                        streak = streak,
                        completion = overallCompletion,
                        totalLogs = logs.size,
                        habitsCount = habits.size,
                        journeyDays = journeyDays
                    )

                AnalyticsUiState(
                    overallCompletion = overallCompletion,
                    currentStreak = streak,
                    consistencyGrade = grade,
                    mostConsistentHabit = habitName,
                    mostConsistentHabitCompletions = completions,
                    last7DaysActivity = last7Days,
                    heatmapData = logs.map { it.completedAt },
                    unlockedAchievements =
                        achievements.count {
                            it.unlocked
                        },
                    achievements = achievements
                )
            }.collect {
                _uiState.value = it
            }
        }
    }
}