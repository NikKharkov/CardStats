package com.example.cardstats.main_screens.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.cardstats.R
import com.example.cardstats.add_card_game.repository.CardGamesRepository
import com.example.cardstats.add_game.data.repository.GameSessionRepository
import com.example.cardstats.main_screens.calendar.components.Calendar
import com.example.cardstats.main_screens.calendar.components.GameSessionsDialog
import com.example.cardstats.main_screens.calendar.components.ScheduledGamesComponent
import com.example.cardstats.navigation.Screen
import com.example.cardstats.schedule_game.ScheduleViewModel
import com.example.cardstats.util.BottomNavBar
import com.example.cardstats.util.CustomTopAppBar
import com.example.cardstats.util.NavigationButton
import com.example.cardstats.util.Wallpapers
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun CalendarScreen(
    navController: NavController,
    scheduleViewModel: ScheduleViewModel = koinViewModel(),
    gameSessionRepository: GameSessionRepository = koinInject(),
    cardGamesRepository: CardGamesRepository = koinInject(),
) {
    val calendarViewModel: CalendarViewModel = koinViewModel()
    val gamesForSelectedDate by calendarViewModel.gamesForSelectedDate.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var dialogDate by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CustomTopAppBar(
                navController = navController
            )
        },
        bottomBar = {
            BottomNavBar(
                modifier = Modifier
                    .padding(8.dp)
                    .padding(WindowInsets.navigationBars.asPaddingValues()),
                navController = navController
            )
        }
    ) { innerPaddings ->
            Wallpapers()
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPaddings)
            ) {
                item {
                    Text(
                        text = "CALENDAR",
                        textAlign = TextAlign.Center,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }

                item {
                    Calendar(
                        modifier = Modifier.padding(8.dp),
                        onDateClicked = { date, _ ->
                            dialogDate = calendarViewModel.formatDate(date)
                            showDialog = true
                        }
                    )
                }

                item {
                    ScheduledGamesComponent(
                        modifier = Modifier.padding(8.dp).heightIn(max = 300.dp),
                        scheduleViewModel = scheduleViewModel,
                        navController = navController
                    )
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        NavigationButton(
                            image = R.drawable.btn_add_game_small,
                            onClick = {
                                navController.navigate(Screen.AddGameScreen.route)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)
                        )

                        NavigationButton(
                            image = R.drawable.btn_set_schedule,
                            onClick = {
                                navController.navigate(Screen.ScheduleGameScreen.route)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)
                        )
                    }
                }
            }
        }


    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            GameSessionsDialog(
                date = dialogDate,
                gameSessions = gamesForSelectedDate,
                gameSessionRepository = gameSessionRepository,
                cardGamesRepository = cardGamesRepository,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}
