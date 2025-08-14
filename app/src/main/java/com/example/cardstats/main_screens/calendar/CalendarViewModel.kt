package com.example.cardstats.main_screens.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardstats.add_game.data.entity.GameSessionEntity
import com.example.cardstats.add_game.data.repository.GameSessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn

class CalendarViewModel(
    private val gameSessionRepository: GameSessionRepository
) : ViewModel() {

    private val _currentMonth = MutableStateFlow(Clock.System.todayIn(TimeZone.currentSystemDefault()))
    val currentMonth: StateFlow<LocalDate> = _currentMonth.asStateFlow()

    private val _selectedDate = MutableStateFlow<LocalDate?>(null)
    val selectedDate: StateFlow<LocalDate?> = _selectedDate.asStateFlow()

    private val _gamesForSelectedDate = MutableStateFlow<List<GameSessionEntity>>(emptyList())
    val gamesForSelectedDate: StateFlow<List<GameSessionEntity>> = _gamesForSelectedDate.asStateFlow()

    private val _winLossMap = MutableStateFlow<Map<LocalDate, Pair<Int, Int>>>(emptyMap())
    val winLossMap: StateFlow<Map<LocalDate, Pair<Int, Int>>> = _winLossMap.asStateFlow()

    init {
        viewModelScope.launch {
            gameSessionRepository.getAllGameSessions().collect { allSessions ->
                val map = mutableMapOf<LocalDate, Pair<Int, Int>>()
                allSessions.forEach { session ->
                    val dateStr = session.date
                    val date = parseDate(dateStr)
                    if (date != null) {
                        val participants = gameSessionRepository.getParticipantsBySessionId(session.id).first()
                        val userWins = participants.count { it.name == "Me" && it.isWin }
                        val userLosses = participants.count { it.name == "Me" && it.isLose }
                        val current = map[date] ?: Pair(0, 0)
                        map[date] = Pair(current.first + userWins, current.second + userLosses)
                    }
                }
                _winLossMap.value = map
            }
        }
    }

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
        viewModelScope.launch {
            val formattedDate = formatDate(date)
            gameSessionRepository.getGameSessionsByDate(formattedDate)
                .collect { games ->
                    _gamesForSelectedDate.value = games
                }
        }
    }

    fun previousMonth() {
        _currentMonth.value = _currentMonth.value.minus(1, DateTimeUnit.MONTH)
    }

    fun nextMonth() {
        _currentMonth.value = _currentMonth.value.plus(1, DateTimeUnit.MONTH)
    }

    fun formatDate(date: LocalDate): String {
        val monthNames = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        val dayNames = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        val dayOfWeek = dayNames[date.dayOfWeek.ordinal]
        val month = monthNames[date.monthNumber - 1]
        val day = date.dayOfMonth
        return "$month $day, $dayOfWeek"
    }

    private fun parseDate(dateStr: String): LocalDate? {
        val parts = dateStr.split(" ")
        if (parts.size == 3) {
            val monthStr = parts[0]
            val day = parts[1].replace(",", "").toIntOrNull() ?: return null
            val year = _currentMonth.value.year
            val month = monthStr.let { m ->
                arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec").indexOf(m) + 1
            }
            if (month == 0) return null
            return try {
                LocalDate(year, month, day)
            } catch (e: Exception) {
                null
            }
        }
        return null
    }
}
