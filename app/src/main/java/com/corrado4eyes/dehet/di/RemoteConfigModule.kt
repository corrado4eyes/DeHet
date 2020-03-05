package com.corrado4eyes.dehet.di

import com.corrado4eyes.dehet.data.network.RemoteConfig
import com.corrado4eyes.dehet.data.network.RemoteConfigImpl
import org.koin.dsl.module

class RemoteConfigModule {

    companion object {
        val module = module {
            single<RemoteConfig> { RemoteConfigImpl() }
        }
    }

}