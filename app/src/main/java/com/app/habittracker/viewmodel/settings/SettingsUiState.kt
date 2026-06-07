package com.app.habittracker.viewmodel.settings

data class SettingsUiState(
    val name: String = "",
    val reminderEnabled: Boolean = false,
    val reminderHour: Int = 20,
    val reminderMinute: Int = 0,
    val appTheme: String = "Amber",
    val isLoading: Boolean = false
)