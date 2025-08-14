package com.example.cardstats.util

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardstats.ui.theme.MainRed

@Composable
fun CardGamePicker(
    onGameSelected: (String) -> Unit,
    games: List<String>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .background(MainRed, RoundedCornerShape(16.dp))
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(games) { game ->
            Text(
                text = game,
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.padding(8.dp).clickable {
                    onGameSelected(game)
                }
            )

            if (game != games[games.size - 1]) {
                HorizontalDivider(thickness = 1.dp, color = Color.White)
            }
        }
    }
}