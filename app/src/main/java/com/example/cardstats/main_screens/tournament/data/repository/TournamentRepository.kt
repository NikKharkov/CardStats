package com.example.cardstats.main_screens.tournament.data.repository

import com.example.cardstats.main_screens.tournament.data.dao.TournamentDao
import com.example.cardstats.main_screens.tournament.data.entity.TournamentEntity
import kotlinx.coroutines.flow.Flow

class TournamentRepository(
    private val tournamentDao: TournamentDao
) {
    fun getAllTournaments(): Flow<List<TournamentEntity>> = tournamentDao.getAllTournaments()

    fun getPastTournaments(currentDate: String): Flow<List<TournamentEntity>> =
        tournamentDao.getPastTournaments(currentDate)

    fun getActiveOrFutureTournaments(currentDate: String): Flow<List<TournamentEntity>> =
        tournamentDao.getActiveOrFutureTournaments(currentDate)

    suspend fun getTournamentById(id: Int): TournamentEntity? = tournamentDao.getTournamentById(id)

    suspend fun insertTournament(tournament: TournamentEntity): Long {
        return tournamentDao.insertTournament(tournament)
    }

    suspend fun updateTournament(tournament: TournamentEntity) {
        tournamentDao.updateTournament(tournament)
    }

    suspend fun deleteTournament(tournament: TournamentEntity) {
        tournamentDao.deleteTournament(tournament)
    }
}