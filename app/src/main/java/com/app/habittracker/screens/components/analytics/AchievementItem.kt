package com.app.habittracker.screens.components.analytics

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.habittracker.viewmodel.analytics.AchievementUi

@Composable
fun AchievementItem(
    achievement: AchievementUi
) {

    Card(
        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(20.dp),

        colors = CardDefaults.cardColors(
            containerColor =
                if (achievement.unlocked)
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                else
                    Color(0xFF151515)
        ),

        border = BorderStroke(
            width = 1.dp,
            color =
                if (achievement.unlocked)
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
                else
                    Color.White.copy(alpha = 0.08f)
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 14.dp,
                    vertical = 12.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (achievement.unlocked)
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)
                        else
                            Color.White.copy(alpha = 0.05f)
                    ),

                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector =
                        achievement.icon,

                    contentDescription = null,

                    tint =
                        if (achievement.unlocked)
                            MaterialTheme.colorScheme.primary
                        else
                            Color.Gray,

                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = achievement.title,

                    color =
                        if (achievement.unlocked)
                            Color.White
                        else
                            Color.Gray,

                    fontWeight = FontWeight.SemiBold,

                    style =
                        MaterialTheme.typography.bodyMedium
                )

                Spacer(
                    modifier = Modifier.height(2.dp)
                )

                Text(
                    text = achievement.description,

                    color = Color.Gray,

                    style =
                        MaterialTheme.typography.bodySmall
                )
            }

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        if (achievement.unlocked)
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
                        else
                            Color.White.copy(alpha = 0.05f)
                    ),

                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector =
                        if (achievement.unlocked)
                            Icons.Default.Verified
                        else
                            Icons.Default.Lock,

                    contentDescription = null,

                    tint =
                        if (achievement.unlocked)
                            Color.White
                        else
                            Color.Gray,

                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}