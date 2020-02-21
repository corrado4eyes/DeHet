package com.corrado4eyes.dehet.di

import com.corrado4eyes.dehet.util.DefaultDispatcherProvider
import com.corrado4eyes.dehet.util.DispatcherProvider
import org.koin.dsl.module

class DispatcherProviderModule {
    companion object {
        val module = module {
            single<DispatcherProvider> { DefaultDispatcherProvider() }
        }
    }
}