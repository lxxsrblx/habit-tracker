package com.app.habittracker.viewmodel.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.habittracker.repository.HabitLogRepository
import com.app.habittracker.repository.HabitRepository
import com.app.habittracker.repository.UserRepository

class AnalyticsViewModelFactory(
    private val userRepository: UserRepository,
    private val habitRepository: HabitRepository,
    private val habitLogRepository: HabitLogRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (
            modelClass.isAssignableFrom(
                AnalyticsViewModel::class.java
            )
        ) {

            @Suppress("UNCHECKED_CAST")
            return AnalyticsViewModel(
                userRepository,
                habitRepository,
                habitLogRepository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel"
        )
    }
}