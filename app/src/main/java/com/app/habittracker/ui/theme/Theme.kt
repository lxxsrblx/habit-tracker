package com.app.habittracker.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.app.habittracker.ui.theme.AmberAccent

@Composable
fun TaskTrackerTheme(
    appTheme: String = "Amber",
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val primaryColor = when (appTheme) {
        "Purple" -> PurpleAccent
        "Blue" -> BlueAccent
        "Green" -> GreenAccent
        "Orange" -> OrangeAccent
        "Pink" -> PinkAccent
        "Cyan" -> CyanAccent
        "Lime" -> LimeAccent
        "Red" -> RedAccent
        "Indigo" -> IndigoAccent
        "Lavender" -> LavenderAccent
        "Emerald" -> EmeraldAccent
        "Sky" -> SkyAccent
        "Rose" -> RoseAccent
        else -> AmberAccent
    }

    val darkColorScheme = darkColorScheme(
        primary = primaryColor,
        secondary = primaryColor,
        tertiary = SuccessGreen,
        background = Background,
        surface = Color(0xFF121212),
        onPrimary = Color.Black,
        onSecondary = Color.Black,
        onTertiary = Color.Black,
        onBackground = WhitePrimary,
        onSurface = WhitePrimary
    )

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkColorScheme
        else -> darkColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}