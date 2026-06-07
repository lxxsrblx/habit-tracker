package com.app.habittracker.repository

import android.content.Context
import android.net.Uri
import androidx.room.withTransaction
import com.app.habittracker.data.AppDatabase
import com.app.habittracker.data.BackupData
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

class BackupRepository(
    private val db: AppDatabase,
    private val context: Context
) {
    private val gson = Gson()

    suspend fun exportData(uri: Uri): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val user = db.userDao().getUser()
            val habits = db.habitDao().getAllHabitsList()
            val logs = db.habitLogDao().getAllLogsList()

            val backupData = BackupData(user, habits, logs)
            val json = gson.toJson(backupData)

            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write(json.toByteArray())
            } ?: return@withContext Result.failure(Exception("Could not open output stream"))

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun importData(uri: Uri): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri) 
                ?: return@withContext Result.failure(Exception("Could not open input stream"))
            
            val reader = BufferedReader(InputStreamReader(inputStream))
            val json = reader.readText()
            val backupData = gson.fromJson(json, BackupData::class.java)

            db.withTransaction {
                db.clearAllTables() 
                
                backupData.user?.let { db.userDao().insert(it) }
                backupData.habits.forEach { db.habitDao().insert(it) }
                backupData.logs.forEach { db.habitLogDao().insert(it) }
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
