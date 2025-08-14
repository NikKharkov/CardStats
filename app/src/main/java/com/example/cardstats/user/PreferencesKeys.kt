package com.example.cardstats.user

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val USER_NAME = stringPreferencesKey("user_name")
    val POINTS = intPreferencesKey("points")
    val BALANCE = intPreferencesKey("balance")

    val BEST_DAY = stringPreferencesKey("best_day")
    val BEST_DAY_POINTS = intPreferencesKey("best_day_points")
    val BEST_DAY_INCOME = intPreferencesKey("best_day_income")

    val WORST_DAY = stringPreferencesKey("worst_day")
    val WORST_DAY_POINTS = intPreferencesKey("worst_day_points")
    val WORST_DAY_INCOME = intPreferencesKey("worst_day_income")

    val AVERAGE_DAY_POINTS = floatPreferencesKey("average_day_points")
    val WIN_PERCENTAGE = intPreferencesKey("win_percentage")
    val TOTAL_GAMES = intPreferencesKey("total_games")
    val IS_WIN = booleanPreferencesKey("is_win")
    val DOLLARS = intPreferencesKey("dollars")
}