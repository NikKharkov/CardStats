package com.example.cardstats.add_card_game.repository

import com.example.cardstats.R
import com.example.cardstats.add_card_game.CardGameDao
import com.example.cardstats.add_card_game.CardGameEntity
import com.example.cardstats.add_card_game.model.Game
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class CardGamesRepository(
    private val cardGameDao: CardGameDao
) {

    fun getAllGames(): Flow<List<Game>> {
        return cardGameDao.getAllGames().map { entities ->
            entities.map { entity ->
                Game(
                    id = entity.id,
                    name = entity.name,
                    description = entity.description,
                    icon = if (entity.isCustom) R.drawable.cards else entity.iconResId
                )
            }
        }
    }

    fun getGameNameById(id: Int): Flow<String> {
        return cardGameDao.getGameNameById(id)
    }


    suspend fun addNewGame(title: String,description: String) {
        val maxId = cardGameDao.getAllGames().first().maxOfOrNull {it.id} ?: 0
        val newGame = CardGameEntity(
            id = maxId + 1,
            name = title,
            description = description,
            iconResId = R.drawable.cards,
            isCustom = true
        )
        cardGameDao.insert(newGame)
    }

    suspend fun updateCard(id: Int, name: String, description: String) {
        val game = CardGameEntity(
            id = id,
            name = name,
            description = description,
            iconResId = R.drawable.cards,
            isCustom = true
        )
        cardGameDao.insert(game)
    }
}