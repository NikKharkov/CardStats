package com.example.cardstats.schedule_game

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.cardstats.add_card_game.CardGameEntity

@Entity(
    tableName = "scheduled_games",
    foreignKeys = [
        ForeignKey(
            entity = CardGameEntity::class,
            parentColumns = ["id"],
            childColumns = ["gameId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ScheduledGameEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val gameId: Int,
    val startTime: String,
    val endTime: String
)