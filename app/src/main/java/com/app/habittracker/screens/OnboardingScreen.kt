package com.app.habittracker.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.habittracker.screens.components.AppBackground

@Composable
fun OnboardingScreen(
    onGetStarted: () -> Unit
) {
    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "🔥 WELCOME",
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 3.sp,
                style = MaterialTheme.typography.labelLarge
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Build Better",
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = "Habits.",
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Small actions. Every single day.",
                color = Color(0xFFAAAAAA),
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(50.dp))

            Button(
                onClick = onGetStarted,
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "Get Started",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}