package com.app.habittracker.viewmodel.habit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.habittracker.repository.HabitLogRepository
import com.app.habittracker.repository.HabitRepository

class HabitDetailViewModelFactory(
    private val habitRepository: HabitRepository,
    private val logRepository: HabitLogRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (
            modelClass.isAssignableFrom(
                HabitDetailViewModel::class.java
            )
        ) {

            @Suppress("UNCHECKED_CAST")

            return HabitDetailViewModel(
                habitRepository,
                logRepository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel"
        )
    }
}