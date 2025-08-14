package com.example.cardstats.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import com.example.cardstats.settings.SettingsRepository
import org.koin.compose.koinInject

@Composable
private fun isDarkThemeEnabled(): Boolean {
    val settingsRepository: SettingsRepository = koinInject()
    return settingsRepository.isDarkThemeFlow.collectAsState(initial = false).value
}


val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val LightRed = Color(0xFFB40000)

val MainRed: Color
    @Composable
    get() = if (isDarkThemeEnabled()) Color(0xFF75191B) else Color(0xFFA32123)

val BrightRed: Color
    @Composable
    get() = if (isDarkThemeEnabled()) Color(0xFFB62326) else Color(0xFFED4C4E)

val MainYellow: Color
    @Composable
    get() = if (isDarkThemeEnabled()) Color(0xFFF6D933) else Color(0xFFFFE448)