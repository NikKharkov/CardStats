package com.example.cardstats.main_screens.tournament.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "tournament_participants",
    foreignKeys = [
        ForeignKey(
            entity = TournamentEntity::class,
            parentColumns = ["id"],
            childColumns = ["tournament_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TournamentParticipantEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "tournament_id") val tournamentId: Int,
    @ColumnInfo(name = "player_name") val playerName: String = "Me",
    @ColumnInfo(name = "points") val points: Int = 0,
    @ColumnInfo(name = "wins") val wins: Int = 0
)