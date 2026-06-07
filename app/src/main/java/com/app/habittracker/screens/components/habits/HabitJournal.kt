package com.app.habittracker.screens.components.habits

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.habittracker.data.HabitLog
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HabitJournal(
    logs: List<HabitLog>,
    onUpdateNote: (Int, String) -> Unit
) {
    val logsWithNotes = logs.filter { !it.isSkipped }.sortedByDescending { it.completedAt }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF121212)
        ),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Journal & History",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (logsWithNotes.isEmpty()) {
                Text(
                    text = "No entries yet. Complete the habit to start your journal!",
                    color = Color.DarkGray,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                logsWithNotes.take(15).forEachIndexed { index, log ->
                    JournalEntryItem(log = log, onUpdateNote = onUpdateNote)
                    if (index < logsWithNotes.take(15).lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = Color.White.copy(alpha = 0.05f),
                            thickness = 1.dp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun JournalEntryItem(
    log: HabitLog,
    onUpdateNote: (Int, String) -> Unit
) {
    var showEditDialog by remember { mutableStateOf(false) }
    val dateStr = SimpleDateFormat("MMM dd, h:mm a", Locale.getDefault()).format(Date(log.completedAt))

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = dateStr,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold
            )

            IconButton(
                onClick = { showEditDialog = true },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.EditNote,
                    contentDescription = "Edit Note",
                    tint = Color.Gray,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        if (!log.note.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = log.note,
                color = Color.White.copy(alpha = 0.9f),
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 20.sp
            )
        } else {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Add a reflection...",
                color = Color.DarkGray,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.clickable { showEditDialog = true }
            )
        }
    }

    if (showEditDialog) {
        NoteEditDialog(
            initialNote = log.note ?: "",
            onDismiss = { showEditDialog = false },
            onSave = { 
                onUpdateNote(log.id, it)
                showEditDialog = false
            }
        )
    }
}

@Composable
fun NoteEditDialog(
    initialNote: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var note by remember { mutableStateOf(initialNote) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Reflection", fontWeight = FontWeight.Bold) },
        text = {
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                placeholder = { Text("How did it go?") },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.DarkGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
        },
        confirmButton = {
            TextButton(onClick = { onSave(note) }) {
                Text("Save", color = MaterialTheme.colorScheme.primary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Gray)
            }
        },
        containerColor = Color(0xFF1E1E1E),
        titleContentColor = Color.White,
        shape = RoundedCornerShape(20.dp)
    )
}
