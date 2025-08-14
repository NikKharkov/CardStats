package com.example.cardstats.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.cardstats.settings.SETTINGS_PREFERENCES.IS_DARK_THEME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object SETTINGS_PREFERENCES {
    val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
    val ARE_NOTIFICATIONS_ENABLED = booleanPreferencesKey("are_notifications_enabled")
}

class SettingsRepository(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings_prefs")

    val isDarkThemeFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_DARK_THEME] ?: false
        }

    val areNotificationsEnabledFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[SETTINGS_PREFERENCES.ARE_NOTIFICATIONS_ENABLED] ?: true
        }

    suspend fun setDarkTheme(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_DARK_THEME] = enabled
        }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[SETTINGS_PREFERENCES.ARE_NOTIFICATIONS_ENABLED] = enabled
        }
    }
}