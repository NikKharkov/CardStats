package com.example.cardstats.main_screens.tournament.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.cardstats.add_card_game.CardGameEntity

@Entity(
    tableName = "tournaments",
    foreignKeys = [
        ForeignKey(
            entity = CardGameEntity::class,
            parentColumns = ["id"],
            childColumns = ["card_game_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TournamentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "tournament_name") val tournamentName: String,
    @ColumnInfo(name = "card_game_id") val cardGameId: Int,
    @ColumnInfo(name = "start_date") val startDate: String,
    @ColumnInfo(name = "end_date") val endDate: String,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis()
)