package com.example.cardstats.notes.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cardstats.R
import com.example.cardstats.add_game.presentation.AddGameViewModel
import com.example.cardstats.navigation.Screen
import com.example.cardstats.notes.presentation.components.NoteCard
import com.example.cardstats.ui.theme.MainRed
import com.example.cardstats.util.CustomTopAppBar
import com.example.cardstats.util.NavigationButton
import com.example.cardstats.util.Wallpapers
import org.koin.androidx.compose.koinViewModel

@Composable
fun NotesScreen(
    navController: NavController,
    noteViewModel: NoteViewModel = koinViewModel(),
    addGameViewModel: AddGameViewModel = koinViewModel()
) {
    val notes by noteViewModel.allNotes.collectAsState(initial = emptyList())
    val cardGames by addGameViewModel.cardGames.collectAsState()
    Log.d("Note Screen","$notes")

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
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .background(MainRed, RoundedCornerShape(16.dp))
            ) {
                item {
                    Text(
                        text = "MY NOTES",
                        textAlign = TextAlign.Center,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }

                items(notes, key = { note -> note.id }) { note ->
                    NoteCard(
                        noteId = note.id,
                        noteName = note.title,
                        date = note.date,
                        isIconFilled = note.isIconFilled,
                        mainText = note.description,
                        gameName = cardGames.getOrNull(note.gameId - 1) ?: "Unknown game",
                        modifier = Modifier.padding(8.dp),
                        onNoteClicked = { noteId ->
                            navController.navigate("full_note/$noteId")
                        },
                        noteViewModel = noteViewModel
                    )
                }
            }

            NavigationButton(
                image = R.drawable.btn_add_note,
                onClick = {
                    navController.navigate(Screen.AddNote.route)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}