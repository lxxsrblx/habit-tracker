package com.app.habittracker.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.habittracker.screens.components.AppBackground
import com.app.habittracker.screens.components.BottomNavBar
import com.app.habittracker.screens.components.habits.ConfirmationDialog
import com.app.habittracker.screens.components.habits.EditHabitDialog
import com.app.habittracker.screens.components.habits.HabitJournal
import com.app.habittracker.screens.components.habits.HabitMenuButton
import com.app.habittracker.screens.components.JourneyChip
import com.app.habittracker.utils.TimeUtils
import com.app.habittracker.viewmodel.habit.HabitDetailUiState
import com.app.habittracker.screens.components.habits.HabitSummaryCard
import com.app.habittracker.screens.components.habits.MonthlyProgressCard
import com.app.habittracker.screens.components.StatTile
import com.app.habittracker.screens.components.habits.WeeklyProgressCard

@Composable
fun HabitDetailScreen(
    state: HabitDetailUiState,
    onBack: () -> Unit,
    onUpdateHabit: (Int, String, String, String, String, Boolean, Int, Int) -> Unit,
    onUpdateNote: (Int, String) -> Unit,
    onArchiveHabit: (Int) -> Unit,
    onDeleteHabit: (Int) -> Unit,
    onDashboardClick: () -> Unit = {},
    onStatsClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
) {

    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    AppBackground {

        Box(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
                    .padding(bottom = 120.dp)
            ) {

                Spacer(modifier = Modifier.height(50.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    JourneyChip(day = state.currentStreak)

                    HabitMenuButton(
                        isArchived = state.isArchived,
                        onEditClick = { showEditDialog = true },
                        onArchiveClick = { onArchiveHabit(state.id) },
                        onDeleteClick = { showDeleteDialog = true }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = state.title,
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Since ${state.formattedCreatedAt}",
                        color = Color.Gray,
                        style = MaterialTheme.typography.labelLarge
                    )

                    if (state.isReminderEnabled) {
                        Surface(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.NotificationsActive,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = TimeUtils.formatTime12Hour(state.reminderHour, state.reminderMinute),
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                HabitSummaryCard(
                    currentStreak = state.currentStreak,
                    bestStreak = state.bestStreak
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatTile(
                        modifier = Modifier.weight(1f),
                        title = "TOTAL DONE",
                        value = state.totalCompleted.toString(),
                        subtitle = "all time"
                    )
                    StatTile(
                        modifier = Modifier.weight(1f),
                        title = "WEEKLY",
                        value = "${state.weeklyCompleted}/7",
                        subtitle = "days done"
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatTile(
                        modifier = Modifier.weight(1f),
                        title = "BEST STREAK",
                        value = state.bestStreak.toString(),
                        subtitle = "record"
                    )
                    StatTile(
                        modifier = Modifier.weight(1f),
                        title = "TOTAL LOGS",
                        value = state.totalCompleted.toString(),
                        subtitle = "entries"
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                WeeklyProgressCard(completedDays = state.completedDays)

                Spacer(modifier = Modifier.height(20.dp))

                MonthlyProgressCard(completionLogs = state.recentCompletions)

                Spacer(modifier = Modifier.height(20.dp))

                HabitJournal(
                    logs = state.logs,
                    onUpdateNote = onUpdateNote
                )
            }

            BottomNavBar(
                selected = "dashboard",
                onDashboardClick = onDashboardClick,
                onStatsClick = onStatsClick,
                onSettingsClick = onSettingsClick,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 24.dp, vertical = 50.dp)
            )
        }

        if (showEditDialog) {
            EditHabitDialog(
                initialTitle = state.title,
                initialCategory = state.category,
                initialIconName = state.iconName,
                initialDaysOfWeek = state.daysOfWeek,
                initialIsReminderEnabled = state.isReminderEnabled,
                initialReminderHour = state.reminderHour,
                initialReminderMinute = state.reminderMinute,
                onDismiss = { showEditDialog = false },
                onSave = { newTitle, newCategory, newIcon, newDays, enabled, hour, min ->
                    onUpdateHabit(state.id, newTitle, newCategory, newIcon, newDays, enabled, hour, min)
                    showEditDialog = false
                }
            )
        }

        if (showDeleteDialog) {
            ConfirmationDialog(
                title = "Delete Habit",
                message = "Are you sure you want to delete \"${state.title}\"? This action cannot be undone.",
                confirmText = "Delete Habit",
                onConfirm = {
                    onDeleteHabit(state.id)
                    showDeleteDialog = false
                },
                onDismiss = {
                    showDeleteDialog = false
                }
            )
        }
    }
}