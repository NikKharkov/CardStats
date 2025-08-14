package com.example.cardstats.add_game.presentation

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
import com.example.cardstats.add_game.presentation.components.WinLoseField
import com.example.cardstats.user.UserViewModel
import com.example.cardstats.util.AddParticipantButton
import com.example.cardstats.util.CardGamePicker
import com.example.cardstats.util.CustomDatePicker
import com.example.cardstats.util.CustomTopAppBar
import com.example.cardstats.util.NavigationButton
import com.example.cardstats.util.TransparentTextField
import com.example.cardstats.util.Wallpapers
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddGameScreen(
    navController: NavController,
    addGameViewModel: AddGameViewModel = koinViewModel(),
    userViewModel: UserViewModel = koinViewModel()
) {
    val usersData by userViewModel.user.collectAsState()
    val selectedDate by addGameViewModel.selectedDate.collectAsState()
    val selectedGame by addGameViewModel.selectedGame.collectAsState()
    val participants by addGameViewModel.participants.collectAsState()
    val cardGames by addGameViewModel.cardGames.collectAsState()

    var showDatePicker by remember { mutableStateOf(false) }
    var showCardGamePicker by remember { mutableStateOf(false) }
    var note by remember { mutableStateOf("") }

    var pointsInput by remember { mutableStateOf(usersData.points.toString()) }
    var dollarsInput by remember { mutableStateOf(usersData.dollars.toString()) }
    var userIsWin by remember { mutableStateOf(usersData.isWin) }

    val currentDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val formattedDate = selectedDate?.let { addGameViewModel.formatDate(it) } ?: addGameViewModel.formatDate(currentDate)

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
                    text = "ADD GAME",
                    textAlign = TextAlign.Center,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                TransparentTextField(
                    placeholderText = formattedDate,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    icon = R.drawable.arrow_forward,
                    readOnly = true,
                    onIconClicked = { showDatePicker = true },
                    trailingIconAvailable = true
                )
                if (showDatePicker) {
                    Dialog(onDismissRequest = { showDatePicker = false }) {
                        CustomDatePicker(
                            onDateSelected = { date ->
                                addGameViewModel.updateDate(date)
                                showDatePicker = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }
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
                Row(modifier = Modifier.fillMaxWidth()) {
                    TransparentTextField(
                        placeholderText = "Me",
                        readOnly = true,
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    )
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    TransparentTextField(
                        value = pointsInput,
                        onValueChange = { newValue ->
                            pointsInput = newValue.filter { it.isDigit() }
                        },
                        placeholderText = "Points",
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    )
                    WinLoseField(
                        isWin = userIsWin,
                        onToggle = {
                            userIsWin = !userIsWin
                            userViewModel.updateUserIsWin(userIsWin)
                        },
                        icon = R.drawable.arrow_left_right,
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    )
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    TransparentTextField(
                        value = dollarsInput,
                        onValueChange = { newDollars ->
                            dollarsInput = newDollars.filter { it.isDigit() }
                        },
                        placeholderText = "Dollars",
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    )
                    WinLoseField(
                        isWin = userIsWin,
                        onToggle = {
                            userIsWin = !userIsWin
                            userViewModel.updateUserIsWin(userIsWin)
                        },
                        icon = R.drawable.dollar,
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
                            addGameViewModel.updateParticipant(index, participant.copy(name = newName))
                        },
                        placeholderText = participant.name.ifEmpty { "Enter name" },
                        trailingIconAvailable = true,
                        icon = R.drawable.delete,
                        iconColor = Color.Red,
                        onIconClicked = { addGameViewModel.removeParticipant(index) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    )
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    TransparentTextField(
                        value = participant.dollars,
                        onValueChange = { newDollars ->
                            addGameViewModel.updateParticipant(index, participant.copy(dollars = newDollars))
                        },
                        placeholderText = "Dollars",
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    )
                    WinLoseField(
                        isWin = participant.isWin,
                        onToggle = {
                            addGameViewModel.updateParticipant(index, participant.copy(isWin = !participant.isWin))
                        },
                        icon = R.drawable.dollar,
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    )
                }
            }

            item {
                AddParticipantButton(onClick = { addGameViewModel.addParticipant() })
                TransparentTextField(
                    value = note,
                    onValueChange = { note = it },
                    placeholderText = "Add a note",
                    maxLines = 5,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                if (selectedGame.isNotBlank()) {
                    NavigationButton(
                        onClick = {
                            val points = pointsInput.toIntOrNull() ?: 0
                            val dollars = dollarsInput.toIntOrNull() ?: 0
                            userViewModel.updateUserPoints(points, userIsWin)
                            userViewModel.updateUserDollars(dollars, userIsWin)
                            addGameViewModel.saveGameSession(points, dollars, userIsWin)
                            navController.popBackStack()
                        },
                        image = R.drawable.btn_save,
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    )
                } else {
                    NavigationButton(
                        image = R.drawable.btn_disabled_save,
                        onClick = {},
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    )
                }
            }
        }
    }
}