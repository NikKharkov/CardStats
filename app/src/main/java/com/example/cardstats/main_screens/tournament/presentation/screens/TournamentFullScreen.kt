package com.example.cardstats.main_screens.tournament.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cardstats.R
import com.example.cardstats.add_game.presentation.AddGameViewModel
import com.example.cardstats.main_screens.tournament.data.entity.TournamentEntity
import com.example.cardstats.main_screens.tournament.data.entity.TournamentParticipantEntity
import com.example.cardstats.main_screens.tournament.presentation.TournamentViewModel
import com.example.cardstats.ui.theme.MainRed
import com.example.cardstats.util.CustomTopAppBar
import com.example.cardstats.util.NavigationButton
import com.example.cardstats.util.Wallpapers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun TournamentFullScreen(
    navController: NavController,
    tournamentViewModel: TournamentViewModel = koinViewModel(),
    addGameViewModel: AddGameViewModel = koinViewModel(),
    tournamentId: Int = 0
) {
    var tournament by remember { mutableStateOf<TournamentEntity?>(null) }
    var allPlayers by remember { mutableStateOf<List<TournamentParticipantEntity>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val cardGames by addGameViewModel.cardGames.collectAsState()

    LaunchedEffect(tournamentId) {
        try {
            tournament = tournamentViewModel.getTournamentById(tournamentId)
                ?: return@LaunchedEffect

            val participants = tournamentViewModel.getParticipants(tournamentId)
                .first()

            val otherParticipants = participants.map { participant ->
                tournamentViewModel.getOtherParticipants(participant.id).first()
            }.flatten()

            val playerMap = mutableMapOf<String, TournamentParticipantEntity>()
            participants.forEach { p ->
                playerMap[p.playerName] = p
            }
            otherParticipants.forEach { op ->
                if (!playerMap.containsKey(op.name)) {
                    playerMap[op.name] = TournamentParticipantEntity(
                        id = 0,
                        tournamentId = tournamentId,
                        playerName = op.name,
                        points = op.points,
                        wins = op.wins
                    )
                }
            }
            allPlayers = playerMap.values.sortedByDescending { it.points }
        } catch (_: Exception) {

        } finally {
            isLoading = false
        }
    }

    val gameName = tournament?.cardGameId?.let { cardGameId ->
        cardGames.getOrNull(cardGameId - 1) ?: "Unknown Game"
    } ?: "Unknown Game"

    val tournamentDate = tournament?.let {
        tournamentViewModel.formatTournamentDate(it.startDate, it.endDate)
    } ?: "Loading..."

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPaddings),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .background(MainRed, RoundedCornerShape(16.dp)),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(R.drawable.trophy_icon),
                        contentDescription = "Tournament Trophy",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(48.dp).padding(8.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = gameName,
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = Modifier
                                .background(Color(0xFFFDAE02), RoundedCornerShape(24.dp))
                                .padding(vertical = 12.dp, horizontal = 20.dp)
                        )
                    }
                    Text(
                        text = tournament?.tournamentName ?: "No Tournament",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    )
                    Text(
                        text = tournamentDate,
                        fontSize = 24.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    )
                    Text(
                        text = "${allPlayers.size} players",
                        fontSize = 24.sp,
                        color = Color(0xFF30CC38),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    )
                    Text(
                        text = "Leaderboard",
                        fontSize = 24.sp,
                        color = Color.White,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    )
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    ) {
                        itemsIndexed(allPlayers) { index, player ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${index + 1}. ${player.playerName} - ${player.points} pts (${player.wins} wins)",
                                    fontSize = 24.sp,
                                    color = if (index == 0) Color(0xFFE9E9E9) else Color.White,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                                when (index) {
                                    0 -> Text(
                                        text = "\uD83E\uDD47",
                                        fontSize = 24.sp,
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                    )
                                    1 -> Text(
                                        text = "\uD83E\uDD48",
                                        fontSize = 24.sp,
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                    )
                                    2 -> Text(
                                        text = "\uD83E\uDD49",
                                        fontSize = 24.sp,
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                    )
                                    else -> Spacer(modifier = Modifier.size(24.dp))
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    NavigationButton(
                        image = R.drawable.btn_edit_small,
                        onClick = { navController.navigate("edit_tournament/$tournamentId") },
                        modifier = Modifier.weight(1f).padding(8.dp)
                    )
                    NavigationButton(
                        image = R.drawable.btn_delete,
                        onClick = {
                            scope.launch {
                                tournament?.let { tournamentViewModel.deleteTournament(it) }
                                navController.popBackStack()
                            }
                        },
                        modifier = Modifier.weight(1f).padding(8.dp)
                    )
                }
            }
        }
    }
}