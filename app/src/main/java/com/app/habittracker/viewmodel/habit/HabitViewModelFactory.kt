package com.app.habittracker.viewmodel.habit

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.habittracker.repository.HabitLogRepository
import com.app.habittracker.repository.HabitRepository

class HabitViewModelFactory(
    private val habitRepository: HabitRepository,
    private val logRepository: HabitLogRepository,
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (
            modelClass.isAssignableFrom(
                HabitViewModel::class.java
            )
        ) {

            @Suppress("UNCHECKED_CAST")
            return HabitViewModel(
                habitRepository,
                logRepository,
                context
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel"
        )
    }
}