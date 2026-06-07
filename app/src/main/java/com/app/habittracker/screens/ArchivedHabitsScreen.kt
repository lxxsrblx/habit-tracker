package com.app.habittracker.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.DirectionsBike
import androidx.compose.material.icons.automirrored.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.habittracker.screens.components.AppBackground
import com.app.habittracker.screens.components.BottomNavBar
import com.app.habittracker.screens.components.HeaderChip
import com.app.habittracker.viewmodel.habit.HabitViewModel

@Composable
fun ArchivedHabitsScreen(
    viewModel: HabitViewModel,
    onDashboardClick: () -> Unit = {},
    onStatsClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val archivedHabits by viewModel.archivedHabits.collectAsState()
    val primary = MaterialTheme.colorScheme.primary

    AppBackground {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(24.dp)
                    .padding(bottom = 100.dp)
            ) {
                Spacer(modifier = Modifier.height(50.dp))

                HeaderChip(
                    icon = Icons.Rounded.Inventory2,
                    text = "The Archive Hall"
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Past",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "Victories.",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    color = primary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "A gallery of your retired goals and finished challenges.",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )

                if (archivedHabits.isEmpty()) {
                    Spacer(modifier = Modifier.height(50.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.AutoAwesome,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Your Hall of Fame is empty.",
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.height(24.dp))
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(bottom = 120.dp)
                    ) {
                        items(archivedHabits) { habit ->
                            ArchivedHabitCard(
                                title = habit.title,
                                category = habit.category,
                                iconName = habit.iconName,
                                onRestore = { viewModel.unarchiveHabit(habit.id) }
                            )
                        }
                    }
                }
            }

            BottomNavBar(
                selected = "settings",
                onDashboardClick = onDashboardClick,
                onStatsClick = onStatsClick,
                onSettingsClick = onSettingsClick,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 24.dp, vertical = 50.dp)
            )
        }
    }
}

@Composable
private fun ArchivedHabitCard(
    title: String,
    category: String,
    iconName: String,
    onRestore: () -> Unit
) {
    val primary = MaterialTheme.colorScheme.primary
    val icon = when (iconName) {
        "Fitness" -> Icons.Rounded.FitnessCenter
        "Work" -> Icons.Rounded.Work
        "Mind" -> Icons.Rounded.SelfImprovement
        "Finance" -> Icons.Rounded.Payments
        "Social" -> Icons.Rounded.Groups
        "Book" -> Icons.Rounded.Book
        "Water" -> Icons.Rounded.LocalDrink
        "Bed" -> Icons.Rounded.Bedtime
        "Code" -> Icons.Rounded.Code
        "Education" -> Icons.Rounded.HistoryEdu
        "Creative" -> Icons.Rounded.Palette
        "Growth" -> Icons.Rounded.AutoAwesome
        "Home" -> Icons.Rounded.House
        "Star" -> Icons.Rounded.Star
        "Pet" -> Icons.Rounded.Pets
        "Travel" -> Icons.Rounded.Flight
        "Food" -> Icons.Rounded.Restaurant
        "Music" -> Icons.Rounded.MusicNote
        "Movie" -> Icons.Rounded.Movie
        "Gaming" -> Icons.Rounded.SportsEsports
        "Biking" -> Icons.AutoMirrored.Rounded.DirectionsBike
        "Running" -> Icons.AutoMirrored.Rounded.DirectionsRun
        "Hiking" -> Icons.Rounded.Terrain
        "Nature" -> Icons.Rounded.Eco
        "Meditation" -> Icons.Rounded.Spa
        "Journal" -> Icons.Rounded.BorderColor
        "Art" -> Icons.Rounded.Brush
        "Photography" -> Icons.Rounded.PhotoCamera
        "Shopping" -> Icons.Rounded.ShoppingCart
        "Cleaning" -> Icons.Rounded.CleaningServices
        "Gardening" -> Icons.Rounded.Yard
        "Tools" -> Icons.Rounded.Construction
        "Lock" -> Icons.Rounded.Https
        "Key" -> Icons.Rounded.Key
        "Heart" -> Icons.Rounded.Favorite
        "Coffee" -> Icons.Rounded.Coffee
        "Language" -> Icons.Rounded.Translate
        "Science" -> Icons.Rounded.Science
        "Coding" -> Icons.Rounded.Terminal
        "Math" -> Icons.Rounded.Functions
        "Moon" -> Icons.Rounded.DarkMode
        "Sun" -> Icons.Rounded.LightMode
        else -> Icons.Rounded.TaskAlt
    }

    Surface(
        color = Color(0xFF121212),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth().aspectRatio(0.85f)
    ) {
        Box {
            Box(
                modifier = Modifier.fillMaxWidth().height(60.dp)
            )

            Column(
                modifier = Modifier.padding(16.dp).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFF1E1E1E), CircleShape)
                        .border(1.dp, primary.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = primary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 2
                )

                Text(
                    text = category,
                    color = Color.Gray,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onRestore,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primary.copy(alpha = 0.1f),
                        contentColor = primary
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text("Restore", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
