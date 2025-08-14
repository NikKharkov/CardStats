package com.example.cardstats.main_screens.tournament.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.navigation.NavController
import com.example.cardstats.R
import com.example.cardstats.main_screens.tournament.presentation.components.TournamentCard
import com.example.cardstats.navigation.Screen
import com.example.cardstats.ui.theme.MainRed
import com.example.cardstats.util.BottomNavBar
import com.example.cardstats.util.CustomTopAppBar
import com.example.cardstats.util.NavigationButton
import com.example.cardstats.util.Wallpapers
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun TournamentScreen(
    navController: NavController,
    tournamentViewModel: TournamentViewModel = koinViewModel()
) {
    val tournaments by tournamentViewModel.allTournaments.collectAsState(initial = emptyList())
    var filterType by remember { mutableStateOf("Active") }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPaddings),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "TOURNAMENTS",
                textAlign = TextAlign.Center,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { filterType = "Active" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (filterType == "Active") Color(0xFF19751E) else Color.DarkGray,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Active")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { filterType = "Completed" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (filterType == "Completed") Color(0xFF19751E) else Color.DarkGray,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Completed")
                }
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .background(MainRed, RoundedCornerShape(16.dp))
            ) {
                val currentCalendar = Calendar.getInstance()
                val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                val filteredTournaments = when (filterType) {
                    "Active" -> tournaments.filter { tournament ->
                        try {
                            val endDate = dateFormat.parse(tournament.endDate)
                            val endCalendar = Calendar.getInstance().apply { time = endDate }
                            endCalendar.after(currentCalendar) || endCalendar.get(Calendar.DAY_OF_YEAR) == currentCalendar.get(Calendar.DAY_OF_YEAR)
                        } catch (e: Exception) {
                            false
                        }
                    }

                    "Completed" -> tournaments.filter { tournament ->
                        try {
                            val endDate = dateFormat.parse(tournament.endDate)
                            val endCalendar = Calendar.getInstance().apply { time = endDate }
                            endCalendar.before(currentCalendar)
                        } catch (e: Exception) {
                            false
                        }
                    }
                    else -> tournaments
                }

                items(filteredTournaments) { tournament ->
                    val participants by tournamentViewModel.getParticipants(tournament.id)
                        .collectAsState(initial = emptyList())

                    val allPlayers = mutableListOf<Pair<String, Int>>()
                    participants.forEach { participant ->
                        allPlayers.add(Pair(participant.playerName, participant.points))
                        val otherParticipants by tournamentViewModel.getOtherParticipants(participant.id)
                            .collectAsState(initial = emptyList())
                        otherParticipants.forEach { other ->
                            allPlayers.add(Pair(other.name, other.points))
                        }
                    }

                    val leader = allPlayers.maxByOrNull { it.second } ?: Pair("Unknown", 0)
                    val leaderName = leader.first
                    val leaderPoints = leader.second

                    val tournamentDate = tournamentViewModel.formatTournamentDate(
                        startDate = tournament.startDate,
                        endDate = tournament.endDate
                    )

                    TournamentCard(
                        icon = if (filterType == "Active") R.drawable.trophy_icon else R.drawable.check_icon,
                        tournamentName = tournament.tournamentName,
                        tournamentDate = tournamentDate,
                        amountOfPlayers = allPlayers.size,
                        leaderName = leaderName,
                        leaderPoints = leaderPoints,
                        tournamentId = tournament.id,
                        onCardClicked = { id ->
                            navController.navigate("tournament_detail/$id")
                        },
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            NavigationButton(
                image = R.drawable.btn_create_tournament,
                onClick = {
                    navController.navigate(Screen.AddTournamentScreen.route)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}