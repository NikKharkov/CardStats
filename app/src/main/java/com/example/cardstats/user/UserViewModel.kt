package com.example.cardstats.user

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardstats.add_game.presentation.AddGameViewModel
import com.example.cardstats.main_screens.statistics.charts.ChartData
import com.example.cardstats.main_screens.tournament.presentation.TournamentViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Year
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.WeekFields
import java.util.Locale

class UserViewModel(
    private val userPreferences: UserPreferences,
    private val tournamentViewModel: TournamentViewModel,
    private val addGameViewModel: AddGameViewModel
) : ViewModel() {

    private val _user = MutableStateFlow(UserData("Me"))
    val user = _user.asStateFlow()

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            userPreferences.userDataFlow.collect { userData ->
                _user.value = userData
                calculateUserStats()
            }
        }
    }

    suspend fun getChartData(period: String): List<ChartData> {
        val tournaments = tournamentViewModel.allTournaments.first()
        val gameSessions = addGameViewModel.getAllGameSessions().first()

        Log.d("UserViewModel", "Tournaments count: ${tournaments.size}")
        Log.d("UserViewModel", "Game Sessions count: ${gameSessions.size}")

        val statsByDate = mutableMapOf<String, Pair<Int, Int>>()

        val tournamentDateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val currentYear = Year.now().value

        fun parseDate(dateStr: String): LocalDate? {
            return try {
                LocalDate.parse(dateStr, tournamentDateFormatter)
            } catch (e: DateTimeParseException) {
                try {
                    val parsed = LocalDate.parse("$currentYear $dateStr", DateTimeFormatter.ofPattern("yyyy MMM d, E", Locale.ENGLISH))
                    parsed
                } catch (e2: DateTimeParseException) {
                    Log.e("UserViewModel", "Failed to parse date: $dateStr", e2)
                    null
                }
            }
        }

        tournaments.forEach { tournament ->
            val parsedDate = parseDate(tournament.endDate)
            if (parsedDate != null) {
                Log.d("UserViewModel", "Processed tournament date: ${tournament.endDate} -> $parsedDate")
                val date = when (period) {
                    "DAY" -> tournament.endDate
                    "WEEK" -> "Week ${parsedDate.get(WeekFields.of(Locale.getDefault()).weekOfYear())}"
                    "MONTH" -> parsedDate.month.toString().substring(0, 3)
                    else -> tournament.endDate
                }
                val participants = tournamentViewModel.getParticipants(tournament.id).first()
                val otherParticipants = tournamentViewModel.getOtherParticipants(
                    participants.firstOrNull()?.id ?: 0
                ).first()
                val allParticipants = participants.map { ParticipantSummary(it.playerName, it.points, it.wins) } +
                        otherParticipants.map { ParticipantSummary(it.name, it.points, it.wins) }

                Log.d("UserViewModel", "Participants count for tournament: ${allParticipants.size}")
                allParticipants.forEach { participant ->
                    Log.d("UserViewModel", "Participant: ${participant.name}, Wins: ${participant.wins}")
                    if (participant.name.trim().equals("Me", ignoreCase = true)) {
                        val currentStats = statsByDate.getOrDefault(date, Pair(0, 0))
                        val wins = if (participant.wins > 0) 1 else 0
                        val losses = if (participant.wins == 0) 1 else 0
                        statsByDate[date] = Pair(currentStats.first + wins, currentStats.second + losses)
                        Log.d("UserViewModel", "Added stats for $date: Wins=$wins, Losses=$losses")
                    }
                }
            }
        }

        gameSessions.forEach { session ->
            val parsedDate = parseDate(session.date)
            if (parsedDate != null) {
                Log.d("UserViewModel", "Processed session date: ${session.date} -> $parsedDate")
                val date = when (period) {
                    "DAY" -> session.date
                    "WEEK" -> "Week ${parsedDate.get(WeekFields.of(Locale.getDefault()).weekOfYear())}"
                    "MONTH" -> parsedDate.month.toString().substring(0, 3)
                    else -> session.date
                }
                val participants = addGameViewModel.getParticipantsForSession(session.id).first()
                val sessionParticipants = participants.map { ParticipantSummary(it.name, it.points, if (it.isWin) 1 else 0) }

                Log.d("UserViewModel", "Participants count for session: ${sessionParticipants.size}")
                sessionParticipants.forEach { participant ->
                    Log.d("UserViewModel", "Session Participant: ${participant.name}, Wins: ${participant.wins}")
                    if (participant.name.trim().equals("Me", ignoreCase = true)) {
                        val currentStats = statsByDate.getOrDefault(date, Pair(0, 0))
                        val wins = if (participant.wins > 0) 1 else 0
                        val losses = if (participant.wins == 0) 1 else 0
                        statsByDate[date] = Pair(currentStats.first + wins, currentStats.second + losses)
                        Log.d("UserViewModel", "Added session stats for $date: Wins=$wins, Losses=$losses")
                    }
                }
            }
        }

        Log.d("UserViewModel", "Final stats by date: $statsByDate")
        return statsByDate.entries.sortedBy { it.key }.map { (date, stats) ->
            ChartData(
                xValue = date,
                wins = stats.first.toFloat(),
                losses = stats.second.toFloat()
            )
        }
    }


    private suspend fun calculateUserStats() {
        val tournaments = tournamentViewModel.allTournaments.first()
        val gameSessions = addGameViewModel.getAllGameSessions().first()

        var totalPoints = 0
        var totalGames = 0
        var totalWins = 0
        var bestDayPoints = 0
        var bestDayIncome = 0
        var bestDay = ""
        var worstDayPoints = 0
        var worstDayIncome = 0
        var worstDay = ""
        val dailyStats = mutableMapOf<String, Pair<Int, Int>>()

        tournaments.forEach { tournament ->
            val participants = tournamentViewModel.getParticipants(tournament.id).first()
            val otherParticipants = tournamentViewModel.getOtherParticipants(
                participants.firstOrNull()?.id ?: 0
            ).first()
            val allParticipants = participants.map { ParticipantSummary(it.playerName, it.points, it.wins) } +
                    otherParticipants.map { ParticipantSummary(it.name, it.points, it.wins) }

            allParticipants.forEach { participant ->
                if (participant.name == "Me") {
                    totalPoints += participant.points
                    totalGames++
                    totalWins += if (participant.wins > 0) 1 else 0
                    val date = tournament.endDate
                    dailyStats[date] = dailyStats.getOrDefault(date, Pair(0, 0)).let {
                        Pair(it.first + participant.points, it.second)
                    }
                }
            }
        }

        gameSessions.forEach { session ->
            val participants = addGameViewModel.getParticipantsForSession(session.id).first()
            val sessionParticipants = participants.map { ParticipantSummary(it.name, it.points, if (it.isWin) 1 else 0) }

            sessionParticipants.forEach { participant ->
                if (participant.name == "Me") {
                    totalPoints += participant.points
                    totalGames++
                    totalWins += if (participant.wins > 0) 1 else 0
                    val date = session.date
                    dailyStats[date] = dailyStats.getOrDefault(date, Pair(0, 0)).let {
                        Pair(it.first + participant.points, it.second)
                    }
                }
            }
        }

        dailyStats.forEach { (date, stats) ->
            val (points, dollars) = stats
            if (points > bestDayPoints || bestDay.isEmpty()) {
                bestDayPoints = points
                bestDay = date
                bestDayIncome = dollars
            }
            if (points < worstDayPoints || worstDayPoints == 0 && worstDay.isEmpty()) {
                worstDayPoints = points
                worstDay = date
                worstDayIncome = dollars
            }
        }

        val averageDayPoints = if (totalGames > 0) totalPoints.toFloat() / totalGames else 0f
        val winPercentage = if (totalGames > 0) (totalWins * 100) / totalGames else 0

        val updatedUserData = _user.value.copy(
            points = totalPoints,
            totalGames = totalGames,
            bestDay = bestDay,
            bestDayPoints = bestDayPoints,
            bestDayIncome = bestDayIncome,
            worstDay = worstDay,
            worstDayPoints = worstDayPoints,
            worstDayIncome = worstDayIncome,
            averageDayPoints = averageDayPoints,
            winPercentage = winPercentage
        )
        _user.value = updatedUserData
        userPreferences.updateUserData(
            points = totalPoints,
            totalGames = totalGames,
            bestDay = bestDay,
            bestDayPoints = bestDayPoints,
            bestDayIncome = bestDayIncome,
            worstDay = worstDay,
            worstDayPoints = worstDayPoints,
            worstDayIncome = worstDayIncome,
            averageDayPoints = averageDayPoints,
            winPercentage = winPercentage
        )
    }

    fun updateUserPoints(points: Int, isWin: Boolean) {
        viewModelScope.launch {
            val currentPoints = _user.value.points
            val updatedPoints = if (isWin) currentPoints + points else currentPoints - points
            val updatedUserData = _user.value.copy(points = updatedPoints, isWin = isWin)
            _user.value = updatedUserData
            userPreferences.updateUserData(points = updatedPoints, isWin = isWin)
        }
    }

    fun updateUserIsWin(isWin: Boolean) {
        viewModelScope.launch {
            val updatedUserData = _user.value.copy(isWin = isWin)
            _user.value = updatedUserData
            userPreferences.updateUserData(isWin = isWin)
        }
    }

    fun updateUserDollars(dollars: Int, isWin: Boolean) {
        viewModelScope.launch {
            val currentBalance = _user.value.balance
            val updatedBalance = if (isWin) currentBalance + dollars else currentBalance - dollars
            val updatedUserData = _user.value.copy(balance = updatedBalance, isWin = isWin)
            _user.value = updatedUserData
            userPreferences.updateUserData(balance = updatedBalance, isWin = isWin)
        }
    }
}