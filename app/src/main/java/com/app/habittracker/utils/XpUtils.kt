package com.app.habittracker.utils

fun getXpForLevel(level: Int): Int {
    return level * 100
}

fun calculateLevel(totalXp: Int): Int {
    var level = 1
    var remainingXp = totalXp
    while (remainingXp >= getXpForLevel(level)) {
        remainingXp -= getXpForLevel(level)
        level++
    }
    return level
}

fun getProgressToNextLevel(totalXp: Int): Float {
    val currentLevel = calculateLevel(totalXp)
    var xpInPreviousLevels = 0
    for (i in 1 until currentLevel) {
        xpInPreviousLevels += getXpForLevel(i)
    }
    val xpInCurrentLevel = totalXp - xpInPreviousLevels
    val xpRequiredForNext = getXpForLevel(currentLevel)
    return xpInCurrentLevel.toFloat() / xpRequiredForNext.toFloat()
}
