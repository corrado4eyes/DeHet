package com.corrado4eyes.dehet.repos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.corrado4eyes.dehet.models.HistoryEntry
import com.corrado4eyes.dehet.testUtils.TestModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class DatabaseRepositoryTest: KoinTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var databaseRepo: DatabaseRepository

    private val entry = HistoryEntry("De", "test")

    private val entries = listOf(entry,
        HistoryEntry("Het", "testCase", isFavourite = true))


    @Before
    fun before() {
        if (GlobalContext.getOrNull() == null) {
            startKoin {
                modules(TestModules.modules)
            }
        }
        databaseRepo = get()
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun upsert() {
        databaseRepo.upsert(entry)

        val result = databaseRepo.getAll()
        assertEquals(1, result.size)
        assertEquals(entry, result[0])
    }

    @Test
    fun getAll() {
        insertEntries()

        val result = databaseRepo.getAll()
        assertEquals(2, result.size)
        assertEquals(result, entries)
    }

    @Test
    fun filter_notFavourite() {
        insertEntries()

        val result = databaseRepo.filter(isFavourite = false)
        val favoriteEntry = entries[0]
        assertEquals(result.first(), favoriteEntry)
    }

    @Test
    fun filter_favourite() {
        insertEntries()

        val result = databaseRepo.filter(isFavourite = true)
        val favoriteEntry = entries[1]
        assertEquals(result.first(), favoriteEntry)
    }

    @Test
    fun count() {
        insertEntries()

        val result = databaseRepo.count()
        assertEquals(2, result)
    }

    fun insertEntries() {
        entries.map {
            databaseRepo.upsert(it)
        }
    }

}