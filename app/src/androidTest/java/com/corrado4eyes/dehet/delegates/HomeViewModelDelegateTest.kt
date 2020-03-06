package com.corrado4eyes.dehet.delegates

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.corrado4eyes.dehet.models.HistoryEntry
import com.corrado4eyes.dehet.models.YandexResponse
import com.corrado4eyes.dehet.repos.DatabaseRepository
import com.corrado4eyes.dehet.repos.YandexRepository
import com.corrado4eyes.dehet.testUtils.CoroutineTestRule
import com.corrado4eyes.dehet.testUtils.TestModules
import com.corrado4eyes.dehet.util.CoroutineUtil
import com.corrado4eyes.dehet.util.DispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class HomeViewModelDelegateTest: KoinTest {

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    lateinit var viewModelDelegate: ViewModelDelegate

    @Mock
    lateinit var yandexRepo: YandexRepository

    @Mock
    lateinit var databaseRepo: DatabaseRepository


    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        if (GlobalContext.getOrNull() == null) {
            startKoin {
                modules(TestModules.modules)
            }
        }

        val dispatcherProvider = get<DispatcherProvider>()
        val coroutineUtil = CoroutineUtil(dispatcherProvider)
        viewModelDelegate = ViewModelDelegate(yandexRepo, databaseRepo, coroutineUtil)
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun checkTextStructure_caseFullText() {
        val text = "sleeping test"
        val expected = "sleeping test"
        val result = viewModelDelegate.checkTextStructure(text)
        assertEquals(expected, result)
    }

    @Test
    fun checkTextStructure_caseWithThe() {
        val text = "The sleeping test"
        val expected = "sleeping test"
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
    fun getArticle_sameStringExpected() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val text = "dog"
        val expected = HistoryEntry("De", "hond")
        val response = YandexResponse(code = 123, lang = "en-nl", text = listOf("De hond"))
        Mockito.`when`(yandexRepo.getTranslation(anyString(), anyString())).thenReturn(response)

        val result = viewModelDelegate.getArticle(text)
        assertEquals(expected.toString(), result.toString())
    }

    @Test
    fun getHistory() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val entry = HistoryEntry("De", "test")
        val list = listOf(entry)
        Mockito.`when`(databaseRepo.getAll()).thenReturn(list)

        val result = viewModelDelegate.getHistory()
        assertEquals(result, list)
    }
}