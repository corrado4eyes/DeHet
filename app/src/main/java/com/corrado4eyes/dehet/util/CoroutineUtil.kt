package com.corrado4eyes.dehet.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> doInBackground(block: suspend CoroutineScope.() -> T): T =
    withContext(Dispatchers.Default) {
        return@withContext block()
    }

