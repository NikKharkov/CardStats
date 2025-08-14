package com.example.cardstats.add_card_game.model

import androidx.annotation.DrawableRes
import com.example.cardstats.R

data class Game(
    val id: Int,
    val name: String,
    val description: String,
    @DrawableRes val icon: Int = R.drawable.cards
)
