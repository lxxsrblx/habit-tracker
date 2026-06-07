package com.app.habittracker.screens.components.habits

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.DirectionsBike
import androidx.compose.material.icons.automirrored.rounded.DirectionsRun
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HabitCard(
    title: String,
    category: String,
    iconName: String,
    completed: Boolean,
    isSkipped: Boolean = false,
    completedDays: Set<Int>,
    onToggle: () -> Unit,
    onSkip: () -> Unit = {},
    streak: Int,
    onOpenDetail: () -> Unit,
    onLongClick: () -> Unit = {}
) {

    val weekDays = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
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

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onOpenDetail() },
                onLongClick = { onLongClick() }
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF121212)
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            completed -> MaterialTheme.colorScheme.primary
                            isSkipped -> Color.Gray.copy(alpha = 0.3f)
                            else -> Color(0xFF2A2A2A)
                        }
                    )
                    .combinedClickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = { onToggle() },
                        onLongClick = { if (!completed && !isSkipped) onSkip() }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when {
                        completed -> Icons.Default.Check
                        isSkipped -> Icons.Rounded.Shield
                        else -> icon
                    },
                    contentDescription = null,
                    tint = when {
                        completed -> Color.Black
                        isSkipped -> MaterialTheme.colorScheme.primary
                        else -> Color.Gray
                    },
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.weight(1f, fill = false)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = title,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f, fill = false)
                            )

                            Surface(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Whatshot,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(
                                        text = "$streak",
                                        color = Color.White,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                        Text(
                            text = category,
                            color = Color.Gray,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Column {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        weekDays.forEachIndexed { index, _ ->
                            val dayNumber = index + 1
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (dayNumber in completedDays)
                                            MaterialTheme.colorScheme.primary
                                        else
                                            Color(0xFF2A2A2A)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {

                                if (dayNumber in completedDays) {

                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.Black,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
                            .forEachIndexed { index, day ->
                                val dayNumber = index + 1
                                Box(
                                    modifier = Modifier.width(20.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = day,
                                        fontSize = MaterialTheme.typography.labelSmall.fontSize * 0.75,
                                        color = if (dayNumber in completedDays) MaterialTheme.colorScheme.primary else Color.Gray
                                    )
                                }
                            }
                    }
                }
            }
        }
    }
}
