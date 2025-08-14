package com.example.cardstats.notes.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.cardstats.add_card_game.CardGameEntity


@Entity(
    tableName = "notes",
    foreignKeys = [
        ForeignKey(
            entity = CardGameEntity::class,
            parentColumns = ["id"],
            childColumns = ["game_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "game_id") val gameId: Int,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "is_icon_filled") val isIconFilled: Boolean = false
)