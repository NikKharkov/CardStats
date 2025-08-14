package com.example.cardstats.add_game.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cardstats.add_game.data.entity.GameSessionEntity
import com.example.cardstats.add_game.data.entity.ParticipantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameSessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameSession(gameSession: GameSessionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParticipant(participant: ParticipantEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParticipants(participants: List<ParticipantEntity>)

    @Query("SELECT * FROM game_sessions ORDER BY createdAt DESC")
    fun getAllGameSessions(): Flow<List<GameSessionEntity>>

    @Query("SELECT * FROM game_sessions WHERE date = :date")
    fun getGameSessionsByDate(date: String): Flow<List<GameSessionEntity>>

    @Query("SELECT * FROM participants WHERE gameSessionId = :gameSessionId")
    fun getParticipantsBySessionId(gameSessionId: Int): Flow<List<ParticipantEntity>>

    @Query("DELETE FROM game_sessions WHERE id = :id")
    suspend fun deleteGameSession(id: Int)

    @Query("DELETE FROM participants WHERE gameSessionId = :gameSessionId")
    suspend fun deleteParticipantsBySessionId(gameSessionId: Int)
}
