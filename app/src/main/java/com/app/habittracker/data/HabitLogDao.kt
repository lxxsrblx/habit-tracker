package com.app.habittracker.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitLogDao {

    @Insert
    suspend fun insert(log: HabitLog)

    @Update
    suspend fun update(log: HabitLog)

    @Delete
    suspend fun delete(log: HabitLog)

    @Query("SELECT * FROM habit_logs WHERE id = :logId")
    suspend fun getLogById(logId: Int): HabitLog?

    @Query("""
        SELECT * FROM habit_logs
        WHERE habitId = :habitId
        AND completedAt BETWEEN :startOfDay AND :endOfDay
        LIMIT 1
    """)
    suspend fun getLogForDay(
        habitId: Int,
        startOfDay: Long,
        endOfDay: Long
    ): HabitLog?

    @Query("""
        SELECT * FROM habit_logs
        WHERE completedAt BETWEEN :startOfDay AND :endOfDay
    """)
    suspend fun getLogsForDay(
        startOfDay: Long,
        endOfDay: Long
    ): List<HabitLog>

    @Query("""
        SELECT * FROM habit_logs
        WHERE completedAt BETWEEN
        :start AND :end
    """)
    suspend fun getLogsBetween(
        start: Long,
        end: Long
    ): List<HabitLog>

    @Query(
        """
    SELECT * FROM habit_logs
    WHERE habitId = :habitId
    ORDER BY completedAt DESC
    """
    )
    suspend fun getLogsForHabit(
        habitId: Int
    ): List<HabitLog>

    @Query("""
        SELECT * FROM habit_logs
        WHERE completedAt BETWEEN :startOfDay AND :endOfDay
    """)
    fun getLogsForDayFlow(
        startOfDay: Long,
        endOfDay: Long
    ): Flow<List<HabitLog>>

    @Query("""
        SELECT * FROM habit_logs
        WHERE completedAt BETWEEN :start AND :end
    """)
    fun getLogsBetweenFlow(
        start: Long,
        end: Long
    ): Flow<List<HabitLog>>

    @Query("""
        SELECT * FROM habit_logs
    """)
    fun getAllLogsFlow(): Flow<List<HabitLog>>

    @Query("SELECT * FROM habit_logs")
    suspend fun getAllLogsList(): List<HabitLog>
}