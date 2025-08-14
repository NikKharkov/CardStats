package com.example.cardstats.add_game.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.cardstats.add_card_game.CardGameEntity

@Entity(
    tableName = "game_sessions",
    foreignKeys = [
        ForeignKey(
            entity = CardGameEntity::class,
            parentColumns = ["id"],
            childColumns = ["cardGameId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class GameSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val cardGameId: Int,
    val date: String,
    val note: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
