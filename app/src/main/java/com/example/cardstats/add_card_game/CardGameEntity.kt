package com.example.cardstats.add_card_game

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "card_games")
data class CardGameEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val description: String,
    val iconResId: Int,
    val isCustom: Boolean = false
)
