package com.app.habittracker.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Insert
    suspend fun insert(habit: Habit): Long

    @Update
    suspend fun update(habit: Habit)

    @Delete
    suspend fun delete(habit: Habit)

    @Query("SELECT * FROM habits ORDER BY id DESC")
    fun getAllHabits(): Flow<List<Habit>>

    @Query("SELECT * FROM habits")
    suspend fun getAllHabitsList(): List<Habit>

    @Query(
        """
        SELECT * FROM habits
        WHERE id = :id
        """
    )
    suspend fun getHabitById(
        id: Int
    ): Habit?
}