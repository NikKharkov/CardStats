package com.example.cardstats.main_screens.tournament.data.repository

import com.example.cardstats.main_screens.tournament.data.dao.OtherTournamentParticipantDao
import com.example.cardstats.main_screens.tournament.data.entity.OtherTournamentParticipantEntity
import kotlinx.coroutines.flow.Flow

class OtherTournamentParticipantRepository(
    private val dao: OtherTournamentParticipantDao
) {
    fun getOtherParticipantsByParticipantId(participantId: Int): Flow<List<OtherTournamentParticipantEntity>> =
        dao.getOtherParticipantsByParticipantId(participantId)

    suspend fun insertOtherParticipant(participant: OtherTournamentParticipantEntity) {
        dao.insertOtherParticipant(participant)
    }

    suspend fun updateOtherParticipant(participant: OtherTournamentParticipantEntity) {
        dao.updateOtherParticipant(participant)
    }

    suspend fun deleteOtherParticipant(participant: OtherTournamentParticipantEntity) {
        dao.deleteOtherParticipant(participant)
    }
}