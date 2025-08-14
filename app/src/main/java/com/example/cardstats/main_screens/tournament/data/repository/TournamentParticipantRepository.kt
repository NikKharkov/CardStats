package com.example.cardstats.main_screens.tournament.data.repository

import com.example.cardstats.main_screens.tournament.data.dao.TournamentParticipantDao
import com.example.cardstats.main_screens.tournament.data.entity.TournamentParticipantEntity
import kotlinx.coroutines.flow.Flow

class TournamentParticipantRepository(
    private val participantDao: TournamentParticipantDao
) {
    fun getParticipantsByTournamentId(tournamentId: Int): Flow<List<TournamentParticipantEntity>> =
        participantDao.getParticipantsByTournamentId(tournamentId)

    suspend fun getParticipantById(id: Int): TournamentParticipantEntity? =
        participantDao.getParticipantById(id)

    suspend fun insertParticipant(participant: TournamentParticipantEntity) {
        participantDao.insertParticipant(participant)
    }

    suspend fun updateParticipant(participant: TournamentParticipantEntity) {
        participantDao.updateParticipant(participant)
    }

    suspend fun deleteParticipant(participant: TournamentParticipantEntity) {
        participantDao.deleteParticipant(participant)
    }
}