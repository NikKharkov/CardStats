package com.example.cardstats.main_screens.list_of_games.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardstats.R
import com.example.cardstats.ui.theme.BrightRed
import com.example.cardstats.util.NavigationButton

@Composable
fun GameInfoDialog(
    title: String,
    @DrawableRes icon: Int ,
    description: String,
    modifier: Modifier = Modifier,
    onEditClicked: () -> Unit
) {
    Column(
        modifier = modifier
            .background(BrightRed, RoundedCornerShape(16.dp))
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 40.sp
        )
        Image(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(width = 150.dp, height = 200.dp)
        )
        Text(
            text = description,
            color = Color.White,
            fontSize = 20.sp
        )
        NavigationButton(
            image = R.drawable.btn_edit_game,
            onClick = onEditClicked,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        )
    }
}