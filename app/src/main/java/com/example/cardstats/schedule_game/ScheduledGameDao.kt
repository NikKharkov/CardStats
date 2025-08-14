package com.example.cardstats.schedule_game

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduledGameDao {
    @Insert
    suspend fun insert(scheduledGame: ScheduledGameEntity)

    @Update
    suspend fun updateSchedule(scheduledGame: ScheduledGameEntity)

    @Query("SELECT * FROM scheduled_games")
    fun getAllScheduledGames(): Flow<List<ScheduledGameEntity>>

    @Query("SELECT * FROM scheduled_games WHERE id = :id")
    fun getScheduledGameById(id: Int): Flow<ScheduledGameEntity?>
}