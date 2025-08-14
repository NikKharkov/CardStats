package com.example.cardstats.main_screens.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardstats.R
import com.example.cardstats.ui.theme.BrightRed
import com.example.cardstats.util.NavigationButton

@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
fun ScheduledGameCard(
    gameName: String = "Poker",
    gameStart: String = "15:00",
    gameEnd: String = "16:00",
    gameId: Int = -1,
    modifier: Modifier = Modifier,
    onEditClicked: (Int) -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(BrightRed, RoundedCornerShape(16.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.alarm_icon),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .size(48.dp)
                .padding(4.dp)
        )

        Column(
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text(
                text = gameName,
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier.padding(4.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.alarm),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "$gameStart - $gameEnd",
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        NavigationButton(
            image = R.drawable.btn_edit_yellow,
            onClick = { onEditClicked(gameId) },
            modifier = Modifier
                .size(width = 64.dp, height = 40.dp)
        )
    }
}