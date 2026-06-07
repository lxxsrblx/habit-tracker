package com.app.habittracker.viewmodel.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.habittracker.repository.BackupRepository
import com.app.habittracker.repository.UserRepository

class SettingsViewModelFactory(
    private val userRepository: UserRepository,
    private val backupRepository: BackupRepository,
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(userRepository, backupRepository, context) as T
        }

        throw IllegalArgumentException("Unknown ViewModel")
    }
}