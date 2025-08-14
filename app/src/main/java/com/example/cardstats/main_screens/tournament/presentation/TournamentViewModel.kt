package com.example.cardstats.main_screens.tournament.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardstats.main_screens.tournament.data.entity.OtherTournamentParticipantEntity
import com.example.cardstats.main_screens.tournament.data.entity.TournamentEntity
import com.example.cardstats.main_screens.tournament.data.entity.TournamentParticipantEntity
import com.example.cardstats.main_screens.tournament.data.model.TempParticipant
import com.example.cardstats.main_screens.tournament.data.repository.OtherTournamentParticipantRepository
import com.example.cardstats.main_screens.tournament.data.repository.TournamentParticipantRepository
import com.example.cardstats.main_screens.tournament.data.repository.TournamentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class TournamentViewModel(
    private val tournamentRepository: TournamentRepository,
    private val participantRepository: TournamentParticipantRepository,
    private val otherParticipantRepository: OtherTournamentParticipantRepository,
) : ViewModel() {
    val allTournaments: Flow<List<TournamentEntity>> = tournamentRepository.getAllTournaments()

    private val _participants = MutableStateFlow<List<TempParticipant>>(emptyList())
    val participants: StateFlow<List<TempParticipant>> = _participants

    val fromDate = MutableStateFlow<LocalDate?>(null)
    val toDate = MutableStateFlow<LocalDate?>(null)

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDate(localDate: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        return localDate.toJavaLocalDate().format(formatter)
    }

    fun getParticipants(tournamentId: Int): Flow<List<TournamentParticipantEntity>> =
        participantRepository.getParticipantsByTournamentId(tournamentId)

    fun getOtherParticipants(participantId: Int): Flow<List<OtherTournamentParticipantEntity>> =
        otherParticipantRepository.getOtherParticipantsByParticipantId(participantId)

    fun updateFromDate(date: LocalDate) {
        fromDate.value = date
    }

    fun updateToDate(date: LocalDate) {
        toDate.value = date
    }

    fun formatTournamentDate(startDate: String, endDate: String): String {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val displayDateFormat = SimpleDateFormat("MMMM d", Locale.ENGLISH)

        return try {

            val start = dateFormat.parse(startDate)
            val end = dateFormat.parse(endDate)

            if (start == null || end == null) {
                return "$startDate – $endDate"
            }

            val startFormatted = displayDateFormat.format(start)
            val endFormatted = displayDateFormat.format(end)

            val calendar = Calendar.getInstance()
            calendar.time = start
            val startYear = calendar.get(Calendar.YEAR)
            calendar.time = end
            val endYear = calendar.get(Calendar.YEAR)
            val year = if (startYear == endYear) endYear.toString() else "$startYear – $endYear"

            "$startFormatted – $endFormatted, $year"
        } catch (e: Exception) {
            "$startDate – $endDate"
        }
    }

    fun addParticipant() {
        _participants.value += TempParticipant()
    }

    fun updateParticipant(index: Int, participant: TempParticipant) {
        _participants.value = _participants.value.toMutableList().apply {
            set(index, participant)
        }
    }

    fun removeParticipant(index: Int) {
        _participants.value = _participants.value.toMutableList().apply {
            removeAt(index)
        }
    }

    suspend fun saveTournamentAndParticipant(
        tournamentName: String,
        cardGameId: Int,
        startDate: String,
        endDate: String,
        playerName: String,
        points: Int,
        wins: Int,
    ) {
        val tournament = TournamentEntity(
            tournamentName = tournamentName,
            cardGameId = cardGameId,
            startDate = startDate,
            endDate = endDate
        )
        val tournamentId = tournamentRepository.insertTournament(tournament)

        val participant = TournamentParticipantEntity(
            tournamentId = tournamentId.toInt(),
            playerName = playerName,
            points = points,
            wins = wins
        )
        participantRepository.insertParticipant(participant)

        val participants = participantRepository.getParticipantsByTournamentId(tournamentId.toInt())
            .firstOrNull() ?: emptyList()
        val mainParticipant = participants.find { it.playerName == playerName }

        mainParticipant?.let { main ->
            _participants.value.forEach { tempParticipant ->
                val isPointsValid =
                    tempParticipant.points.isNotBlank() && tempParticipant.points.toIntOrNull() != null
                val isWinsValid =
                    tempParticipant.wins.isNotBlank() && tempParticipant.wins.toIntOrNull() != null
                if (tempParticipant.name.isNotBlank() && isPointsValid && isWinsValid) {
                    val otherParticipant = OtherTournamentParticipantEntity(
                        participantId = main.id,
                        name = tempParticipant.name,
                        points = tempParticipant.points.toInt(),
                        wins = tempParticipant.wins.toInt()
                    )
                    otherParticipantRepository.insertOtherParticipant(otherParticipant)
                }
            }
        }

        _participants.value = emptyList()
    }

    fun deleteTournament(tournament: TournamentEntity) {
        viewModelScope.launch {
            tournamentRepository.deleteTournament(tournament)
        }
    }

    suspend fun getTournamentById(id: Int): TournamentEntity? {
        return tournamentRepository.getTournamentById(id)
    }
    fun setParticipants(participants: List<TempParticipant>) {
        _participants.value = participants
    }

    suspend fun getAllTournaments(): List<TournamentEntity> {
        return allTournaments.first()
    }

    suspend fun updateTournamentAndParticipants(
        tournamentId: Int,
        tournamentName: String,
        cardGameId: Int,
        startDate: String,
        endDate: String,
        playerPoints: Int,
        playerWins: Int
    ) {
        val tournament = TournamentEntity(
            id = tournamentId,
            tournamentName = tournamentName,
            cardGameId = cardGameId,
            startDate = startDate,
            endDate = endDate
        )
        tournamentRepository.updateTournament(tournament)

        val existingParticipants = participantRepository.getParticipantsByTournamentId(tournamentId).firstOrNull()
        existingParticipants?.firstOrNull()?.let { participant ->
            val updatedParticipant = participant.copy(
                points = playerPoints,
                wins = playerWins
            )
            participantRepository.updateParticipant(updatedParticipant)

            val existingOtherParticipants = otherParticipantRepository.getOtherParticipantsByParticipantId(participant.id).firstOrNull()
            existingOtherParticipants?.forEach {
                otherParticipantRepository.deleteOtherParticipant(it)
            }

            _participants.value.forEach { tempParticipant ->
                val isPointsValid = tempParticipant.points.isNotBlank() && tempParticipant.points.toIntOrNull() != null
                val isWinsValid = tempParticipant.wins.isNotBlank() && tempParticipant.wins.toIntOrNull() != null
                if (tempParticipant.name.isNotBlank() && isPointsValid && isWinsValid) {
                    val otherParticipant = OtherTournamentParticipantEntity(
                        participantId = participant.id,
                        name = tempParticipant.name,
                        points = tempParticipant.points.toInt(),
                        wins = tempParticipant.wins.toInt()
                    )
                    otherParticipantRepository.insertOtherParticipant(otherParticipant)
                }
            }
        }
        _participants.value = emptyList()
    }
}