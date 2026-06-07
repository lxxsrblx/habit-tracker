package com.app.habittracker

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.app.habittracker.data.AppDatabase
import com.app.habittracker.data.User
import com.app.habittracker.repository.BackupRepository
import com.app.habittracker.repository.HabitLogRepository
import com.app.habittracker.repository.HabitRepository
import com.app.habittracker.repository.UserRepository
import com.app.habittracker.screens.AnalyticsScreen
import com.app.habittracker.screens.ArchivedHabitsScreen
import com.app.habittracker.screens.DashboardScreen
import com.app.habittracker.screens.HabitDetailScreen
import com.app.habittracker.screens.OnboardingScreen
import com.app.habittracker.screens.ProfileSetupScreen
import com.app.habittracker.screens.SettingsScreen
import com.app.habittracker.ui.theme.TaskTrackerTheme
import com.app.habittracker.viewmodel.analytics.AnalyticsViewModel
import com.app.habittracker.viewmodel.analytics.AnalyticsViewModelFactory
import com.app.habittracker.viewmodel.dashboard.DashboardViewModel
import com.app.habittracker.viewmodel.dashboard.DashboardViewModelFactory
import com.app.habittracker.viewmodel.habit.HabitDetailViewModel
import com.app.habittracker.viewmodel.habit.HabitDetailViewModelFactory
import com.app.habittracker.viewmodel.habit.HabitViewModel
import com.app.habittracker.viewmodel.habit.HabitViewModelFactory
import com.app.habittracker.viewmodel.settings.SettingsViewModel
import com.app.habittracker.viewmodel.settings.SettingsViewModelFactory
import com.app.habittracker.viewmodel.user.UserViewModel
import com.app.habittracker.viewmodel.user.UserViewModelFactory
import com.app.habittracker.worker.HabitReminderScheduler
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    enum class Screen {
        ONBOARDING,
        PROFILE,
        DASHBOARD,
        HABIT_DETAIL,
        ANALYTICS,
        SETTINGS,
        ARCHIVED_HABITS
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val db = AppDatabase.getInstance(applicationContext)

        val userRepository = UserRepository(db.userDao())
        val habitRepository = HabitRepository(db.habitDao())
        val logRepository = HabitLogRepository(db.habitLogDao())
        val backupRepository = BackupRepository(db, applicationContext)

        val userFactory = UserViewModelFactory(userRepository)
        val habitFactory = HabitViewModelFactory(habitRepository, logRepository, applicationContext)
        val dashboardFactory =
            DashboardViewModelFactory(userRepository, habitRepository, logRepository)
        val habitDetailFactory = HabitDetailViewModelFactory(habitRepository, logRepository)
        val analyticsFactory =
            AnalyticsViewModelFactory(userRepository, habitRepository, logRepository)
        val settingsFactory =
            SettingsViewModelFactory(userRepository, backupRepository, applicationContext)

        val userViewModel: UserViewModel by viewModels { userFactory }
        val dashboardViewModel: DashboardViewModel by viewModels { dashboardFactory }
        val habitViewModel: HabitViewModel by viewModels { habitFactory }
        val habitDetailViewModel: HabitDetailViewModel by viewModels { habitDetailFactory }
        val analyticsViewModel: AnalyticsViewModel by viewModels { analyticsFactory }
        val settingsViewModel: SettingsViewModel by viewModels { settingsFactory }

        splashScreen.setKeepOnScreenCondition {
            userViewModel.isLoading.value
        }

        setContent {
            val user by userViewModel.user.collectAsState()
            val theme = user?.appTheme ?: "Amber"

            TaskTrackerTheme(appTheme = theme) {

                val context = LocalContext.current
                var parties by remember { mutableStateOf<List<Party>>(emptyList()) }
                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { _ -> }

                LaunchedEffect(Unit) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                }

                LaunchedEffect(user) {
                    user?.let {
                        if (it.notificationsEnabled) {
                            HabitReminderScheduler.scheduleDailyReminder(
                                context,
                                it.reminderHour,
                                it.reminderMinute
                            )
                        }
                    }
                }

                val isLoading by userViewModel.isLoading.collectAsState()
                val dashboardState by dashboardViewModel.uiState.collectAsState()
                val habitDetailState by habitDetailViewModel.uiState.collectAsState()
                val analyticState by analyticsViewModel.uiState.collectAsState()

                var currentScreen by remember { mutableStateOf(Screen.DASHBOARD) }
                var selectedHabitId by remember { mutableStateOf<Int?>(null) }

                LaunchedEffect(Unit) {
                    userViewModel.loadUser()
                }

                var hasDeterminedInitialScreen by remember { mutableStateOf(false) }

                LaunchedEffect(isLoading, user) {
                    if (!isLoading && !hasDeterminedInitialScreen) {
                        currentScreen = if (user == null) {
                            Screen.ONBOARDING
                        } else {
                            Screen.DASHBOARD
                        }
                        hasDeterminedInitialScreen = true
                    }
                }

                when (currentScreen) {

                    Screen.ONBOARDING -> {
                        OnboardingScreen(
                            onGetStarted = {
                                currentScreen = Screen.PROFILE
                            }
                        )
                    }

                    Screen.PROFILE -> {
                        ProfileSetupScreen(
                            onContinue = { name ->
                                userViewModel.saveUser(name) {
                                    userViewModel.loadUser()
                                    currentScreen = Screen.DASHBOARD
                                }
                            }
                        )
                    }

                    Screen.DASHBOARD -> {
                        val contextForToast = LocalContext.current
                        DashboardScreen(
                            state = dashboardState,

                            onAddHabit = { title, category, icon, days, enabled, hour, min ->
                                habitViewModel.addHabit(
                                    title,
                                    category,
                                    icon,
                                    days,
                                    enabled,
                                    hour,
                                    min
                                )
                            },

                            onToggleHabit = { habitId ->
                                habitViewModel.toggleHabit(habitId) { completed ->
                                    if (completed) {
                                        userViewModel.addXp(15)
                                        parties = listOf(
                                            Party(
                                                speed = 0f,
                                                maxSpeed = 30f,
                                                damping = 0.9f,
                                                spread = 360,
                                                colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                                                emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100),
                                                position = Position.Relative(0.5, 0.3)
                                            )
                                        )
                                    } else {
                                        userViewModel.addXp(-15)
                                    }
                                }
                            },

                            onSkipHabit = { habitId ->
                                habitViewModel.skipHabit(habitId) { message ->
                                    Toast.makeText(contextForToast, message, Toast.LENGTH_SHORT)
                                        .show()
                                }
                            },

                            onDeleteHabit = { habit ->
                                habitViewModel.deleteHabit(habit)
                            },

                            onEditHabit = { habit, title, category, icon, days, enabled, hour, min ->
                                habitViewModel.updateHabitFull(
                                    habit.id,
                                    title,
                                    category,
                                    icon,
                                    days,
                                    enabled,
                                    hour,
                                    min
                                ) {}
                            },

                            onArchiveHabit = { id ->
                                habitViewModel.archiveHabit(id)
                            },

                            onSearchQueryChange = { query ->
                                dashboardViewModel.onSearchQueryChange(query)
                            },

                            onCategoryChange = { category ->
                                dashboardViewModel.onCategoryChange(category)
                            },

                            onToggleShowAll = { showAll ->
                                dashboardViewModel.onToggleShowAll(showAll)
                            },

                            onOpenHabitDetail = { habitId ->
                                habitDetailViewModel.clearHabit()
                                habitDetailViewModel.loadHabit(habitId)
                                selectedHabitId = habitId
                                currentScreen = Screen.HABIT_DETAIL
                            },

                            onDashboardClick = {
                                currentScreen = Screen.DASHBOARD
                            },

                            onStatsClick = {
                                currentScreen = Screen.ANALYTICS
                            },

                            onSettingsClick = {
                                currentScreen = Screen.SETTINGS
                            },
                        )
                    }

                    Screen.HABIT_DETAIL -> {

                        LaunchedEffect(currentScreen) {
                            selectedHabitId?.let {
                                habitDetailViewModel.loadHabit(it)
                            }
                        }

                        HabitDetailScreen(
                            state = habitDetailState,

                            onBack = {
                                currentScreen = Screen.DASHBOARD
                            },

                            onDashboardClick = {
                                currentScreen = Screen.DASHBOARD
                            },

                            onStatsClick = {
                                currentScreen = Screen.ANALYTICS
                            },

                            onSettingsClick = {
                                currentScreen = Screen.SETTINGS
                            },

                            onUpdateHabit = { id, title, category, icon, days, enabled, hour, min ->
                                habitViewModel.updateHabitFull(
                                    id,
                                    title,
                                    category,
                                    icon,
                                    days,
                                    enabled,
                                    hour,
                                    min
                                ) {
                                    habitDetailViewModel.loadHabit(id)
                                }
                            },

                            onUpdateNote = { logId, note ->
                                habitViewModel.updateLogNote(logId, note) {
                                    selectedHabitId?.let { habitDetailViewModel.loadHabit(it) }
                                }
                            },

                            onArchiveHabit = { id ->
                                habitViewModel.archiveHabit(id) {
                                    currentScreen = Screen.DASHBOARD
                                }
                            },

                            onDeleteHabit = { id ->
                                habitViewModel.deleteHabitById(id)
                                currentScreen = Screen.DASHBOARD
                            }
                        )
                    }

                    Screen.ANALYTICS -> {
                        AnalyticsScreen(
                            state = analyticState,
                            onDashboardClick = {
                                currentScreen = Screen.DASHBOARD
                            },

                            onStatsClick = {
                                currentScreen = Screen.ANALYTICS
                            },

                            onSettingsClick = {
                                currentScreen = Screen.SETTINGS
                            },
                        )
                    }

                    Screen.SETTINGS -> {
                        SettingsScreen(
                            viewModel = settingsViewModel,
                            onArchivedHabitsClick = {
                                currentScreen = Screen.ARCHIVED_HABITS
                            },
                            onDashboardClick = {
                                currentScreen = Screen.DASHBOARD
                            },

                            onStatsClick = {
                                currentScreen = Screen.ANALYTICS
                            },

                            onSettingsClick = {
                                currentScreen = Screen.SETTINGS
                            },
                        )
                    }

                    Screen.ARCHIVED_HABITS -> {
                        ArchivedHabitsScreen(
                            viewModel = habitViewModel,
                            onDashboardClick = {
                                currentScreen = Screen.DASHBOARD
                            },
                            onStatsClick = {
                                currentScreen = Screen.ANALYTICS
                            },
                            onSettingsClick = {
                                currentScreen = Screen.SETTINGS
                            }
                        )
                    }
                }

                if (parties.isNotEmpty()) {
                    KonfettiView(
                        modifier = Modifier.fillMaxSize(),
                        parties = parties
                    )
                    LaunchedEffect(parties) {
                        delay(5000)
                        parties = emptyList()
                    }
                }
            }
        }
    }
}
