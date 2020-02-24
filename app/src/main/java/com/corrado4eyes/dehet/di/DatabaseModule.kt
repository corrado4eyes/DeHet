package com.corrado4eyes.dehet.di

import com.corrado4eyes.dehet.data.local.databases.HistoryDatabase
import com.corrado4eyes.dehet.repos.DatabaseRepository
import com.corrado4eyes.dehet.repos.DatabaseRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

class DatabaseModule {

    companion object {
        val module: Module = module {
            single { HistoryDatabase.getInstance(androidContext()) }
            single { get<HistoryDatabase>().historyDao() }
            single { DatabaseRepositoryImpl(get()) as DatabaseRepository  }
        }
    }

}