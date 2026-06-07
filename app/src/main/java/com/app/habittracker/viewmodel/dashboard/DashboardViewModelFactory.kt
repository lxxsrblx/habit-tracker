package com.app.habittracker.viewmodel.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.habittracker.repository.HabitLogRepository
import com.app.habittracker.repository.HabitRepository
import com.app.habittracker.repository.UserRepository

class DashboardViewModelFactory(
    private val userRepository: UserRepository,
    private val habitRepository: HabitRepository,
    private val habitLogRepository: HabitLogRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (
            modelClass.isAssignableFrom(
                DashboardViewModel::class.java
            )
        ) {

            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(
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