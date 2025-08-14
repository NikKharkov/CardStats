package com.example.cardstats.main_screens.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cardstats.add_game.presentation.AddGameViewModel
import com.example.cardstats.navigation.Screen
import com.example.cardstats.schedule_game.ScheduleViewModel
import com.example.cardstats.ui.theme.MainRed
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScheduledGamesComponent(
    modifier: Modifier = Modifier,
    scheduleViewModel: ScheduleViewModel = koinViewModel(),
    addGameViewModel: AddGameViewModel = koinViewModel(),
    navController: NavController,
) {
    val scheduledGames by scheduleViewModel.getAllScheduledGames().collectAsState(initial = emptyList())
    val cardGames by addGameViewModel.cardGames.collectAsState()

    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    val todayGames = scheduledGames.filter { game ->
        val startDateTime = try {
            Instant.fromEpochSeconds(game.startTime.toLong())
                .toLocalDateTime(TimeZone.currentSystemDefault())
        } catch (e: NumberFormatException) {
            Instant.parse(game.startTime)
                .toLocalDateTime(TimeZone.currentSystemDefault())
        }
        startDateTime.date == today
    }

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .background(MainRed, RoundedCornerShape(16.dp))
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        item {
            Text(
                text = "Don't forget schedule for today",
                fontSize = 14.sp,
                color = Color(0xFFE9E9E9),
                modifier = Modifier.padding(4.dp)
            )
        }
        items(todayGames) { game ->
            val startDateTime = try {
                Instant.fromEpochSeconds(game.startTime.toLong())
                    .toLocalDateTime(TimeZone.currentSystemDefault())
            } catch (e: NumberFormatException) {
                Instant.parse(game.startTime)
                    .toLocalDateTime(TimeZone.currentSystemDefault())
            }
            val endDateTime = try {
                Instant.fromEpochSeconds(game.endTime.toLong())
                    .toLocalDateTime(TimeZone.currentSystemDefault())
            } catch (e: NumberFormatException) {
                Instant.parse(game.endTime)
                    .toLocalDateTime(TimeZone.currentSystemDefault())
            }

            ScheduledGameCard(
                gameName = cardGames.getOrNull(game.gameId - 1) ?: "Unknown Game",
                gameStart = scheduleViewModel.formatTimeOnly(startDateTime),
                gameEnd = scheduleViewModel.formatTimeOnly(endDateTime),
                gameId = game.id,
                modifier = Modifier.padding(4.dp),
                onEditClicked = { gameId ->
                    navController.navigate("${Screen.ScheduleGameScreen.route}/$gameId")
                }
            )
        }
    }
}