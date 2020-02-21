package com.corrado4eyes.dehet.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject

class CoroutineUtil(private val dispatcher: DispatcherProvider): KoinComponent {


    suspend fun <T> doInBackground(block: suspend CoroutineScope.() -> T): T =
        withContext(dispatcher.default()) {
            return@withContext block()
        }
}

