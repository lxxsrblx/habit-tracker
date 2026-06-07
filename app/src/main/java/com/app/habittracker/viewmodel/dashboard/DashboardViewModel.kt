package com.app.habittracker.viewmodel.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.habittracker.data.Habit
import com.app.habittracker.data.HabitLog
import com.app.habittracker.data.User
import com.app.habittracker.repository.HabitLogRepository
import com.app.habittracker.repository.HabitRepository
import com.app.habittracker.repository.UserRepository
import com.app.habittracker.utils.calculateJourneyDays
import com.app.habittracker.utils.calculateStreak
import com.app.habittracker.utils.dayOfWeek
import com.app.habittracker.utils.endOfToday
import com.app.habittracker.utils.startOfToday
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar

class DashboardViewModel(
    userRepository: UserRepository,
    habitRepository: HabitRepository,
    habitLogRepository: HabitLogRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedCategory = MutableStateFlow("All")
    private val _showAllHabits = MutableStateFlow(false)

    @Suppress("UNCHECKED_CAST")
    val uiState: StateFlow<DashboardUiState> = combine(
        userRepository.getUserFlow(),
        habitRepository.getAllHabits(),
        habitLogRepository.getLogsForDayFlow(startOfToday(), endOfToday()),
        habitLogRepository.getAllLogsFlow(),
        _searchQuery,
        _selectedCategory,
        _showAllHabits
    ) { params: Array<Any?> ->
        val user = params[0] as User?
        val allHabits = params[1] as List<Habit>
        val todayLogs = params[2] as List<HabitLog>
        val allLogs = params[3] as List<HabitLog>
        val query = params[4] as String
        val category = params[5] as String
        val showAll = params[6] as Boolean

        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)

        val filteredHabits = allHabits.filter { habit ->
            val isScheduledToday = habit.daysOfWeek.split(",").contains(currentDay.toString())
            val matchesDay = !habit.isArchived && (showAll || isScheduledToday)
            val matchesQuery = habit.title.contains(query, ignoreCase = true)
            val matchesCategory = category == "All" || habit.category == category

            matchesDay && matchesQuery && matchesCategory
        }

        val activeHabits = filteredHabits.size
        val completedHabitIds = todayLogs.filter { !it.isSkipped }.map { it.habitId }.toSet()
        val skippedHabitIds = todayLogs.filter { it.isSkipped }.map { it.habitId }.toSet()
        val completedToday = completedHabitIds.size

        val weekLogs =
            allLogs.filter { it.completedAt >= startOfToday().let { today -> today - (7 * 24 * 3600 * 1000) } && it.completedAt <= endOfToday() }

        val completedHabitDays = weekLogs
            .groupBy { it.habitId }
            .mapValues { entry ->
                entry.value.map { dayOfWeek(it.completedAt) }.toSet()
            }
        val habitStreaks = filteredHabits.associate { habit ->
            val logs = allLogs.filter { it.habitId == habit.id }
            habit.id to calculateStreak(logs.map { it.completedAt }, habit.daysOfWeek)
        }
        val progress =
            if (activeHabits == 0) 0f
            else completedToday.toFloat() / activeHabits.toFloat()

        DashboardUiState(
            userName = user?.name ?: "",
            journeyDays = user?.let { calculateJourneyDays(it.joinedAt) } ?: 1,
            activeHabits = activeHabits,
            completedToday = completedToday,
            progress = progress,
            habits = filteredHabits,
            completedHabitIds = completedHabitIds,
            skippedHabitIds = skippedHabitIds,
            completedHabitDays = completedHabitDays,
            habitStreaks = habitStreaks,
            totalXp = user?.xp ?: 0,
            level = user?.level ?: 1,
            searchQuery = query,
            selectedCategory = category,
            showAllHabits = showAll
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000),
        initialValue = DashboardUiState()
    )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onCategoryChange(category: String) {
        _selectedCategory.value = category
    }

    fun onToggleShowAll(showAll: Boolean) {
        _showAllHabits.value = showAll
    }
}