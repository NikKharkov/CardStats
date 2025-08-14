package com.example.cardstats.loading

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardstats.R
import com.example.cardstats.util.NavigationButton
import com.example.cardstats.loading.model.ScreenContent

@Composable
fun IntroductionScreen(currentScreen: Int, onNextClick: () -> Unit) {
    Log.d("Intro Screen", "invoke")
    val screenContent = when (currentScreen) {
        1 -> ScreenContent(
            text = "Welcome to CardStats! \uD83C\uDF89\n\nTrack and analyze your\n\ncard games.",
            alignment = Alignment.BottomStart,
            ofSetDp = 150,
            linesImage = R.drawable.color_line1,
            mainImage = R.drawable.woman_celebrating
        )

        2 -> ScreenContent(
            text = "Add games! \n\n \uD83D\uDCC5 Log results and \n\n participants easily.",
            alignment = Alignment.Center,
            ofSetDp = 0,
            linesImage = R.drawable.color_line2,
            mainImage = R.drawable.joker
        )

        3 -> ScreenContent(
            text = "Monitor progress! \uD83D\uDCCA \n\nStats,charts, and\n\n tournaments in one \n\nplace!",
            alignment = Alignment.BottomCenter,
            ofSetDp = 175,
            linesImage = R.drawable.color_line3,
            mainImage = R.drawable.happy_man
        )

        else -> ScreenContent("", Alignment.BottomStart, -1, 0, 0)
    }

    Crossfade(
        targetState = screenContent,
        animationSpec = tween(300),
        label = "Screen Transition"
    )
    { screen ->
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(R.drawable.blue_bg),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = screen.text,
                    fontSize = 32.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(innerPadding)
                )

                Image(
                    painter = painterResource(screen.linesImage),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )

                Image(
                    painter = painterResource(screen.mainImage),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .align(screen.alignment)
                        .offset(y = screen.ofSetDp.dp),
                )

                NavigationButton(
                    image = R.drawable.btn_next,
                    onClick = onNextClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(innerPadding)
                        .align(Alignment.BottomCenter)
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun IntroductionScreenPreview() {
    IntroductionScreen(
        currentScreen = 2,
        onNextClick = {}
    )
}