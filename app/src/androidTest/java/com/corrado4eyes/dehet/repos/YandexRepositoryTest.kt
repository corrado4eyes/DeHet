package com.corrado4eyes.dehet.repos

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.corrado4eyes.dehet.testUtils.TestModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.get
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class YandexRepositoryTest: KoinTest {

    private lateinit var yandexRepo: YandexRepository

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        if (GlobalContext.getOrNull() == null) {
            startKoin {
                modules(TestModules.modules)
            }
        }

        yandexRepo = get()
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun getTranslation() = runBlocking {
        val result = yandexRepo.getTranslation("en-nl", "the cat")
        assertEquals("de kat", result.text.first())
    }
}