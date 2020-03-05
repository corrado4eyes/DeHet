package com.corrado4eyes.dehet.di

class Modules {
    companion object {
        val modules = listOf(YandexModule.yandexModule,
            DispatcherProviderModule.module,
            DatabaseModule.module,
            RemoteConfigModule.module)
    }
}