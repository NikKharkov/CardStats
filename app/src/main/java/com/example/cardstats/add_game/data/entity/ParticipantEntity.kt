package com.example.cardstats.add_game.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "participants",
    foreignKeys = [
        ForeignKey(
            entity = GameSessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["gameSessionId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ParticipantEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val gameSessionId: Int,
    val name: String,
    val dollars: Int = 0,
    val points: Int = 0,
    val isWin: Boolean = false,
    val isLose: Boolean = false
)
