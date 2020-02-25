package com.corrado4eyes.dehet.testUtils

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.corrado4eyes.dehet.data.local.databases.HistoryDatabase
import com.corrado4eyes.dehet.di.YandexModule
import com.corrado4eyes.dehet.repos.DatabaseRepository
import com.corrado4eyes.dehet.repos.DatabaseRepositoryImpl
import com.corrado4eyes.dehet.util.DispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.dsl.module


object DatabaseTestModule {
    val module = module {
        single { Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
            HistoryDatabase::class.java).allowMainThreadQueries().build() }
        single { get<HistoryDatabase>().historyDao() }
        single { DatabaseRepositoryImpl(get()) as DatabaseRepository }
    }
}

@ExperimentalCoroutinesApi
object DispatcherProviderTestModule {
    val coroutineTestRule = CoroutineTestRule()
    val module = module {
        single { coroutineTestRule.testDispatcherProvider }
    }
}

@ExperimentalCoroutinesApi
object TestModules {

    val modules = listOf(YandexModule.yandexModule,
        DispatcherProviderTestModule.module,
        DatabaseTestModule.module)
}