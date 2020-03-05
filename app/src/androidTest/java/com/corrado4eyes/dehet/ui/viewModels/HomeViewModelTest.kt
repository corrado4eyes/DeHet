package com.corrado4eyes.dehet.ui.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.corrado4eyes.dehet.models.HistoryEntry
import com.corrado4eyes.dehet.models.YandexResponse
import com.corrado4eyes.dehet.repos.DatabaseRepository
import com.corrado4eyes.dehet.repos.YandexRepository
import com.corrado4eyes.dehet.testUtils.CoroutineTestRule
import com.corrado4eyes.dehet.testUtils.DatabaseTestModule
import com.corrado4eyes.dehet.testUtils.DispatcherProviderTestModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import org.mockito.ArgumentMatchers.*
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
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

    private lateinit var databaseRepo: DatabaseRepository

    @Mock
    private lateinit var yandexRepo: YandexRepository

    private val yandexModule = module {
        single { yandexRepo }
    }

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        if (GlobalContext.getOrNull() == null) {
            startKoin {
                modules(listOf(yandexModule,
                    DatabaseTestModule.module,
                    DispatcherProviderTestModule.module))
            }
        }
        databaseRepo = get()
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
    fun onSearchButtonClicked() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(yandexRepo.getTranslation(anyString(), anyString())).thenReturn(
            YandexResponse(123, "lang", listOf("de bal"))
        )
        val word = "ball"
        val expectedOutput = HistoryEntry("de", "bal")
        val result = viewModel.onSearchButtonClicked(word)
        assertEquals(expectedOutput.article, result.article)
        assertEquals(expectedOutput.word, result.word)
    }

    @Test
    fun onAddResultClicked() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val newEntry = HistoryEntry("de", "test")
        assertEquals(0, viewModel.syncWithLocalDb().size)
        viewModel.onAddResultClicked(newEntry)
        val result = viewModel.syncWithLocalDb()
        assertEquals(1, result.size)
        assertEquals(newEntry, result[0])
    }

    @Test
    fun onFilterSelected_favourite() = coroutinesTestRule.testDispatcher.runBlockingTest {
        populateLocalDb()
        assertEquals(2, viewModel.historyList.value?.size)

        viewModel.historyList.value = viewModel.onFilterSelected()
        assertEquals(1, viewModel.historyList.value?.size)
        assertTrue(viewModel.historyList.value?.first()!!.isFavourite)
    }

    @Test
    fun onFavouriteButtonTappedTest_toFavourite() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            populateLocalDb()
            assertEquals(2, viewModel.historyList.value?.size)

            viewModel.onFavouriteButtonTapped(viewModel.historyList.value!!.first())
            viewModel.historyList.value = viewModel.syncWithLocalDb()
            assertTrue(viewModel.historyList.value!!.first().isFavourite)
    }

    @Test
    fun onFavouriteButtonTappedTest_toNotFavourite() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            populateLocalDb()
            assertEquals(2, viewModel.historyList.value?.size)

            viewModel.onFavouriteButtonTapped(viewModel.historyList.value!!.last())
            viewModel.historyList.value = viewModel.syncWithLocalDb()
            assertFalse(viewModel.historyList.value!!.last().isFavourite)
    }

    @Test
    fun onDeleteButtonTappedTest() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            populateLocalDb()
            assertEquals(2, viewModel.historyList.value?.size)
            val tappedElementIndex = 0
            val remainingEntry = viewModel.historyList.value!!.last()
            viewModel.onDeleteButtonTapped(tappedElementIndex)
            viewModel.historyList.value = viewModel.syncWithLocalDb()
            assertEquals(1, viewModel.historyList.value?.size)
            assertEquals(remainingEntry, viewModel.historyList.value!!.first())
        }

    private suspend fun populateLocalDb() {
        listOf(
            HistoryEntry("De", "test"),
            HistoryEntry("Het", "case", true)
        ).apply {
            map {
                databaseRepo.upsert(it)
            }
        }

        viewModel.historyList.value = viewModel.syncWithLocalDb()
    }
}