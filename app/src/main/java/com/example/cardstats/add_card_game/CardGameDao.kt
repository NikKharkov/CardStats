package com.example.cardstats.add_card_game

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CardGameDao {

    @Query("SELECT * FROM card_games")
    fun getAllGames(): Flow<List<CardGameEntity>>

    @Query("SELECT name FROM card_games WHERE id = :id")
    fun getGameNameById(id: Int): Flow<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(game: CardGameEntity)

    @Update
    suspend fun update(game: CardGameEntity)
}