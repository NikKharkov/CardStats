package com.example.cardstats.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class StatsApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@StatsApplication)
            modules(appModule)
        }
    }
}