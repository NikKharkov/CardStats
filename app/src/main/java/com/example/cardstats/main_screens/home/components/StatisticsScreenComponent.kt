package com.example.cardstats.main_screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardstats.R
import com.example.cardstats.ui.theme.MainRed

@Composable
fun StatisticsScreenComponent(
    modifier: Modifier = Modifier,
    points: String?,
    balance: String?,
    winRate: Int,
    mostSuccessfulDay: String
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MainRed)
            .padding(8.dp)
    ) {
        Text(
            text = "General statistics",
            textAlign = TextAlign.Start,
            color = Color(0xFFE9E9E9),
            fontSize = 20.sp
        )
        Text(
            text = "${points ?: 0} points",
            textAlign = TextAlign.Center,
            color = Color.White,
            fontSize = 24.sp,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${balance ?: 0}",
                textAlign = TextAlign.Center,
                color = Color.White,
                fontSize = 48.sp
            )
            Icon(
                painter = painterResource(R.drawable.dollar),
                contentDescription = null,
                modifier = Modifier.size(52.dp),
                tint = Color.Unspecified
            )
        }
        Text(
            text = "Win rate: $winRate%",
            textAlign = TextAlign.Start,
            color = Color.White,
            fontSize = 24.sp,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Most successful day: $mostSuccessfulDay",
            textAlign = TextAlign.Start,
            color = Color.White,
            fontSize = 24.sp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun StatisticsScreenComponentPreview() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        StatisticsScreenComponent(
            points = "123",
            modifier = Modifier.padding(8.dp),
            balance = "23",
            winRate = 75,
            mostSuccessfulDay = "26.01"
        )
    }
}