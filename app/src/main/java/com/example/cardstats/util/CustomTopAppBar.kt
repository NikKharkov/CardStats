package com.example.cardstats.util

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.cardstats.R
import com.example.cardstats.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    navController: NavController,
    destination: String = "",
    isNavigationIconVisible: Boolean = false,
    isActionButtonVisible: Boolean = false,
    @DrawableRes navigationIcon: Int = 0,
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.card_stats),
                    contentDescription = null,
                    contentScale = ContentScale.Inside,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        },
        navigationIcon = {
            if (isNavigationIconVisible) {
                CustomIconButton(
                    onClick = if (destination.isEmpty()) {
                        { navController.popBackStack() }
                    } else {
                        { navController.navigate(destination) }
                    },
                    icon = navigationIcon,
                    color = Color.White
                )
            }
        },
        actions = {
            if (isActionButtonVisible) {
                CustomIconButton(
                    onClick = {
                        navController.navigate(Screen.SettingsScreen.route)
                    },
                    icon = R.drawable.settings
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent
        )
    )
}

