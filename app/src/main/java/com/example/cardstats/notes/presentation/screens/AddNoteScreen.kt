package com.example.cardstats.notes.presentation.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.cardstats.R
import com.example.cardstats.add_game.presentation.AddGameViewModel
import com.example.cardstats.notes.presentation.NoteViewModel
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddNoteScreen(
    navController: NavController,
    addGameViewModel: AddGameViewModel = koinViewModel(),
    noteViewModel: NoteViewModel = koinViewModel()
) {
    val currentDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val selectedGame by addGameViewModel.selectedGame.collectAsState()
    val cardGames by addGameViewModel.cardGames.collectAsState()
    var showCardGamePicker by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(currentDate) }
    val formattedDate = noteViewModel.formatDate(selectedDate)

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPaddings)
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "WRITE NOTES",
                    textAlign = TextAlign.Center,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                TransparentTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholderText = "Name",
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

                TransparentTextField(
                    value = selectedGame,
                    placeholderText = "Game",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    icon = R.drawable.arrow_forward,
                    readOnly = true,
                    onIconClicked = { showCardGamePicker = true },
                    trailingIconAvailable = true
                )

                TransparentTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholderText = "Add a note",
                    maxLines = 10,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }

            if (selectedGame.isNotBlank() && name.isNotBlank() && description.isNotBlank()) {
                NavigationButton(
                    onClick = {
                        noteViewModel.saveNote(
                            title = name,
                            date = formattedDate,
                            gameId = cardGames.indexOf(selectedGame) + 1,
                            description = description
                        )
                        navController.popBackStack()
                    },
                    image = R.drawable.btn_save,
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

        if (showDatePicker) {
            Dialog(onDismissRequest = { showDatePicker = false }) {
                CustomDatePicker(
                    onDateSelected = { newDate ->
                        selectedDate = newDate
                        showDatePicker = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
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
    }
}