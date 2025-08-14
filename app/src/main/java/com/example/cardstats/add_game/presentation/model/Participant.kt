package com.example.cardstats.add_game.presentation.model

data class Participant(
    val name: String = "",
    val points: String = "0",
    val dollars: String = "0",
    val isWin: Boolean = false
)
