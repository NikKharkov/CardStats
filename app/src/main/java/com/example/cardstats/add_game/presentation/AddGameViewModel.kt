package com.example.cardstats.add_game.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardstats.add_card_game.repository.CardGamesRepository
import com.example.cardstats.add_game.data.entity.GameSessionEntity
import com.example.cardstats.add_game.data.entity.ParticipantEntity
import com.example.cardstats.add_game.data.repository.GameSessionRepository
import com.example.cardstats.add_game.presentation.model.Participant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

class AddGameViewModel(
    private val gameSessionRepository: GameSessionRepository,
    private val cardGamesRepository: CardGamesRepository
) : ViewModel() {

    private val _selectedDate = MutableStateFlow<LocalDate?>(null)
    val selectedDate: StateFlow<LocalDate?> = _selectedDate.asStateFlow()

    private val _selectedGame = MutableStateFlow("")
    val selectedGame: StateFlow<String> = _selectedGame.asStateFlow()

    private val _participants = MutableStateFlow<List<Participant>>(emptyList())
    val participants: StateFlow<List<Participant>> = _participants.asStateFlow()

    private val _cardGames = cardGamesRepository.getAllGames()
        .map { games -> games.map { it.name } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cardGames: StateFlow<List<String>> = _cardGames

    fun getAllGameSessions(): Flow<List<GameSessionEntity>> = gameSessionRepository.getAllGameSessions()
    fun getParticipantsForSession(sessionId: Int): Flow<List<ParticipantEntity>> = gameSessionRepository.getParticipantsBySessionId(sessionId)

    fun updateDate(date: LocalDate?) {
        _selectedDate.value = date
    }

    fun updateSelectedGame(game: String) {
        _selectedGame.value = game
    }

    fun updateParticipant(index: Int, participant: Participant) {
        _participants.value = _participants.value.mapIndexed { i, p ->
            if (i == index) participant else p
        }
    }

    fun addParticipant() {
        _participants.value += Participant()
    }

    fun removeParticipant(index: Int) {
        _participants.value = _participants.value.filterIndexed { i, _ -> i != index }
    }

    fun saveGameSession(userPoints: Int, userDollars: Int, userIsWin: Boolean) {
        viewModelScope.launch {
            val date = _selectedDate.value?.let { formatDate(it) } ?: formatDate(Clock.System.todayIn(TimeZone.currentSystemDefault()))
            val cardGameName = _selectedGame.value
            val cardGame = cardGamesRepository.getAllGames().first().find { it.name == cardGameName }
            if (cardGame != null) {
                val gameSessionId = gameSessionRepository.addGameSession(cardGame.id, date)
                val participantsList = mutableListOf<ParticipantEntity>()

                participantsList.add(
                    ParticipantEntity(
                        gameSessionId = gameSessionId.toInt(),
                        name = "Me",
                        points = userPoints,
                        dollars = userDollars,
                        isWin = userIsWin,
                        isLose = !userIsWin
                    )
                )

                participantsList.addAll(
                    _participants.value.map { p ->
                        ParticipantEntity(
                            gameSessionId = gameSessionId.toInt(),
                            name = p.name,
                            points = 0,
                            dollars = p.dollars.toIntOrNull() ?: 0,
                            isWin = p.isWin,
                            isLose = !p.isWin
                        )
                    }
                )

                gameSessionRepository.addParticipants(gameSessionId.toInt(), participantsList)
            }
        }
    }

    fun formatDate(date: LocalDate): String {
        val monthNames = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        val dayNames = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        val dayOfWeek = dayNames[date.dayOfWeek.ordinal]
        val month = monthNames[date.monthNumber - 1]
        val day = date.dayOfMonth
        return "$month $day, $dayOfWeek"
    }
}