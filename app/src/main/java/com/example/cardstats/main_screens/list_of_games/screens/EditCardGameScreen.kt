package com.example.cardstats.main_screens.list_of_games.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cardstats.R
import com.example.cardstats.main_screens.list_of_games.CardGamesViewModel
import com.example.cardstats.util.CustomTopAppBar
import com.example.cardstats.util.NavigationButton
import com.example.cardstats.util.TransparentTextField
import com.example.cardstats.util.Wallpapers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun EditCardGameScreen(
    navController: NavController,
    cardGamesViewModel: CardGamesViewModel = koinViewModel(),
    gameId: Int? = null,
    initialName: String? = null,
    initialDescription: String? = null
) {
    val scope = rememberCoroutineScope()
    var name by remember { mutableStateOf(initialName ?: "") }
    var description by remember { mutableStateOf(initialDescription ?: "") }
    val isEditMode = gameId != null

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CustomTopAppBar(
                navigationIcon = R.drawable.arrow_back,
                isNavigationIconVisible = true,
                navController = navController
            )
        },
    ) { innerPaddings ->
        Wallpapers()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPaddings),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.padding(80.dp))
            Text(
                text = if (isEditMode) "EDIT GAME" else "ADD YOUR GAME",
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
                placeholderText = "Name of the game",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            TransparentTextField(
                value = description,
                onValueChange = { description = it },
                placeholderText = "Rules",
                maxLines = 5,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            if (name.isNotBlank() && description.isNotBlank()) {
                NavigationButton(
                    image = R.drawable.btn_save,
                    onClick = {
                        scope.launch {
                            if (name.isNotBlank() && description.isNotBlank()) {
                                if (isEditMode) {
                                    gameId?.let { id ->
                                        cardGamesViewModel.updateCard(id, name, description)
                                    }
                                } else {
                                    cardGamesViewModel.addNewGame(name, description)
                                }
                                navController.popBackStack()
                            }
                        }
                    },
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
    }
}