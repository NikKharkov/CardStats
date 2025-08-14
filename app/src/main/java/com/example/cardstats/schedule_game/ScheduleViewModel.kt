package com.example.cardstats.schedule_game

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardstats.notifications.scheduleNotification
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

class ScheduleViewModel(
    private val scheduleGameRepository: ScheduleGameRepository
) : ViewModel() {

    private val _fromDate = MutableStateFlow<LocalDateTime?>(null)
    val fromDate = _fromDate.asStateFlow()

    private val _toDate = MutableStateFlow<LocalDateTime?>(null)
    val toDate = _toDate.asStateFlow()

    fun updateFromDate(date: LocalDateTime?) {
        _fromDate.value = date
    }

    fun updateToDate(date: LocalDateTime?) {
        _toDate.value = date
    }

    fun formatDateTime(date: LocalDateTime?): String {
        val monthNames = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        val dayNames = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        val dateTime = date ?: return "Not set"
        val dayOfWeek = dayNames[dateTime.dayOfWeek.ordinal]
        val month = monthNames[dateTime.monthNumber - 1]
        val day = dateTime.dayOfMonth
        val hour = dateTime.hour.toString().padStart(2, '0')
        val minutes = dateTime.minute.toString().padStart(2, '0')
        return "$month $day, $dayOfWeek; $hour:$minutes"
    }

    fun formatTimeOnly(date: LocalDateTime?): String {
        val dateTime = date ?: return "00:00"
        val hour = dateTime.hour.toString().padStart(2, '0')
        val minutes = dateTime.minute.toString().padStart(2, '0')
        return "$hour:$minutes"
    }

    fun scheduleGame(
        gameId: Int,
        startTime: String,
        endTime: String,
        context: Context,
    ) {
        viewModelScope.launch {
            val scheduledGame = ScheduledGameEntity(
                gameId = gameId,
                startTime = startTime,
                endTime = endTime
            )
            scheduleGameRepository.insertScheduledGame(scheduledGame)
            planNotification(context, startTime.toLong(), gameId)
        }
    }

    private fun planNotification(context: Context, triggerTime: Long, gameId: Int) {
        viewModelScope.launch {
            val gameFlow = scheduleGameRepository.getScheduledGameById(gameId)
            val game = gameFlow.first()
            if (game != null) {
                val gameName = scheduleGameRepository.getAllCardGames().first().find { it.id == gameId }?.name ?: "Game"
                scheduleNotification(
                    context = context,
                    triggerTime = triggerTime * 1000,
                    title = "$gameName game is starting",
                    message = "Your $gameName game is about to start!"
                )
            }
        }
    }

    fun getAllScheduledGames(): Flow<List<ScheduledGameEntity>> {
        return scheduleGameRepository.getAllScheduledGames()
    }

    fun getScheduledGameById(id: Int): Flow<ScheduledGameEntity?> {
        return scheduleGameRepository.getScheduledGameById(id)
    }

    fun updateScheduledGame(
        id: Int,
        gameId: Int,
        startTime: String,
        endTime: String,
        context: Context
    ) {
        viewModelScope.launch {
            val updatedGame = ScheduledGameEntity(
                id = id,
                gameId = gameId,
                startTime = startTime,
                endTime = endTime
            )
            scheduleGameRepository.updateScheduledGame(updatedGame)
            planNotification(context, startTime.toLong(), gameId)
        }
    }
}