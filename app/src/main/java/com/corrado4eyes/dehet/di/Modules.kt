package com.corrado4eyes.dehet.di

import org.koin.core.module.Module

class Modules {
    companion object {
        val modules = listOf(YandexModule.yandexModule,
            DispatcherProviderModule.module)
    }
}