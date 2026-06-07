package com.app.habittracker.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.app.habittracker.screens.components.habits.EditHabitDialog
import com.app.habittracker.screens.components.QuickActionMenu
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.habittracker.data.Habit
import com.app.habittracker.viewmodel.dashboard.DashboardUiState
import androidx.compose.ui.Alignment
import com.app.habittracker.screens.components.AppBackground
import com.app.habittracker.screens.components.BottomNavBar
import com.app.habittracker.screens.components.JourneyChip
import com.app.habittracker.screens.components.LevelCard
import com.app.habittracker.screens.components.StatCard
import com.app.habittracker.screens.components.habits.EmptyHabitState
import com.app.habittracker.screens.components.habits.AddHabitCard
import com.app.habittracker.screens.components.habits.HabitCard
import com.app.habittracker.screens.components.habits.HabitFilterBar
import com.app.habittracker.screens.components.habits.ProgressCard

@Composable
fun DashboardScreen(
    state: DashboardUiState,
    onAddHabit: (String, String, String, String, Boolean, Int, Int) -> Unit,
    onToggleHabit: (Int) -> Unit,
    onSkipHabit: (Int) -> Unit,
    onArchiveHabit: (Int) -> Unit,
    onDeleteHabit: (Habit) -> Unit,
    onEditHabit: (Habit, String, String, String, String, Boolean, Int, Int) -> Unit,
    onToggleShowAll: (Boolean) -> Unit = {},
    onSearchQueryChange: (String) -> Unit = {},
    onCategoryChange: (String) -> Unit = {},
    onDashboardClick: () -> Unit = {},
    onStatsClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onOpenHabitDetail: (Int) -> Unit

) {
    var selectedHabitForMenu by remember { mutableStateOf<Habit?>(null) }
    var showEditDialog by remember { mutableStateOf<Habit?>(null) }

    AppBackground {

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(24.dp)
                    .padding(bottom = 100.dp)
            ) {

                Spacer(
                    modifier = Modifier.height(50.dp)
                )

                JourneyChip(
                    day = state.journeyDays
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Text(
                    text = "Build Better",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "Habits.",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = "Welcome back, ${state.userName}!",
                    color = Color.White
                )

                Spacer(
                    modifier = Modifier.height(28.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "ACTIVE",
                        value = state.activeHabits.toString(),
                        subtitle = "habits tracked",
                        valueColor = Color.White
                    )

                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "TODAY",
                        value = state.completedToday.toString(),
                        subtitle = "completed",
                        valueColor = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                ProgressCard(
                    progress = state.progress
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                LevelCard(
                    level = state.level,
                    totalXp = state.totalXp
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                HabitFilterBar(
                    searchQuery = state.searchQuery,
                    onSearchQueryChange = onSearchQueryChange,
                    selectedCategory = state.selectedCategory,
                    onCategoryChange = onCategoryChange
                )

                Spacer(
                    modifier = Modifier.height(28.dp)
                )

                AddHabitCard(
                    onAddHabit = onAddHabit
                )

                Spacer(
                    modifier = Modifier.height(28.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Your Habits",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )

                    TextButton(
                        onClick = { onToggleShowAll(!state.showAllHabits) },
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        Text(
                            text = if (state.showAllHabits) "Show Today Only" else "View All",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                if (state.habits.isEmpty()) {
                    if (state.activeHabits == 0 && state.selectedCategory == "All" && state.searchQuery.isEmpty()) {
                        EmptyHabitState()
                    } else if (state.completedToday > 0 && state.completedToday == state.activeHabits && !state.showAllHabits) {
                        EmptyHabitState(
                            emoji = "🔥",
                            title = "All Caught Up!",
                            subtitle = "You've completed all your habits for today. Great job staying disciplined!"
                        )
                    } else {
                        EmptyHabitState(
                            emoji = "🔍",
                            title = "No Habits Found",
                            subtitle = "No habits match your current filters. Try adjusting your search or category."
                        )
                    }
                } else {

                    state.habits.forEach { habit ->

                        HabitCard(
                            title = habit.title,
                            category = habit.category,
                            iconName = habit.iconName,
                            completed = state.completedHabitIds.contains(
                                habit.id
                            ),
                            isSkipped = state.skippedHabitIds.contains(
                                habit.id
                            ),
                            streak = state.habitStreaks[
                                habit.id
                            ] ?: 0,
                            completedDays = state.completedHabitDays[
                                habit.id
                            ] ?: emptySet(),
                            onToggle = {
                                onToggleHabit(
                                    habit.id
                                )
                            },
                            onSkip = {
                                onSkipHabit(
                                    habit.id
                                )
                            },
                            onOpenDetail = {
                                onOpenHabitDetail(
                                    habit.id
                                )
                            },
                            onLongClick = {
                                selectedHabitForMenu = habit
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )
                    }
                }
            }

            BottomNavBar(
                selected = "dashboard",

                onDashboardClick =
                    onDashboardClick,

                onStatsClick =
                    onStatsClick,

                onSettingsClick =
                    onSettingsClick,

                modifier = Modifier
                    .align(
                        Alignment.BottomCenter
                    )
                    .padding(
                        horizontal = 24.dp,
                        vertical = 50.dp
                    )
            )
        }
    }

    if (selectedHabitForMenu != null) {
        val habit = selectedHabitForMenu!!
        QuickActionMenu(
            habit = habit,
            onDismiss = { selectedHabitForMenu = null },
            onEdit = { 
                showEditDialog = habit
                selectedHabitForMenu = null 
            },
            onArchive = {
                onArchiveHabit(habit.id)
                selectedHabitForMenu = null
            },
            onDelete = {
                onDeleteHabit(habit)
                selectedHabitForMenu = null
            }
        )
    }

    if (showEditDialog != null) {
        val habit = showEditDialog!!
        EditHabitDialog(
            initialTitle = habit.title,
            initialCategory = habit.category,
            initialIconName = habit.iconName,
            initialDaysOfWeek = habit.daysOfWeek,
            initialIsReminderEnabled = habit.isReminderEnabled,
            initialReminderHour = habit.reminderHour,
            initialReminderMinute = habit.reminderMinute,
            onDismiss = { showEditDialog = null },
            onSave = { title, category, icon, days, enabled, hour, min ->
                onEditHabit(habit, title, category, icon, days, enabled, hour, min)
                showEditDialog = null
            }
        )
    }
}
