package com.example.cardstats

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.cardstats.loading.LoadingScreen
import com.example.cardstats.navigation.NavController
import com.example.cardstats.settings.SettingsRepository
import com.example.cardstats.ui.theme.CardStatsTheme
import org.koin.core.context.GlobalContext.get

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _: Boolean ->

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val settingsRepository: SettingsRepository = get().get()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }

        setContent {
            val isDarkTheme by settingsRepository.isDarkThemeFlow.collectAsState(initial = false)

            LaunchedEffect(isDarkTheme) {
                val uiMode = if (isDarkTheme) {
                    Configuration.UI_MODE_NIGHT_YES
                } else {
                    Configuration.UI_MODE_NIGHT_NO
                }

                val newConfig = Configuration(resources.configuration).apply {
                    this.uiMode = uiMode or (this.uiMode and Configuration.UI_MODE_NIGHT_MASK.inv())
                }
                resources.updateConfiguration(newConfig, resources.displayMetrics)
            }

            CardStatsTheme(
                darkTheme = isDarkTheme
            ) {
                var isLoading by remember { mutableStateOf(true) }

                if (isLoading) {
                    LoadingScreen(
                        onLoadingComplete = { isLoading = false }
                    )
                } else {
                    NavController()
                }
            }
        }
    }
}

