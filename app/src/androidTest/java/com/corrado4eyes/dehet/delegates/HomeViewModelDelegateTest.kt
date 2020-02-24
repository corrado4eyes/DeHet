package com.corrado4eyes.dehet.delegates

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.corrado4eyes.dehet.models.HistoryEntry
import com.corrado4eyes.dehet.repos.YandexRepository
import com.corrado4eyes.dehet.testUtils.CoroutineTestRule
import com.corrado4eyes.dehet.testUtils.TestModules
import com.corrado4eyes.dehet.util.CoroutineUtil
import com.corrado4eyes.dehet.util.DispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.get
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class HomeViewModelDelegateTest: KoinTest {

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var viewModelDelegate: ViewModelDelegate

    @Before
    fun before() {
        if (GlobalContext.getOrNull() == null) {
            startKoin {
                modules(TestModules.modules)
            }
        }
        val yandexRepo = get<YandexRepository>()
        val dispatcherProvider = get<DispatcherProvider>()
        val coroutineUtil = CoroutineUtil(dispatcherProvider)
        viewModelDelegate = ViewModelDelegate(yandexRepo, coroutineUtil)
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun checkTextStructure_caseFullText() {
        val text = "Pass the last word"
        val expected = "word"
        val result = viewModelDelegate.checkTextStructure(text)
        assertEquals(expected, result)
    }

    @Test
    fun checkTextStructure_caseSingleWord() {
        val text = "singleWordCase"
        val expected = "singleWordCase"
        val result = viewModelDelegate.checkTextStructure(text)
        assertEquals(expected, result)
    }

    @Test
    fun getArticle_sameStringExpected() = runBlocking {
        val text = "dog"
        val result = viewModelDelegate.getArticle(text)
        val expected = HistoryEntry("De", "hond").toString()
        assertEquals(expected, result.toString())
    }

    @Test
    fun addArticleToHistoryList() {
        val entry = HistoryEntry("De", "entry")
        val list = mutableListOf(HistoryEntry("Het", "element"))
        val result = viewModelDelegate.addArticle(entry, list)
        val expectedSize = 2
        assertEquals(expectedSize, result.size)
        assertEquals(entry, result[1])
    }

    @Test
    fun updateHistoryList() {
        val newEntry = HistoryEntry("De", "entry")
        val oldEntry = HistoryEntry("De", "secondElement")
        val list = mutableListOf(
            HistoryEntry("Het", "element"),
            oldEntry)
        assertEquals(list[1], oldEntry)
        val result = viewModelDelegate.updateItemInHistory(newEntry,
            1, list)
        assertNotEquals(result[1], oldEntry)
        assertEquals(result[1], newEntry)
    }
}