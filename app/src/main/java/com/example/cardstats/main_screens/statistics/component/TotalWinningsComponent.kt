package com.example.cardstats.main_screens.statistics.component

import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardstats.R
import com.example.cardstats.ui.theme.MainRed

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun TotalWinningsComponent(
    balance: Int = 152,
    points: Int = 123,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(MainRed, RoundedCornerShape(16.dp))
    ) {
        Text(
            text = "\uD83C\uDFC6 Total winnings",
            fontSize = 16.sp,
            color = Color(0xFFE9E9E9),
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = "$points points",
            fontSize = 20.sp,
            color = Color.White,
            modifier = Modifier.padding(8.dp),
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "$balance",
                fontSize = 40.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
            Icon(
                painter = painterResource(R.drawable.dollar),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(42.dp)
            )
        }
    }
}