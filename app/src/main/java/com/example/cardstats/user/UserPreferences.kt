package com.example.cardstats.user

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

    val userDataFlow: Flow<UserData> = context.dataStore.data.map { prefs ->
        UserData(
            name = prefs[PreferencesKeys.USER_NAME] ?: "Me",
            points = prefs[PreferencesKeys.POINTS] ?: 0,
            balance = prefs[PreferencesKeys.BALANCE] ?: 0,
            bestDay = prefs[PreferencesKeys.BEST_DAY] ?: "",
            bestDayPoints = prefs[PreferencesKeys.BEST_DAY_POINTS] ?: 0,
            bestDayIncome = prefs[PreferencesKeys.BEST_DAY_INCOME] ?: 0,
            worstDay = prefs[PreferencesKeys.WORST_DAY] ?: "",
            worstDayPoints = prefs[PreferencesKeys.WORST_DAY_POINTS] ?: 0,
            worstDayIncome = prefs[PreferencesKeys.WORST_DAY_INCOME] ?: 0,
            averageDayPoints = prefs[PreferencesKeys.AVERAGE_DAY_POINTS] ?: 0f,
            winPercentage = prefs[PreferencesKeys.WIN_PERCENTAGE] ?: 0,
            totalGames = prefs[PreferencesKeys.TOTAL_GAMES] ?: 0,
            dollars = prefs[PreferencesKeys.DOLLARS] ?: 0,
            isWin = prefs[PreferencesKeys.IS_WIN] ?: false
        )
    }

    suspend fun updateUserData(
        name: String? = null,
        points: Int? = null,
        balance: Int? = null,
        bestDay: String? = null,
        bestDayPoints: Int? = null,
        bestDayIncome: Int? = null,
        worstDay: String? = null,
        worstDayPoints: Int? = null,
        worstDayIncome: Int? = null,
        averageDayPoints: Float? = null,
        winPercentage: Int? = null,
        totalGames: Int? = null,
        dollars: Int? = null,
        isWin: Boolean? = null
    ) {
        context.dataStore.edit { prefs ->
            name?.let { prefs[PreferencesKeys.USER_NAME] = it }
            points?.let { prefs[PreferencesKeys.POINTS] = it }
            balance?.let { prefs[PreferencesKeys.BALANCE] = it }
            bestDay?.let { prefs[PreferencesKeys.BEST_DAY] = it }
            bestDayPoints?.let { prefs[PreferencesKeys.BEST_DAY_POINTS] = it }
            bestDayIncome?.let { prefs[PreferencesKeys.BEST_DAY_INCOME] = it }
            worstDay?.let { prefs[PreferencesKeys.WORST_DAY] = it }
            worstDayPoints?.let { prefs[PreferencesKeys.WORST_DAY_POINTS] = it }
            worstDayIncome?.let { prefs[PreferencesKeys.WORST_DAY_INCOME] = it }
            averageDayPoints?.let { prefs[PreferencesKeys.AVERAGE_DAY_POINTS] = it }
            winPercentage?.let { prefs[PreferencesKeys.WIN_PERCENTAGE] = it }
            totalGames?.let { prefs[PreferencesKeys.TOTAL_GAMES] = it }
            dollars?.let { prefs[PreferencesKeys.DOLLARS] = it }
            isWin?.let { prefs[PreferencesKeys.IS_WIN] = it }
        }
    }
}