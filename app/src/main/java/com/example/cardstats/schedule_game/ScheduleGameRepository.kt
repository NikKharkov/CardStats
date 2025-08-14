package com.example.cardstats.schedule_game

import com.example.cardstats.add_card_game.CardGameDao
import com.example.cardstats.add_card_game.CardGameEntity
import kotlinx.coroutines.flow.Flow

class ScheduleGameRepository(
    private val cardGameDao: CardGameDao,
    private val scheduledGameDao: ScheduledGameDao
) {
    suspend fun updateScheduledGame(game: ScheduledGameEntity) {
        scheduledGameDao.updateSchedule(game)
    }

    suspend fun insertScheduledGame(scheduledGame: ScheduledGameEntity) {
        scheduledGameDao.insert(scheduledGame)
    }

    fun getAllCardGames(): Flow<List<CardGameEntity>>  {
        return cardGameDao.getAllGames()
    }

    fun getAllScheduledGames(): Flow<List<ScheduledGameEntity>>  {
       return scheduledGameDao.getAllScheduledGames()
    }

    fun getScheduledGameById(id: Int): Flow<ScheduledGameEntity?> {
        return scheduledGameDao.getScheduledGameById(id)
    }
}