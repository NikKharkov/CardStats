package com.example.cardstats.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.navigation.NavController
import com.example.cardstats.R
import com.example.cardstats.navigation.Screen
import com.example.cardstats.util.CustomTopAppBar
import com.example.cardstats.util.Wallpapers
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun SettingsScreen(
    navController: NavController,
    settingsRepository: SettingsRepository = koinInject()
) {
    val isNotificationEnabled by settingsRepository.areNotificationsEnabledFlow.collectAsState(initial = true)
    val isDarkTheme by settingsRepository.isDarkThemeFlow.collectAsState(initial = false)
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CustomTopAppBar(
                navigationIcon = R.drawable.arrow_back,
                isNavigationIconVisible = true,
                navController = navController
            )
        }
    ) { innerPaddings ->
        Wallpapers()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPaddings),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "SETTINGS",
                textAlign = TextAlign.Center,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Notifications:",
                    fontSize = 24.sp,
                    color = Color.White,
                    modifier = Modifier.padding(8.dp)
                )

                Switch(
                    checked = isNotificationEnabled,
                    onCheckedChange = { enabled ->
                        coroutineScope.launch {
                            settingsRepository.setNotificationsEnabled(enabled)
                        }
                    },
                    modifier = Modifier
                        .width(80.dp)
                        .height(40.dp)
                        .padding(8.dp),
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        uncheckedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF48A83F),
                        uncheckedTrackColor = Color(0xFFA8523F),
                        checkedBorderColor = Color.Transparent,
                        uncheckedBorderColor = Color.Transparent
                    )
                )
            }
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(8.dp),
                color = Color.White
            )
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Dark theme:",
                    fontSize = 24.sp,
                    color = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = { enabled ->
                        coroutineScope.launch {
                            settingsRepository.setDarkTheme(enabled)
                            navController.navigate(Screen.HomeScreen.route)
                        }
                    },
                    modifier = Modifier
                        .width(80.dp)
                        .height(40.dp),
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        uncheckedThumbColor = Color(0xFFB0BEC5),
                        checkedTrackColor = Color(0xFF4CAF50),
                        uncheckedTrackColor = Color(0xFF616161),
                        checkedBorderColor = Color.Transparent,
                        uncheckedBorderColor = Color.Transparent
                    )
                )
            }
        }
    }
}