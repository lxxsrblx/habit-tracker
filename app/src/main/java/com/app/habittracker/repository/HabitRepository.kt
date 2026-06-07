package com.app.habittracker.repository

import com.app.habittracker.data.Habit
import com.app.habittracker.data.HabitDao
import kotlinx.coroutines.flow.Flow

class HabitRepository(
    private val habitDao: HabitDao
) {

    suspend fun insertHabit(
        habit: Habit
    ) {
        habitDao.insert(habit)
    }

    suspend fun insertHabitWithId(
        habit: Habit
    ): Long {
        return habitDao.insert(habit)
    }

    suspend fun updateHabit(
        habit: Habit
    ) {
        habitDao.update(habit)
    }

    suspend fun deleteHabit(
        habit: Habit
    ) {
        habitDao.delete(habit)
    }

    fun getAllHabits(): Flow<List<Habit>> {
        return habitDao.getAllHabits()
    }

    suspend fun getHabitById(
        id: Int
    ): Habit? {

        return habitDao.getHabitById(id)
    }
}