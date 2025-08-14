package com.example.cardstats.main_screens.tournament.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.cardstats.main_screens.tournament.data.entity.OtherTournamentParticipantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OtherTournamentParticipantDao {
    @Query("SELECT * FROM other_tournament_participants WHERE participant_id = :participantId")
    fun getOtherParticipantsByParticipantId(participantId: Int): Flow<List<OtherTournamentParticipantEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOtherParticipant(participant: OtherTournamentParticipantEntity)

    @Update
    suspend fun updateOtherParticipant(participant: OtherTournamentParticipantEntity)

    @Delete
    suspend fun deleteOtherParticipant(participant: OtherTournamentParticipantEntity)
}