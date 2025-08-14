package com.example.cardstats.main_screens.tournament.presentation.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.cardstats.R
import com.example.cardstats.add_game.presentation.AddGameViewModel
import com.example.cardstats.main_screens.tournament.presentation.TournamentViewModel
import com.example.cardstats.util.AddParticipantButton
import com.example.cardstats.util.CardGamePicker
import com.example.cardstats.util.CustomDatePicker
import com.example.cardstats.util.CustomTopAppBar
import com.example.cardstats.util.NavigationButton
import com.example.cardstats.util.TransparentTextField
import com.example.cardstats.util.Wallpapers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddTournamentScreen(
    navController: NavController,
    tournamentViewModel: TournamentViewModel = koinViewModel(),
    addGameViewModel: AddGameViewModel = koinViewModel()
) {
    var tournamentName by remember { mutableStateOf("") }
    val selectedGame by addGameViewModel.selectedGame.collectAsState()
    val cardGames by addGameViewModel.cardGames.collectAsState()
    var showCardGamePicker by remember { mutableStateOf(false) }
    val fromDate by tournamentViewModel.fromDate.collectAsState()
    val toDate by tournamentViewModel.toDate.collectAsState()
    var showDateFromTimePicker by remember { mutableStateOf(false) }
    var showTillFromTimePicker by remember { mutableStateOf(false) }
    val formattedFromDate = fromDate?.let { tournamentViewModel.formatDate(it) }
    val formattedToDate = toDate?.let { tournamentViewModel.formatDate(it) }
    var points by remember { mutableStateOf("") }
    var wins by remember { mutableStateOf("") }
    val participants by tournamentViewModel.participants.collectAsState()
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CustomTopAppBar(
                navigationIcon = R.drawable.arrow_back,
                isNavigationIconVisible = true,
                navController = navController
            )
        }
    ) { innerPaddings ->
        Wallpapers()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPaddings)
        ) {
            item {
                Text(
                    text = "ADD TOURNAMENT",
                    textAlign = TextAlign.Center,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                TransparentTextField(
                    value = tournamentName,
                    onValueChange = { tournamentName = it },
                    placeholderText = "Tournament Name",
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
                    placeholderText = formattedFromDate ?: "From",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    icon = R.drawable.arrow_forward,
                    readOnly = true,
                    onIconClicked = { showDateFromTimePicker = true },
                    trailingIconAvailable = true
                )

                TransparentTextField(
                    placeholderText = formattedToDate ?: "Till",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    icon = R.drawable.arrow_forward,
                    readOnly = true,
                    onIconClicked = { showTillFromTimePicker = true },
                    trailingIconAvailable = true
                )

                TransparentTextField(
                    value = "Me",
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                Row(modifier = Modifier.fillMaxWidth()) {
                    TransparentTextField(
                        value = points,
                        onValueChange = { points = it },
                        placeholderText = "Points",
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    )
                    TransparentTextField(
                        value = wins,
                        onValueChange = { wins = it },
                        placeholderText = "Wins",
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    )
                }
            }

            itemsIndexed(participants) { index, participant ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    TransparentTextField(
                        value = participant.name,
                        onValueChange = { newName ->
                            tournamentViewModel.updateParticipant(index, participant.copy(name = newName))
                        },
                        placeholderText = participant.name.ifEmpty { "Enter name" },
                        trailingIconAvailable = true,
                        icon = R.drawable.delete,
                        iconColor = Color.Red,
                        onIconClicked = { tournamentViewModel.removeParticipant(index) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    )
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    TransparentTextField(
                        value = participant.points,
                        onValueChange = { newPoints ->
                            tournamentViewModel.updateParticipant(index, participant.copy(points = newPoints.filter { it.isDigit() }))
                        },
                        placeholderText = "Points",
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    )
                    TransparentTextField(
                        value = participant.wins,
                        onValueChange = { newWins ->
                            tournamentViewModel.updateParticipant(index, participant.copy(wins = newWins.filter { it.isDigit() }))
                        },
                        placeholderText = "Wins",
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    )
                }
            }

            item {
                AddParticipantButton(onClick = { tournamentViewModel.addParticipant() })
                val isPointsValid = points.isNotBlank() && points.toIntOrNull() != null
                val isWinsValid = wins.isNotBlank() && wins.toIntOrNull() != null
                if (tournamentName.isNotBlank() && selectedGame.isNotBlank() && formattedFromDate != null && formattedToDate != null && isPointsValid && isWinsValid) {
                    NavigationButton(
                        image = R.drawable.btn_save,
                        onClick = {
                            scope.launch {
                                val cardGameId = cardGames.indexOf(selectedGame) + 1
                                tournamentViewModel.saveTournamentAndParticipant(
                                    tournamentName = tournamentName,
                                    cardGameId = cardGameId,
                                    startDate = formattedFromDate,
                                    endDate = formattedToDate,
                                    playerName = "Me",
                                    points = points.toInt(),
                                    wins = wins.toInt()
                                )
                                navController.popBackStack()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                } else {
                    NavigationButton(
                        image = R.drawable.btn_disabled_save,
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
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
                CustomDatePicker(
                    onDateSelected = { date ->
                        tournamentViewModel.updateFromDate(date)
                        showDateFromTimePicker = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
        }

        if (showTillFromTimePicker) {
            Dialog(onDismissRequest = { showTillFromTimePicker = false }) {
                CustomDatePicker(
                    onDateSelected = { date ->
                        tournamentViewModel.updateToDate(date)
                        showTillFromTimePicker = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
        }
    }
}