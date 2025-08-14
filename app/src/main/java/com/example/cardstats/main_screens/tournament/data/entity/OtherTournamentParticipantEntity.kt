package com.example.cardstats.main_screens.tournament.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "other_tournament_participants",
    foreignKeys = [
        ForeignKey(
            entity = TournamentParticipantEntity::class,
            parentColumns = ["id"],
            childColumns = ["participant_id"],
            onDelete = CASCADE
        )
    ]
)
data class OtherTournamentParticipantEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "participant_id") val participantId: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "wins") val wins: Int,
    @ColumnInfo(name = "points") val points: Int
)