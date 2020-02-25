package com.corrado4eyes.dehet.ui.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.corrado4eyes.dehet.models.HistoryEntry
import com.corrado4eyes.dehet.testUtils.CoroutineTestRule
import com.corrado4eyes.dehet.testUtils.TestModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class HomeViewModelTest: KoinTest {

    private lateinit var viewModel: HomeViewModel

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun before() {
        if (GlobalContext.getOrNull() == null) {
            startKoin {
                modules(TestModules.modules)
            }
        }
        viewModel = HomeViewModel()
    }

    @After
    fun stopKoinAfterTest() {
        stopKoin()
    }

    @Test
    fun shouldMatchInitialValues() {
        assert(viewModel.editTextValue.value == "")
        assert(viewModel.resultHistoryEntry.value == HistoryEntry())
        assert(viewModel.historyList.value == emptyList<HistoryEntry>())
    }


    // This test is currently using runBlocking as a work around, since it should use
    // coroutinesTestRule.testDispatcher.runBlockingTest, but runBlockingTest fails

    @Test
    fun onSearchButtonClicked() = runBlocking {
        val word = "ball"
        val expectedOutput = HistoryEntry("de", "bal")
        val result = viewModel.onSearchButtonClicked(word)
        assertEquals(expectedOutput.article, result.article)
        assertEquals(expectedOutput.word, result.word)
    }

    @Test
    fun onAddResultClicked() = runBlocking {
        val newEntry = HistoryEntry("de", "test")
        viewModel.onAddResultClicked(newEntry)
        val result = viewModel.syncUiWithDb()
        assertEquals(result.size, 1)
        assertEquals(result[0], newEntry)
    }

    @Test
    fun onFavouriteButtonClicked() = runBlocking {
        viewModel.historyList.value = listOf(
            HistoryEntry("De", "test"),
            HistoryEntry("Het", "case")
        )

        val list = viewModel.historyList.value
        list?.get(0)?.isFavourite?.let { assertFalse(it) }
        viewModel.onFavouriteButtonClicked(0)
        val result = viewModel.syncUiWithDb()
        assertTrue(result[0].isFavourite)
    }
}