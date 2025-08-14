package com.example.cardstats.main_screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cardstats.R
import com.example.cardstats.main_screens.home.components.CalendarComponent
import com.example.cardstats.main_screens.home.components.NavigationBox
import com.example.cardstats.main_screens.home.components.StatisticsScreenComponent
import com.example.cardstats.navigation.Screen
import com.example.cardstats.user.UserViewModel
import com.example.cardstats.util.BottomNavBar
import com.example.cardstats.util.CustomTopAppBar
import com.example.cardstats.util.NavigationButton
import com.example.cardstats.util.Wallpapers
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    userViewModel: UserViewModel = koinViewModel()
) {
    val usersData by userViewModel.user.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CustomTopAppBar(
                navigationIcon = R.drawable.edit_icon,
                isActionButtonVisible = true,
                isNavigationIconVisible = true,
                navController = navController,
                destination = Screen.NotesScreen.route
            )
        },
        bottomBar = {
            BottomNavBar(
                modifier = Modifier.padding(8.dp).padding(WindowInsets.navigationBars.asPaddingValues()),
                navController = navController
            )
        }
    ) { innerPaddings ->
        Wallpapers()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPaddings)
        ) {
            Spacer(modifier = Modifier.padding(16.dp))
            StatisticsScreenComponent(
                points = usersData.points.toString(),
                balance = usersData.balance.toString(),
                winRate = usersData.winPercentage,
                mostSuccessfulDay = usersData.bestDay,
                modifier = Modifier.padding(8.dp)
            )
            CalendarComponent(modifier = Modifier.padding(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                NavigationBox(
                    icon = R.drawable.statistic_icon,
                    text = "My statistics",
                    onClick = {
                        navController.navigate(Screen.StatisticsScreen.route)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(horizontal = 8.dp)
                )
                NavigationBox(
                    icon = R.drawable.trophy_icon,
                    text = "Tournament",
                    onClick = {
                        navController.navigate(Screen.TournamentScreen.route)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(horizontal = 8.dp)
                )
            }
            NavigationButton(
                image = R.drawable.btn_add_game,
                onClick = {
                    navController.navigate(Screen.AddGameScreen.route)
                },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )
        }
    }
}