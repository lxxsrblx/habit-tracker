package com.app.habittracker.repository

import com.app.habittracker.data.HabitLog
import com.app.habittracker.data.HabitLogDao

class HabitLogRepository(
    private val dao: HabitLogDao
) {

    suspend fun insertLog(
        log: HabitLog
    ) {
        dao.insert(log)
    }

    suspend fun updateLog(
        log: HabitLog
    ) {
        dao.update(log)
    }

    suspend fun deleteLog(
        log: HabitLog
    ) {
        dao.delete(log)
    }

    suspend fun getLogById(
        logId: Int
    ) = dao.getLogById(logId)

    suspend fun getLogForDay(
        habitId: Int,
        startOfDay: Long,
        endOfDay: Long
    ): HabitLog? {

        return dao.getLogForDay(
            habitId,
            startOfDay,
            endOfDay
        )
    }

    suspend fun getLogsForDay(
        startOfDay: Long,
        endOfDay: Long
    ): List<HabitLog> {

        return dao.getLogsForDay(
            startOfDay,
            endOfDay
        )
    }

    suspend fun getLogsBetween(
        start: Long,
        end: Long
    ): List<HabitLog> {

        return dao.getLogsBetween(
            start,
            end
        )
    }

    suspend fun getLogsForHabit(
        habitId: Int
    ) =
        dao.getLogsForHabit(habitId)

    fun getLogsForDayFlow(
        startOfDay: Long,
        endOfDay: Long
    ) = dao.getLogsForDayFlow(
        startOfDay,
        endOfDay
    )

    fun getLogsBetweenFlow(
        start: Long,
        end: Long
    ) = dao.getLogsBetweenFlow(
        start,
        end
    )

    fun getAllLogsFlow() = dao.getAllLogsFlow()
}