package com.corrado4eyes.dehet.testUtils

import com.corrado4eyes.dehet.di.YandexModule
import com.corrado4eyes.dehet.util.DispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.module.Module
import org.koin.dsl.module

@ExperimentalCoroutinesApi
object DispatcherProviderTestModule {
    val coroutineTestRule = CoroutineTestRule()
    val module = module {
        single { coroutineTestRule.testDispatcherProvider }
    }
}

@ExperimentalCoroutinesApi
object TestModules {

    val modules = listOf<Module>(YandexModule.yandexModule,
        DispatcherProviderTestModule.module)
}