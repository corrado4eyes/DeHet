package com.corrado4eyes.dehet.di

import com.corrado4eyes.dehet.data.network.YandexApiFactory
import com.corrado4eyes.dehet.repos.YandexRepository
import com.corrado4eyes.dehet.repos.YandexRepositoryImpl
import org.koin.dsl.module

class YandexModule {
    companion object {
        val yandexModule = module {
            single {YandexApiFactory.create()}
            single<YandexRepository> { YandexRepositoryImpl(get()) }
        }
    }
}