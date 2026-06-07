package com.app.habittracker.viewmodel.settings

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.habittracker.data.User
import com.app.habittracker.repository.BackupRepository
import com.app.habittracker.repository.UserRepository
import com.app.habittracker.worker.HabitReminderScheduler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val userRepository: UserRepository,
    private val backupRepository: BackupRepository,
    private val context: Context
) : ViewModel() {

    private val _saveSuccess = Channel<Unit>(Channel.Factory.CONFLATED)
    val saveSuccess = _saveSuccess.receiveAsFlow()

    private val _backupStatus = MutableStateFlow<BackupStatus>(BackupStatus.Idle)
    val backupStatus = _backupStatus.asStateFlow()

    sealed class BackupStatus {
        object Idle : BackupStatus()
        object Loading : BackupStatus()
        data class Success(val message: String) : BackupStatus()
        data class Error(val message: String) : BackupStatus()
    }

    private val _uiState = MutableStateFlow(
        SettingsUiState()
    )
    val uiState = _uiState.asStateFlow()

    init {
        loadUser()
    }

    fun exportData(uri: Uri) {
        viewModelScope.launch {
            _backupStatus.value = BackupStatus.Loading
            backupRepository.exportData(uri)
                .onSuccess {
                    _backupStatus.value = BackupStatus.Success("Data exported successfully!")
                }
                .onFailure {
                    _backupStatus.value = BackupStatus.Error("Export failed: ${it.message}")
                }
        }
    }

    fun importData(uri: Uri) {
        viewModelScope.launch {
            _backupStatus.value = BackupStatus.Loading
            backupRepository.importData(uri)
                .onSuccess {
                    loadUser() // Reload settings
                    _backupStatus.value = BackupStatus.Success("Data imported successfully!")
                }
                .onFailure {
                    _backupStatus.value = BackupStatus.Error("Import failed: ${it.message}")
                }
        }
    }

    fun clearBackupStatus() {
        _backupStatus.value = BackupStatus.Idle
    }

    private fun loadUser() {
        viewModelScope.launch {
            val user = userRepository.getUser()

            _uiState.update {
                it.copy(
                    name = user?.name ?: "",
                    reminderEnabled = user?.notificationsEnabled ?: false,
                    reminderHour = user?.reminderHour ?: 20,
                    reminderMinute = user?.reminderMinute ?: 0,
                    appTheme = user?.appTheme ?: "Amber"
                )
            }
        }
    }

    fun onNameChange(name: String) {
        _uiState.update {
            it.copy(name = name)
        }
    }

    fun onThemeChange(theme: String) {
        _uiState.update {
            it.copy(appTheme = theme)
        }
        saveUser(isSilent = true)
    }

    fun onReminderToggled(enabled: Boolean) {
        _uiState.update {
            it.copy(reminderEnabled = enabled)
        }
    }

    fun onTimeChanged(hour: Int, minute: Int) {
        _uiState.update {
            it.copy(reminderHour = hour, reminderMinute = minute)
        }
    }

    fun saveUser(isSilent: Boolean = false) {
        viewModelScope.launch {

            val state = _uiState.value
            val currentUser = userRepository.getUser()

            userRepository.saveUser(
                User(
                    id = 1,
                    name = state.name,
                    notificationsEnabled = state.reminderEnabled,
                    reminderHour = state.reminderHour,
                    reminderMinute = state.reminderMinute,
                    joinedAt = currentUser?.joinedAt ?: System.currentTimeMillis(),
                    appTheme = state.appTheme,
                    xp = currentUser?.xp ?: 0,
                    level = currentUser?.level ?: 1
                )
            )

            if (state.reminderEnabled) {
                HabitReminderScheduler.scheduleDailyReminder(
                    context,
                    state.reminderHour,
                    state.reminderMinute
                )
            } else {
                HabitReminderScheduler.cancelReminder(context)
            }

            val updatedUser = userRepository.getUser()

            _uiState.update {
                it.copy(
                    name = updatedUser?.name ?: "",
                    reminderEnabled = updatedUser?.notificationsEnabled ?: false,
                    reminderHour = updatedUser?.reminderHour ?: 20,
                    reminderMinute = updatedUser?.reminderMinute ?: 0,
                    appTheme = updatedUser?.appTheme ?: "Amber"
                )
            }
            if (!isSilent) {
                _saveSuccess.trySend(Unit)
            }
        }
    }
}
