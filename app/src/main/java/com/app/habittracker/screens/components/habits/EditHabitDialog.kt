package com.app.habittracker.screens.components.habits

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.DirectionsBike
import androidx.compose.material.icons.automirrored.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.habittracker.utils.TimeUtils

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EditHabitDialog(
    initialTitle: String,
    initialCategory: String,
    initialIconName: String,
    initialDaysOfWeek: String,
    initialIsReminderEnabled: Boolean,
    initialReminderHour: Int,
    initialReminderMinute: Int,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String, Boolean, Int, Int) -> Unit
) {
    var title by remember { mutableStateOf(initialTitle) }
    var selectedCategory by remember { mutableStateOf(initialCategory) }
    var selectedIconName by remember { mutableStateOf(initialIconName) }
    var selectedDays by remember { 
        mutableStateOf(initialDaysOfWeek.split(",").mapNotNull { it.toIntOrNull() }.toSet()) 
    }
    
    var isReminderEnabled by remember { mutableStateOf(initialIsReminderEnabled) }
    var reminderHour by remember { mutableStateOf(initialReminderHour) }
    var reminderMinute by remember { mutableStateOf(initialReminderMinute) }
    var showTimePicker by remember { mutableStateOf(false) }

    val categories = listOf("Health", "Fitness", "Work", "Mind", "Study", "Finance", "Social", "Home", "Creative", "Growth", "Wellness", "Other")
    val dayNames = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    
    val icons = listOf(
        "TaskAlt" to Icons.Rounded.TaskAlt,
        "Fitness" to Icons.Rounded.FitnessCenter,
        "Work" to Icons.Rounded.Work,
        "Mind" to Icons.Rounded.SelfImprovement,
        "Finance" to Icons.Rounded.Payments,
        "Social" to Icons.Rounded.Groups,
        "Book" to Icons.Rounded.Book,
        "Water" to Icons.Rounded.LocalDrink,
        "Bed" to Icons.Rounded.Bedtime,
        "Code" to Icons.Rounded.Code,
        "Education" to Icons.Rounded.HistoryEdu,
        "Creative" to Icons.Rounded.Palette,
        "Growth" to Icons.Rounded.AutoAwesome,
        "Home" to Icons.Rounded.House,
        "Star" to Icons.Rounded.Star,
        "Pet" to Icons.Rounded.Pets,
        "Travel" to Icons.Rounded.Flight,
        "Food" to Icons.Rounded.Restaurant,
        "Music" to Icons.Rounded.MusicNote,
        "Movie" to Icons.Rounded.Movie,
        "Gaming" to Icons.Rounded.SportsEsports,
        "Biking" to Icons.AutoMirrored.Rounded.DirectionsBike,
        "Running" to Icons.AutoMirrored.Rounded.DirectionsRun,
        "Hiking" to Icons.Rounded.Terrain,
        "Nature" to Icons.Rounded.Eco,
        "Meditation" to Icons.Rounded.Spa,
        "Journal" to Icons.Rounded.BorderColor,
        "Art" to Icons.Rounded.Brush,
        "Photography" to Icons.Rounded.PhotoCamera,
        "Shopping" to Icons.Rounded.ShoppingCart,
        "Cleaning" to Icons.Rounded.CleaningServices,
        "Gardening" to Icons.Rounded.Yard,
        "Tools" to Icons.Rounded.Construction,
        "Lock" to Icons.Rounded.Https,
        "Key" to Icons.Rounded.Key,
        "Heart" to Icons.Rounded.Favorite,
        "Coffee" to Icons.Rounded.Coffee,
        "Language" to Icons.Rounded.Translate,
        "Science" to Icons.Rounded.Science,
        "Coding" to Icons.Rounded.Terminal,
        "Math" to Icons.Rounded.Functions,
        "Moon" to Icons.Rounded.DarkMode,
        "Sun" to Icons.Rounded.LightMode
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Habit", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Habit Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.DarkGray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                // Days Selection
                Column {
                    Text(
                        text = "Repeat on",
                        color = Color.White.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.labelSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        dayNames.forEachIndexed { index, day ->
                            val dayNum = index + 1
                            val isSelected = dayNum in selectedDays
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFF2A2A2A))
                                    .clickable {
                                        selectedDays = if (isSelected) {
                                            if (selectedDays.size > 1) selectedDays - dayNum else selectedDays
                                        } else {
                                            selectedDays + dayNum
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = day.first().toString(),
                                    color = if (isSelected) Color.Black else Color.Gray,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    HorizontalDivider(color = Color.DarkGray.copy(alpha = 0.5f), thickness = 0.5.dp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Reminder",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                        Switch(
                            checked = isReminderEnabled,
                            onCheckedChange = { isReminderEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.primary,
                                checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                            ),
                            modifier = Modifier.scale(0.7f)
                        )
                    }

                    if (isReminderEnabled) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showTimePicker = true },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = TimeUtils.formatTime12Hour(reminderHour, reminderMinute),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "Change",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.background(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    RoundedCornerShape(4.dp)
                                ).padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    HorizontalDivider(color = Color.DarkGray.copy(alpha = 0.5f), thickness = 0.5.dp)
                }

                Column {
                    Text(
                        text = "Category",
                        color = Color.White.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.labelSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        categories.forEach { category ->
                            val isSelected = selectedCategory == category
                            Surface(
                                modifier = Modifier.clickable { selectedCategory = category },
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFF2A2A2A),
                                contentColor = if (isSelected) Color.Black else Color.Gray
                            ) {
                                Text(
                                    text = category,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = if (isSelected) FontWeight.Bold else null
                                )
                            }
                        }
                    }
                }

                // Icon Selection
                Column {
                    Text(
                        text = "Icon",
                        color = Color.White.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.labelSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        icons.forEach { (name, icon) ->
                            val isSelected = selectedIconName == name
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFF2A2A2A))
                                    .clickable { selectedIconName = name },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = if (isSelected) Color.Black else Color.Gray,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { 
                    if (title.isNotBlank()) {
                        val daysString = selectedDays.sorted().joinToString(",")
                        onSave(title, selectedCategory, selectedIconName, daysString, isReminderEnabled, reminderHour, reminderMinute) 
                    }
                }
            ) {
                Text("Save", color = MaterialTheme.colorScheme.primary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Gray)
            }
        },
        shape = RoundedCornerShape(20.dp),
        containerColor = Color(0xFF1E1E1E),
        titleContentColor = Color.White,
        textContentColor = Color.White
    )

    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = reminderHour,
            initialMinute = reminderMinute,
            is24Hour = false // Standard 12 hour
        )
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    reminderHour = timePickerState.hour
                    reminderMinute = timePickerState.minute
                    showTimePicker = false
                }) { Text("Confirm", color = MaterialTheme.colorScheme.primary) }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Cancel", color = Color.Gray) }
            },
            text = {
                TimePicker(state = timePickerState)
            },
            containerColor = Color(0xFF1E1E1E),
            shape = RoundedCornerShape(24.dp)
        )
    }
}
