package com.example.cardstats.navigation

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cardstats.add_game.presentation.AddGameScreen
import com.example.cardstats.add_game.presentation.AddGameViewModel
import com.example.cardstats.loading.IntroductionScreen
import com.example.cardstats.loading.LoadingScreen
import com.example.cardstats.loading.StartScreen
import com.example.cardstats.main_screens.calendar.CalendarScreen
import com.example.cardstats.main_screens.calendar.screens.ScheduleGameScreen
import com.example.cardstats.main_screens.home.HomeScreen
import com.example.cardstats.main_screens.list_of_games.ListOfGames
import com.example.cardstats.main_screens.list_of_games.screens.EditCardGameScreen
import com.example.cardstats.main_screens.statistics.StatisticsScreen
import com.example.cardstats.main_screens.tournament.presentation.TournamentScreen
import com.example.cardstats.main_screens.tournament.presentation.screens.AddTournamentScreen
import com.example.cardstats.main_screens.tournament.presentation.screens.EditTournamentScreen
import com.example.cardstats.main_screens.tournament.presentation.screens.TournamentFullScreen
import com.example.cardstats.notes.presentation.NoteViewModel
import com.example.cardstats.notes.presentation.screens.AddNoteScreen
import com.example.cardstats.notes.presentation.NotesScreen
import com.example.cardstats.notes.presentation.screens.EditNoteScreen
import com.example.cardstats.notes.presentation.screens.FullScreenNote
import com.example.cardstats.settings.SettingsScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "app_prefs")
private val IS_FIRST_LAUNCH = booleanPreferencesKey("first_launch")

@Composable
fun NavController() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val dataStoreFlow = context.datastore.data.map { preferences ->
        preferences[IS_FIRST_LAUNCH] ?: true
    }

    val isFirstLaunchState = dataStoreFlow.collectAsState(initial = null)

    when (val isFirstLaunch = isFirstLaunchState.value) {
        null -> {
            LoadingScreen {}
        }

        else -> {
            val startDestination = if (isFirstLaunch) {
                Screen.IntroductionScreen.route
            } else {
                Screen.HomeScreen.route
            }

            NavHost(navController = navController, startDestination = startDestination) {

                //Introduction
                composable("${Screen.IntroductionScreen.route}?screen={screen}") { navBackStackEntry ->
                    val currentScreen =
                        navBackStackEntry.arguments?.getString("screen")?.toIntOrNull() ?: 1
                    IntroductionScreen(
                        currentScreen = currentScreen,
                        onNextClick = {
                            if (currentScreen < 3) {
                                navController.navigate("${Screen.IntroductionScreen.route}?screen=${currentScreen + 1}")
                            } else {
                                navController.navigate(Screen.StartScreen.route) {
                                    popUpTo(Screen.IntroductionScreen.route) { inclusive = true }
                                }
                            }
                        }
                    )
                }

                //Start
                composable(Screen.StartScreen.route) {
                    StartScreen(
                        navController,
                        Screen.HomeScreen.route,
                        onClick = {
                            scope.launch(Dispatchers.IO) {
                                context.datastore.edit { preferences ->
                                    preferences[IS_FIRST_LAUNCH] = false
                                }
                            }
                        }
                    )
                }

                //Home
                composable(Screen.HomeScreen.route) {
                    HomeScreen(navController)
                }

                //AddGameScreen
                composable(Screen.AddGameScreen.route) {
                    AddGameScreen(navController)
                }

                //List of Games
                composable(Screen.GamesScreen.route) {
                    ListOfGames(navController)
                }

                //Edit card game screen
                composable(
                    route = "edit_card_game/{id}/{name}/{description}",
                    arguments = listOf(
                        navArgument("id") { type = NavType.IntType },
                        navArgument("name") { type = NavType.StringType },
                        navArgument("description") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getInt("id")
                    val name = backStackEntry.arguments?.getString("name")
                    val description = backStackEntry.arguments?.getString("description")
                    EditCardGameScreen(
                        navController = navController,
                        gameId = id,
                        initialName = name,
                        initialDescription = description
                    )
                }

                //Add card game screen
                composable(Screen.EditCardGameScreen.route) {
                    EditCardGameScreen(navController)
                }

                //Calendar screen
                composable(Screen.CalendarScreen.route) {
                    CalendarScreen(navController)
                }

                //Tournament screen
                composable(Screen.TournamentScreen.route) {
                    TournamentScreen(navController)
                }

                //Schedule screen with ID
                composable(
                    route = "${Screen.ScheduleGameScreen.route}/{gameId}",
                    arguments = listOf(navArgument("gameId") {
                        type = NavType.IntType
                        defaultValue = -1
                    })
                ) { backStackEntry ->
                    val gameId = backStackEntry.arguments?.getInt("gameId")
                    ScheduleGameScreen(
                        navController = navController,
                        gameId = if (gameId == -1) null else gameId
                    )
                }

                //Schedule screen
                composable(Screen.ScheduleGameScreen.route) {
                    ScheduleGameScreen(navController)
                }

                //Notes screen
                composable(Screen.NotesScreen.route) {
                    NotesScreen(navController)
                }

                //Add note screen
                composable(Screen.AddNote.route) {
                    AddNoteScreen(navController)
                }

                //Full note
                composable(
                    route = "full_note/{noteId}",
                    arguments = listOf(navArgument("noteId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val noteId = backStackEntry.arguments?.getInt("noteId") ?: 0
                    val noteViewModel: NoteViewModel = koinViewModel()
                    val addGameViewModel: AddGameViewModel = koinViewModel()
                    val notes by noteViewModel.allNotes.collectAsState(initial = emptyList())
                    val cardGames by addGameViewModel.cardGames.collectAsState()

                    val note = notes.find { it.id == noteId }
                    if (note != null) {
                        FullScreenNote(
                            noteId = note.id,
                            noteName = note.title,
                            date = note.date,
                            mainText = note.description,
                            gameName = cardGames.getOrNull(note.gameId - 1) ?: "Unknown game",
                            navController = navController,
                            noteViewModel = noteViewModel
                        )
                    }
                }

                //Edit note
                composable(
                    route = "edit_note/{noteId}/{noteName}/{date}/{mainText}/{gameName}",
                    arguments = listOf(
                        navArgument("noteId") { type = NavType.IntType },
                        navArgument("noteName") { type = NavType.StringType },
                        navArgument("date") { type = NavType.StringType },
                        navArgument("mainText") { type = NavType.StringType },
                        navArgument("gameName") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val noteId = backStackEntry.arguments?.getInt("noteId") ?: 0
                    val noteName = backStackEntry.arguments?.getString("noteName") ?: ""
                    val date = backStackEntry.arguments?.getString("date") ?: ""
                    val mainText = backStackEntry.arguments?.getString("mainText") ?: ""
                    val gameName = backStackEntry.arguments?.getString("gameName") ?: ""
                    EditNoteScreen(
                        navController = navController,
                        noteId = noteId,
                        initialName = noteName,
                        initialDate = date,
                        initialDescription = mainText,
                        initialGameName = gameName
                    )
                }

                //Add tournament screen
                composable(Screen.AddTournamentScreen.route){
                    AddTournamentScreen(navController)
                }

                // Tournament full screen
                composable(
                    route = "tournament_detail/{tournamentId}",
                    arguments = listOf(navArgument("tournamentId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val tournamentId = backStackEntry.arguments?.getInt("tournamentId") ?: 0
                    TournamentFullScreen(navController = navController, tournamentId = tournamentId)
                }

                composable("edit_tournament/{tournamentId}") { backStackEntry ->
                    val tournamentId = backStackEntry.arguments?.getString("tournamentId")?.toIntOrNull() ?: return@composable
                    EditTournamentScreen(
                        navController = navController,
                        tournamentId = tournamentId
                    )
                }

                composable(Screen.StatisticsScreen.route) {
                    StatisticsScreen(navController)
                }

                composable(Screen.SettingsScreen.route) {
                    SettingsScreen(navController)
                }
            }
        }
    }
}