package com.example.cardstats.di

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.cardstats.R
import com.example.cardstats.add_card_game.repository.CardGamesRepository
import com.example.cardstats.add_game.data.repository.GameSessionRepository
import com.example.cardstats.add_game.presentation.AddGameViewModel
import com.example.cardstats.database.AppDatabase
import com.example.cardstats.main_screens.calendar.CalendarViewModel
import com.example.cardstats.main_screens.list_of_games.CardGamesViewModel
import com.example.cardstats.main_screens.tournament.data.repository.OtherTournamentParticipantRepository
import com.example.cardstats.main_screens.tournament.data.repository.TournamentParticipantRepository
import com.example.cardstats.main_screens.tournament.data.repository.TournamentRepository
import com.example.cardstats.main_screens.tournament.presentation.TournamentViewModel
import com.example.cardstats.notes.data.NoteRepository
import com.example.cardstats.notes.presentation.NoteViewModel
import com.example.cardstats.schedule_game.ScheduleGameRepository
import com.example.cardstats.schedule_game.ScheduleViewModel
import com.example.cardstats.settings.SettingsRepository
import com.example.cardstats.user.UserPreferences
import com.example.cardstats.user.UserViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module{

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java
            ,"app_database"
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                db.execSQL(
                    """
                    INSERT INTO card_games (id, name, description,iconResId,isCustom) VALUES 
                    (1, 'POKER', 'Players get 2 cards, 5 community cards are placed on the table. The goal is to make the best hand. Betting and bluffing are key.',${R.drawable.poker_cards},0),
                    (2,'Blackjack','Players try to reach 21 points without exceeding. Aces count as 1 or 11, face cards as 10. Played against the dealer.',${R.drawable.blackjack_cards},0),
                    (3,'Bridge','A team game (2 vs 2), aiming to win the most tricks after bidding on contracts.',${R.drawable.bridge_cards},0),
                    (4,'Rummy','Form sets (same rank) or sequences (same suit). The first to discard all cards wins.',${R.drawable.rummy_cards},0),
                    (5,'Thousand',' A trick-taking game with bidding, aiming to reach 1000 points.',${R.drawable.thousand_cards},0),
                    (6,'Durak','Players defend against attacks by matching suits. The last player with cards loses ("Durak").',${R.drawable.durak_cards},0),
                    (7,'UNO','Players discard matching numbers or colors while using special cards to challenge opponents.',${R.drawable.uno_cards},0),
                    (8,'Preference','A trick-taking game with bidding and contracts, where players commit to winning a certain number of tricks.',${R.drawable.preference_cards},0)
                    """
                )
            }
        }).build()
    }

    single {
        get<AppDatabase>().cardGameDao()
    }

    single {
        CardGamesRepository(get())
    }

    viewModel {
        CardGamesViewModel(get())
    }

    single {
        get<AppDatabase>().cardGameDao()
    }

    single {
        CardGamesRepository(get())
    }

    single {
        get<AppDatabase>().gameSessionDao()
    }

    single {
        GameSessionRepository(get())
    }
    viewModel {
        AddGameViewModel(get(), get())
    }

    single {
        UserPreferences(androidContext())
    }

    viewModel {
        UserViewModel(get(),get(),get())
    }

    viewModel {
        CalendarViewModel(get())
    }

    single {
        get<AppDatabase>().scheduledGameDao()
    }

    single {
        ScheduleGameRepository(get(),get())
    }

    viewModel {
        ScheduleViewModel(get())
    }

    single {
        get<AppDatabase>().noteDao()
    }

    single {
        NoteRepository(get())
    }

    single {
        NoteViewModel(get())
    }

    single {
        get<AppDatabase>().tournamentDao()
    }

    single {
        get<AppDatabase>().tournamentParticipantDao()
    }

    single {
        get<AppDatabase>().otherTournamentParticipantDao()
    }

    single {
        TournamentRepository(get())
    }

    single {
        TournamentParticipantRepository(get())
    }

    single {
        OtherTournamentParticipantRepository(get())
    }

    viewModel {
        TournamentViewModel(get(),get(),get())
    }

    single {
        SettingsRepository(androidContext())
    }
}