package com.app.habittracker.screens.components.habits

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HabitMenuButton(
    isArchived: Boolean = false,
    onEditClick: () -> Unit,
    onArchiveClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    Box {

        IconButton(
            modifier = Modifier
                .size(30.dp)
                .background(
                    color = Color.White.copy(alpha = 0.08f),
                    shape = CircleShape
                ),
            onClick = {
                expanded = true
            }
        ) {

            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More options",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            containerColor = Color(0xFF1A1A1A),
            shape = RoundedCornerShape(12.dp)
        ) {

            DropdownMenuItem(
                contentPadding = PaddingValues(
                    horizontal = 12.dp,
                    vertical = 4.dp
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                },
                text = {
                    Text(
                        text = "Edit",
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                onClick = {
                    expanded = false
                    onEditClick()
                }
            )

            DropdownMenuItem(
                contentPadding = PaddingValues(
                    horizontal = 12.dp,
                    vertical = 4.dp
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Inventory2,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = Color.Gray
                    )
                },
                text = {
                    Text(
                        text = if (isArchived) "Restore" else "Archive",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White
                    )
                },
                onClick = {
                    expanded = false
                    onArchiveClick()
                }
            )

            DropdownMenuItem(
                contentPadding = PaddingValues(
                    horizontal = 12.dp,
                    vertical = 4.dp
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                },
                text = {
                    Text(
                        text = "Delete",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                },
                onClick = {
                    expanded = false
                    onDeleteClick()
                }
            )
        }
    }
}