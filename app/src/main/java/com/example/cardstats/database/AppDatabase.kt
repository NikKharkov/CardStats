package com.example.cardstats.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cardstats.add_card_game.CardGameDao
import com.example.cardstats.add_card_game.CardGameEntity
import com.example.cardstats.add_game.data.dao.GameSessionDao
import com.example.cardstats.add_game.data.entity.GameSessionEntity
import com.example.cardstats.add_game.data.entity.ParticipantEntity
import com.example.cardstats.main_screens.tournament.data.dao.OtherTournamentParticipantDao
import com.example.cardstats.main_screens.tournament.data.dao.TournamentDao
import com.example.cardstats.main_screens.tournament.data.dao.TournamentParticipantDao
import com.example.cardstats.main_screens.tournament.data.entity.OtherTournamentParticipantEntity
import com.example.cardstats.main_screens.tournament.data.entity.TournamentEntity
import com.example.cardstats.main_screens.tournament.data.entity.TournamentParticipantEntity
import com.example.cardstats.notes.data.NoteDao
import com.example.cardstats.notes.data.NoteEntity
import com.example.cardstats.schedule_game.ScheduledGameDao
import com.example.cardstats.schedule_game.ScheduledGameEntity

@Database(
    version = 1,
    entities = [
        CardGameEntity::class,
        GameSessionEntity::class,
        ParticipantEntity::class,
        ScheduledGameEntity::class,
        NoteEntity::class,
        TournamentEntity::class,
        TournamentParticipantEntity::class,
        OtherTournamentParticipantEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cardGameDao(): CardGameDao
    abstract fun gameSessionDao(): GameSessionDao
    abstract fun scheduledGameDao(): ScheduledGameDao
    abstract fun noteDao(): NoteDao
    abstract fun tournamentDao(): TournamentDao
    abstract fun tournamentParticipantDao(): TournamentParticipantDao
    abstract fun otherTournamentParticipantDao(): OtherTournamentParticipantDao

}