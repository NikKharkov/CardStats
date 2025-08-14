package com.example.cardstats.notes.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cardstats.R
import com.example.cardstats.notes.data.NoteEntity
import com.example.cardstats.notes.presentation.NoteViewModel
import com.example.cardstats.ui.theme.MainRed
import com.example.cardstats.util.CustomTopAppBar
import com.example.cardstats.util.NavigationButton
import com.example.cardstats.util.Wallpapers
import org.koin.androidx.compose.koinViewModel

@Composable
fun FullScreenNote(
    noteId: Int,
    noteName: String = "Name of the note",
    date: String = "17.03.2025",
    mainText: String = "Bluffing too much in early positions is risky...",
    gameName: String = "Poker",
    modifier: Modifier = Modifier,
    navController: NavController,
    noteViewModel: NoteViewModel = koinViewModel(),
) {
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
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .background(MainRed, RoundedCornerShape(16.dp)),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
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
                    text = noteName,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                Text(
                    text = date,
                    fontSize = 24.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                Text(
                    text = mainText,
                    fontSize = 24.sp,
                    color = Color.White,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                NavigationButton(
                    image = R.drawable.btn_edit_small,
                    onClick = {
                        navController.navigate(
                            "edit_note/$noteId/$noteName/$date/$mainText/$gameName"
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                )
                NavigationButton(
                    image = R.drawable.btn_delete,
                    onClick = {
                        noteViewModel.deleteNote(
                            NoteEntity(
                                id = noteId,
                                title = noteName,
                                date = date,
                                gameId = 0,
                                description = mainText,
                                isIconFilled = false
                            )
                        )
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                )
            }
        }
    }
}