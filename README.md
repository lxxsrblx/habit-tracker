# 🚀 Habit Tracker: Build Better Habits.

A premium, offline-first Android application designed to help users cultivate discipline through gamified progress and elegant design. Built with the latest Android technologies, including Jetpack Compose, Room, and Material 3.

## ✨ Key Features

*   **Gamified Growth:** Earn XP for every habit completed. Watch your level grow as you build consistency.
*   **Celebratory Feedback:** Uses the **Konfetti** library to trigger visual rewards upon habit completion, making discipline feel rewarding.
*   **Privacy Focused (Offline-First):** All data is stored locally on-device using a robust Room database. No cloud, no tracking—just you and your goals.
*   **Smart Reminders:** 
    *   **Individual Reminders:** Set specific times for each habit.
    *   **Daily Catch-up:** A background "Daily Summary" notification (powered by **WorkManager**) to remind you of remaining tasks.
*   **Premium Visuals:** 
    *   **Modern Splash Screen:** Implements the official Android 12+ Splash Screen API.
    *   **Themed UI:** Supports multiple color accents (Amber, Purple, Indigo, etc.) on a sleek, high-contrast dark theme.
    *   **Material 3 Design:** Fully responsive UI following the latest Material Design guidelines.
*   **Data Portability:** Built-in Export/Import functionality to backup and restore your habit data via JSON.

## 🛠 Tech Stack

*   **Language:** Kotlin
*   **UI Framework:** Jetpack Compose
*   **Database:** Room (with safe migration handling)
*   **Background Tasks:** WorkManager
*   **Architecture:** MVVM (Model-View-ViewModel) + Repository Pattern
*   **Dependency Management:** Gradle Version Catalog (libs.versions.toml)
*   **Libraries:**
    *   `androidx.core:core-splashscreen`
    *   `nl.dionsegijn:konfetti-compose`
    *   `com.google.code.gson:gson` (for data backup)

## 📸 Screenshots
<img width="720" height="1600" alt="image" src="https://github.com/user-attachments/assets/84a3e9e6-2c81-4c2c-b1dc-23f26eabe06e" />
<img width="720" height="1600" alt="image" src="https://github.com/user-attachments/assets/cbcea745-c9a4-4d3f-9c29-ba39f8d92449" />
<img width="720" height="1600" alt="image" src="https://github.com/user-attachments/assets/65a4bc4a-9ac8-4543-852a-573e1e390dde" />

## 🚀 Getting Started

1.  Clone this repository.
2.  Open the project in **Android Studio Ladybug (or newer)**.
3.  Ensure your JDK is set to 17 or higher.
4.  Run the `:app` module on an emulator or physical device (Android 7.0+).

## 📄 License
This project is available under the **Apache License 2.0**.
