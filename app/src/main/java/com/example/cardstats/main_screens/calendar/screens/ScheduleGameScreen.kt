package com.example.cardstats.main_screens.calendar.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.cardstats.R
import com.example.cardstats.add_game.presentation.AddGameViewModel
import com.example.cardstats.main_screens.calendar.components.CustomDateTimePicker
import com.example.cardstats.schedule_game.ScheduleViewModel
import com.example.cardstats.util.CardGamePicker
import com.example.cardstats.util.CustomTopAppBar
import com.example.cardstats.util.NavigationButton
import com.example.cardstats.util.TransparentTextField
import com.example.cardstats.util.Wallpapers
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScheduleGameScreen(
    navController: NavController,
    addGameViewModel: AddGameViewModel = koinViewModel(),
    scheduleViewModel: ScheduleViewModel = koinViewModel(),
    gameId: Int? = null,
) {
    val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val selectedGame by addGameViewModel.selectedGame.collectAsState()
    var showDateFromTimePicker by remember { mutableStateOf(false) }
    var showTillFromTimePicker by remember { mutableStateOf(false) }
    var showCardGamePicker by remember { mutableStateOf(false) }
    val cardGames by addGameViewModel.cardGames.collectAsState()
    val fromDate by scheduleViewModel.fromDate.collectAsState()
    val toDate by scheduleViewModel.toDate.collectAsState()
    val formattedFromDate = fromDate?.let { scheduleViewModel.formatDateTime(it) } ?: scheduleViewModel.formatDateTime(currentDateTime)
    val formattedToDate = toDate?.let { scheduleViewModel.formatDateTime(it) } ?: scheduleViewModel.formatDateTime(currentDateTime)
    val isEditMode = gameId != null
    val context = LocalContext.current

    LaunchedEffect(gameId) {
        if (isEditMode && gameId != null) {
            val game = scheduleViewModel.getScheduledGameById(gameId).first()
            if (game != null) {
                addGameViewModel.updateSelectedGame(cardGames.getOrNull(game.gameId - 1) ?: "")
                scheduleViewModel.updateFromDate(
                    Instant.fromEpochSeconds(game.startTime.toLong())
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                )
                scheduleViewModel.updateToDate(
                    Instant.fromEpochSeconds(game.endTime.toLong())
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                )
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CustomTopAppBar(
                navigationIcon = R.drawable.arrow_back,
                isNavigationIconVisible = true,
                navController = navController
            )
        },
    ) { innerPaddings ->
        Wallpapers()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPaddings),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.padding(80.dp))
            Text(
                text = if (isEditMode) "EDIT YOUR PLAN" else "PLAN YOUR GAME",
                textAlign = TextAlign.Center,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            TransparentTextField(
                value = selectedGame,
                placeholderText = "Choose game",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                icon = R.drawable.arrow_forward,
                readOnly = true,
                onIconClicked = { showCardGamePicker = true },
                trailingIconAvailable = true
            )

            TransparentTextField(
                placeholderText = formattedFromDate,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                icon = R.drawable.arrow_forward,
                readOnly = true,
                onIconClicked = { showDateFromTimePicker = true },
                trailingIconAvailable = true
            )

            TransparentTextField(
                placeholderText = formattedToDate,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                icon = R.drawable.arrow_forward,
                readOnly = true,
                onIconClicked = { showTillFromTimePicker = true },
                trailingIconAvailable = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (selectedGame.isNotEmpty()) {
                NavigationButton(
                    image = R.drawable.btn_save,
                    onClick = {
                        if (selectedGame.isNotEmpty()) {
                            fromDate?.let { from ->
                                toDate?.let { to ->
                                    if (isEditMode && gameId != null) {

                                        scheduleViewModel.updateScheduledGame(
                                            id = gameId,
                                            gameId = cardGames.indexOf(selectedGame) + 1,
                                            startTime = from.toInstant(TimeZone.currentSystemDefault()).epochSeconds.toString(),
                                            endTime = to.toInstant(TimeZone.currentSystemDefault()).epochSeconds.toString(),
                                            context = context
                                        )
                                    } else {

                                        scheduleViewModel.scheduleGame(
                                            gameId = cardGames.indexOf(selectedGame) + 1,
                                            startTime = from.toInstant(TimeZone.currentSystemDefault()).epochSeconds.toString(),
                                            endTime = to.toInstant(TimeZone.currentSystemDefault()).epochSeconds.toString(),
                                            context = context
                                        )
                                    }
                                    navController.popBackStack()
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            } else {
                NavigationButton(
                    image = R.drawable.btn_disabled_save,
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
        }

        if (showCardGamePicker) {
            Dialog(onDismissRequest = { showCardGamePicker = false }) {
                CardGamePicker(
                    onGameSelected = { game ->
                        addGameViewModel.updateSelectedGame(game)
                        showCardGamePicker = false
                    },
                    games = cardGames
                )
            }
        }

        if (showDateFromTimePicker) {
            Dialog(onDismissRequest = { showDateFromTimePicker = false }) {
                CustomDateTimePicker(
                    onDateTimeSelected = { date ->
                        scheduleViewModel.updateFromDate(date)
                        showDateFromTimePicker = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }

        if (showTillFromTimePicker) {
            Dialog(onDismissRequest = { showTillFromTimePicker = false }) {
                CustomDateTimePicker(
                    onDateTimeSelected = { date ->
                        scheduleViewModel.updateToDate(date)
                        showTillFromTimePicker = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}