package com.example.cardstats.main_screens.list_of_games

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.navigation.compose.rememberNavController
import com.example.cardstats.R
import com.example.cardstats.add_card_game.model.Game
import com.example.cardstats.main_screens.list_of_games.components.GameCell
import com.example.cardstats.main_screens.list_of_games.components.GameInfoDialog
import com.example.cardstats.navigation.Screen
import com.example.cardstats.util.BottomNavBar
import com.example.cardstats.util.CustomTopAppBar
import com.example.cardstats.util.NavigationButton
import com.example.cardstats.util.Wallpapers
import org.koin.androidx.compose.koinViewModel

@Composable
fun ListOfGames(
    navController: NavController = rememberNavController(),
    cardGamesViewModel: CardGamesViewModel = koinViewModel()
) {
    var showGameInfoDialog by remember { mutableStateOf(false) }
    var selectedGame by remember { mutableStateOf<Game?>(null) }

    val cardGames = cardGamesViewModel.games.collectAsState()

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
                .padding(innerPaddings)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
            ) {
                item(span = { GridItemSpan(3) }) {
                    Text(
                        text = "LIST OF GAMES",
                        textAlign = TextAlign.Center,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
                items(cardGames.value) { cardGame ->
                    GameCell(
                        icon = cardGame.icon,
                        name = cardGame.name,
                        modifier = Modifier.height(160.dp),
                        onClick = {
                            selectedGame = cardGame
                            showGameInfoDialog = true
                        }
                    )
                }
            }

            if (showGameInfoDialog && selectedGame != null) {
                Dialog(
                    onDismissRequest = { showGameInfoDialog = false }
                ) {
                    GameInfoDialog(
                        title = selectedGame!!.name,
                        icon = selectedGame!!.icon,
                        description = selectedGame!!.description,
                        onEditClicked = {
                            showGameInfoDialog = false
                            navController.navigate("edit_card_game/${selectedGame!!.id}/${selectedGame!!.name}/${selectedGame!!.description}")                        }
                    )
                }
            }

            NavigationButton(
                image = R.drawable.btn_add_your_game,
                onClick = {
                    navController.navigate(Screen.EditCardGameScreen.route)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    }
}

