package com.app.habittracker.screens.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun AppBackground(
    content: @Composable () -> Unit
) {
    val primary = MaterialTheme.colorScheme.primary

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFF0A0A0F)
            )
    ) {
        // Grid drawing...
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val gridSize = 60f
            var x = 0f
            while (x <= size.width) {
                drawLine(
                    color = Color(0xFF2A2A2A).copy(alpha = 0.35f),
                    start = Offset(x, 0f),
                    end = Offset(x, size.height),
                    strokeWidth = 1f
                )
                x += gridSize
            }
            var y = 0f
            while (y <= size.height) {
                drawLine(
                    color = Color(0xFF2A2A2A).copy(alpha = 0.35f),
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1f
                )
                y += gridSize
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            primary.copy(alpha = 0.2f),
                            Color.Transparent
                        ),
                        center = Offset(
                            x = 250f,
                            y = 250f
                        ),
                        radius = 700f
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            primary.copy(alpha = 0.15f),
                            Color.Transparent
                        ),
                        center = Offset(
                            x = 1000f,
                            y = 300f
                        ),
                        radius = 900f
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            primary.copy(alpha = 0.1f),
                            Color.Transparent
                        ),
                        center = Offset(
                            x = 500f,
                            y = 1800f
                        ),
                        radius = 1200f
                    )
                )
        )

        content()
    }
}