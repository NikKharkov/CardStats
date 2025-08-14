package com.example.cardstats.main_screens.tournament.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.cardstats.main_screens.tournament.data.entity.TournamentParticipantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TournamentParticipantDao {

    @Query("SELECT * FROM tournament_participants WHERE tournament_id = :tournamentId")
    fun getParticipantsByTournamentId(tournamentId: Int): Flow<List<TournamentParticipantEntity>>

    @Query("SELECT * FROM tournament_participants WHERE id = :id")
    suspend fun getParticipantById(id: Int): TournamentParticipantEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParticipant(participant: TournamentParticipantEntity)

    @Update
    suspend fun updateParticipant(participant: TournamentParticipantEntity)

    @Delete
    suspend fun deleteParticipant(participant: TournamentParticipantEntity)
}