package com.app.habittracker.screens.components.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.app.habittracker.ui.theme.AmberAccent
import com.app.habittracker.ui.theme.BlueAccent
import com.app.habittracker.ui.theme.CyanAccent
import com.app.habittracker.ui.theme.EmeraldAccent
import com.app.habittracker.ui.theme.GreenAccent
import com.app.habittracker.ui.theme.IndigoAccent
import com.app.habittracker.ui.theme.LavenderAccent
import com.app.habittracker.ui.theme.LimeAccent
import com.app.habittracker.ui.theme.OrangeAccent
import com.app.habittracker.ui.theme.PinkAccent
import com.app.habittracker.ui.theme.PurpleAccent
import com.app.habittracker.ui.theme.RedAccent
import com.app.habittracker.ui.theme.RoseAccent
import com.app.habittracker.ui.theme.SkyAccent

@Composable
fun AppearanceCard(
    currentTheme: String,
    onThemeSelected: (String) -> Unit
) {
    val themes = listOf(
        ThemeOption("Amber", AmberAccent),
        ThemeOption("Orange", OrangeAccent),
        ThemeOption("Red", RedAccent),
        ThemeOption("Rose", RoseAccent),
        ThemeOption("Pink", PinkAccent),
        ThemeOption("Purple", PurpleAccent),
        ThemeOption("Lavender", LavenderAccent),
        ThemeOption("Indigo", IndigoAccent),
        ThemeOption("Blue", BlueAccent),
        ThemeOption("Sky", SkyAccent),
        ThemeOption("Cyan", CyanAccent),
        ThemeOption("Emerald", EmeraldAccent),
        ThemeOption("Green", GreenAccent),
        ThemeOption("Lime", LimeAccent)
    )

    SettingsCard(
        title = "Appearance",
        icon = Icons.Default.Palette
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                themes.forEach { theme ->
                    ThemeCircle(
                        color = theme.color,
                        isSelected = currentTheme == theme.name,
                        onClick = { onThemeSelected(theme.name) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ThemeCircle(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(25.dp)
            .clip(CircleShape)
            .background(color)
            .border(
                width = 2.dp,
                color = if (isSelected) Color.White else Color.Transparent,
                shape = CircleShape
            )
            .clickable { onClick() }
    )
}

private data class ThemeOption(
    val name: String,
    val color: Color
)
