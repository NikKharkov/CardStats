package com.example.cardstats.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.cardstats.navigation.Screen
import com.example.cardstats.ui.theme.BrightRed

@Composable
fun BottomNavBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(BrightRed, RoundedCornerShape(32.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        screens_list.forEach { screen ->
            CustomIconButton(
                onClick = {
                    navController.navigate(screen.route)
                },
                icon = screen.icon,
                isSelected = currentRoute == screen.route
            )
        }
    }
}

private val screens_list = listOf(
    Screen.HomeScreen,
    Screen.GamesScreen,
    Screen.CalendarScreen,
    Screen.TournamentScreen,
    Screen.StatisticsScreen
)