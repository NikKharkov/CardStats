package com.example.cardstats.main_screens.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardstats.R
import com.example.cardstats.add_game.data.entity.GameSessionEntity
import com.example.cardstats.add_game.data.entity.ParticipantEntity

@Composable
fun GameSessionCard(
    gameName: String,
    participants: List<ParticipantEntity>,
    gameSessionEntity: GameSessionEntity,
    onDeleteClick: (Int) -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFD35759), RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.cards),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(16.dp)
                        .padding(end = 4.dp)
                )
                Text(
                    text = gameName,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Icon(
                painter = painterResource(android.R.drawable.ic_menu_delete),
                contentDescription = "Delete",
                tint = Color.White,
                modifier = Modifier
                    .size(20.dp)
                    .clickable {
                        onDeleteClick(gameSessionEntity.id)
                    }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Players: ${participants.joinToString(", ") { it.name }}",
            color = Color.White,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                participants.forEach { participant ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${participant.name}: ",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                        Text(
                            text = if (participant.isWin) "+$${participant.dollars}" else "-$${participant.dollars}",
                            color = if (participant.isWin) Color.Green else Color.Red,
                            fontSize = 14.sp
                        )
                        if (participant.isWin) {
                            Text(
                                text = "\uD83D\uDCB0 (winner)",
                                color = Color.Yellow,
                                fontSize = 12.sp
                            )
                        } else {
                            Icon(
                                painter = painterResource(android.R.drawable.ic_delete),
                                contentDescription = "Loser",
                                tint = Color.Red,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}