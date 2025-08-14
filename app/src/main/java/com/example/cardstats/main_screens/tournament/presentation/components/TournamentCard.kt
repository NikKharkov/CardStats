package com.example.cardstats.main_screens.tournament.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardstats.ui.theme.BrightRed

@Composable
fun TournamentCard(
    @DrawableRes icon: Int,
    leaderName: String = "Alex",
    leaderPoints: Int = 1500,
    amountOfPlayers: Int = 8,
    tournamentDate: String = "February 10 – February 15, 2025",
    tournamentName: String = "Poker Masters 2025",
    tournamentId: Int,
    onCardClicked: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "Click"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(BrightRed, RoundedCornerShape(16.dp))
            .padding(16.dp)
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onCardClicked(tournamentId) },
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = tournamentName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Icon(
                painter = painterResource(icon),
                contentDescription = "Tournament Icon",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(52.dp)
            )
        }

        Text(
            text = tournamentDate,
            fontSize = 16.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "$amountOfPlayers players",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF30CC38),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "\uD83C\uDFC6 Current Leader: $leaderName ($leaderPoints points)",
            fontSize = 16.sp,
            color = Color(0xFFE9E9E9),
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}