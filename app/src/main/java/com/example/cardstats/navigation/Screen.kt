package com.example.cardstats.navigation

import com.example.cardstats.R

sealed class Screen(val route: String, val icon: Int = 0) {
    data object IntroductionScreen : Screen("introduction")
    data object StartScreen : Screen("start")
    data object HomeScreen : Screen("home", R.drawable.home)
    data object GamesScreen : Screen("games", R.drawable.billiards)
    data object CalendarScreen : Screen("calendar", R.drawable.calendar)
    data object TournamentScreen : Screen("tournament", R.drawable.trophy)
    data object StatisticsScreen : Screen("statistics", R.drawable.bar_chart)
    data object AddGameScreen : Screen("add_game")
    data object EditCardGameScreen : Screen("edit_card_game")
    data object NotesScreen : Screen("notes")
    data object AddNote : Screen("add_note")
    data object ScheduleGameScreen : Screen("schedule")
    data object AddTournamentScreen : Screen("add_tournament")
    data object SettingsScreen : Screen("settings")
}

