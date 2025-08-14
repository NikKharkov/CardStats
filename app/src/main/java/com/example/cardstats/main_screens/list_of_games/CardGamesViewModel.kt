package com.example.cardstats.main_screens.list_of_games

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardstats.add_card_game.model.Game
import com.example.cardstats.add_card_game.repository.CardGamesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CardGamesViewModel(
    private val cardGamesRepository: CardGamesRepository,
) : ViewModel() {

    private val _games = MutableStateFlow<List<Game>>(emptyList())
    val games = _games.asStateFlow()

    init {
        viewModelScope.launch {
            cardGamesRepository.getAllGames().collect { games ->
                _games.value = games
            }
        }
    }

    fun addNewGame(title: String,description: String) {
        viewModelScope.launch {
            cardGamesRepository.addNewGame(title,description)
        }
    }

    fun updateCard(id: Int, name: String, description: String) {
        viewModelScope.launch {
            cardGamesRepository.updateCard(id,name,description)
        }
    }
}