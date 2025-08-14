package com.example.cardstats.add_game.data.repository

import com.example.cardstats.add_game.data.dao.GameSessionDao
import com.example.cardstats.add_game.data.entity.GameSessionEntity
import com.example.cardstats.add_game.data.entity.ParticipantEntity
import kotlinx.coroutines.flow.Flow

class GameSessionRepository(
    private val gameSessionDao: GameSessionDao
) {

    fun getAllGameSessions(): Flow<List<GameSessionEntity>> {
        return gameSessionDao.getAllGameSessions()
    }

    fun getParticipantsBySessionId(gameSessionId: Int): Flow<List<ParticipantEntity>> {
        return gameSessionDao.getParticipantsBySessionId(gameSessionId)
    }

    fun getGameSessionsByDate(date: String): Flow<List<GameSessionEntity>> {
        return gameSessionDao.getGameSessionsByDate(date)
    }

    suspend fun addGameSession(cardGameId: Int, date: String, note: String = ""): Long {
        val gameSession = GameSessionEntity(
            cardGameId = cardGameId,
            date = date,
            note = note
        )
        return gameSessionDao.insertGameSession(gameSession)
    }

    suspend fun addParticipants(gameSessionId: Int, participants: List<ParticipantEntity>) {
        gameSessionDao.insertParticipants(participants)
    }

    suspend fun deleteGameSession(id: Int) {
        gameSessionDao.deleteParticipantsBySessionId(id)
        gameSessionDao.deleteGameSession(id)
    }
}