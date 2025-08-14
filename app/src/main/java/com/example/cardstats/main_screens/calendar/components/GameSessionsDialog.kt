package com.example.cardstats.main_screens.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardstats.add_card_game.repository.CardGamesRepository
import com.example.cardstats.add_game.data.entity.GameSessionEntity
import com.example.cardstats.add_game.data.repository.GameSessionRepository
import com.example.cardstats.ui.theme.BrightRed
import kotlinx.coroutines.launch

@Composable
fun GameSessionsDialog(
    date: String,
    gameSessions: List<GameSessionEntity>,
    gameSessionRepository: GameSessionRepository,
    cardGamesRepository: CardGamesRepository,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    LazyColumn(
        modifier = modifier
            .background(BrightRed, RoundedCornerShape(16.dp))
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = date,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.White,
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )
            Text(
                text = "Games played on this day:",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                color = Color.White,
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )
        }

        items(gameSessions) { session ->
            val participants by gameSessionRepository.getParticipantsBySessionId(session.id).collectAsState(initial = emptyList())
            val gameName by cardGamesRepository.getGameNameById(session.cardGameId).collectAsState(initial = "")
            GameSessionCard(
                gameName = gameName,
                participants = participants,
                gameSessionEntity = session,
                onDeleteClick = {
                    scope.launch {
                        gameSessionRepository.deleteGameSession(session.id)
                    }
                },
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}