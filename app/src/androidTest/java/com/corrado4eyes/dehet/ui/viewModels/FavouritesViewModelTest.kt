package com.corrado4eyes.dehet.ui.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.corrado4eyes.dehet.models.HistoryEntry
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
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class FavouritesViewModelTest: KoinTest {

    private lateinit var viewModel: FavouritesViewModel

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
        viewModel = FavouritesViewModel()
    }

    @After
    fun stopKoinAfterTest() {
        stopKoin()
    }

    @Test
    fun shouldMatchInitialValues() {
        assert(viewModel.favouritesList.value == emptyList<HistoryEntry>())
    }

    @Test
    fun onFavouriteButtonClicked() = coroutinesTestRule.testDispatcher.runBlockingTest {
        viewModel.favouritesList.value = listOf(
            HistoryEntry("De", "test"),
            HistoryEntry("Het", "case")
        ).apply {
            map {
                databaseRepo.upsert(it)
            }
        }

        viewModel.favouritesList.value?.map { assertFalse(it.isFavourite) }

        val list = viewModel.favouritesList.value!!
        viewModel.onFavouriteButtonTapped(list[1])
        val result = viewModel.syncWithLocalDb()
        assertTrue(result[0].isFavourite)
    }

    @Test
    fun onDeleteButtonTapped() = coroutinesTestRule.testDispatcher.runBlockingTest {
        viewModel.favouritesList.value = listOf(
            HistoryEntry("De", "test")
        ).apply {
            map {
                databaseRepo.upsert(it)
            }
        }

        assertEquals(1, viewModel.favouritesList.value!!.size)
        viewModel.onDeleteButtonTapped(viewModel.favouritesList.value!!.first())
        viewModel.favouritesList.value = viewModel.syncWithLocalDb()
        assertEquals(0, viewModel.favouritesList.value!!.size)
    }
}