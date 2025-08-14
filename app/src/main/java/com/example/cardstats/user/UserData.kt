package com.example.cardstats.user

data class UserData(
    val name: String = "Me",
    val points: Int = 0,
    val balance: Int = 0,
    val bestDay: String = "",
    val bestDayPoints: Int = 0,
    val bestDayIncome: Int = 0,
    val worstDay: String = "-",
    val worstDayPoints: Int = 0,
    val worstDayIncome: Int = 0,
    val averageDayPoints: Float = 0f,
    val winPercentage: Int = 0,
    val totalGames: Int = 0,
    val dollars: Int = 0,
    val isWin: Boolean = false
)
