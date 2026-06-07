package com.app.habittracker.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.habittracker.screens.components.AppBackground
import com.app.habittracker.screens.components.BottomNavBar
import com.app.habittracker.screens.components.HeaderChip
import com.app.habittracker.screens.components.settings.AboutCard
import com.app.habittracker.screens.components.settings.AppearanceCard
import com.app.habittracker.screens.components.settings.DataManagementCard
import com.app.habittracker.screens.components.settings.NotificationSettingsCard
import com.app.habittracker.screens.components.settings.ProfileCard
import com.app.habittracker.viewmodel.settings.SettingsViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen (
    viewModel: SettingsViewModel,
    onArchivedHabitsClick: () -> Unit = {},
    onDashboardClick: () -> Unit = {},
    onStatsClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    val backupStatus by viewModel.backupStatus.collectAsState()
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var showTimePicker by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        viewModel.saveSuccess.collectLatest {
            keyboardController?.hide()
            Toast.makeText(context, "Settings updated successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(backupStatus) {
        when (val status = backupStatus) {
            is SettingsViewModel.BackupStatus.Success -> {
                Toast.makeText(context, status.message, Toast.LENGTH_LONG).show()
                viewModel.clearBackupStatus()
            }
            is SettingsViewModel.BackupStatus.Error -> {
                Toast.makeText(context, status.message, Toast.LENGTH_LONG).show()
                viewModel.clearBackupStatus()
            }
            else -> {}
        }
    }

    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = state.reminderHour,
            initialMinute = state.reminderMinute
        )

        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onTimeChanged(timePickerState.hour, timePickerState.minute)
                    showTimePicker = false
                }) {
                    Text("OK", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            containerColor = Color(0xFF1E1E1E),
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }

    AppBackground {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp).padding(bottom = 100.dp)) {
                Spacer(modifier = Modifier.height(50.dp))

                HeaderChip(
                    icon = Icons.Default.Analytics,
                    text = "Settings"
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Text(
                    text = "Settings.",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = "Manage preferences and app information.",
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(32.dp))

                ProfileCard(
                    name = state.name,
                    onNameChange = viewModel::onNameChange,
                    onSave = viewModel::saveUser
                )

                Spacer(modifier = Modifier.height(20.dp))

                NotificationSettingsCard(
                    notificationsEnabled = state.reminderEnabled,
                    reminderHour = state.reminderHour,
                    reminderMinute = state.reminderMinute,
                    onNotificationsToggled = viewModel::onReminderToggled,
                    onTimeClick = { showTimePicker = true },
                    onSave = viewModel::saveUser
                )

                Spacer(modifier = Modifier.height(20.dp))

                AppearanceCard(
                    currentTheme = state.appTheme,
                    onThemeSelected = { newTheme ->
                        viewModel.onThemeChange(newTheme)
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                DataManagementCard(
                    onExport = viewModel::exportData,
                    onImport = viewModel::importData
                )

                Spacer(modifier = Modifier.height(20.dp))

                AboutCard()

                Spacer(modifier = Modifier.height(20.dp))

                TextButton(
                    onClick = onArchivedHabitsClick,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = "View Archived Habits",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            BottomNavBar(
                selected = "settings",
                onDashboardClick = onDashboardClick,
                onStatsClick = onStatsClick,
                onSettingsClick = onSettingsClick,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(
                        horizontal = 24.dp,
                        vertical = 50.dp
                    )
            )
        }
    }
}