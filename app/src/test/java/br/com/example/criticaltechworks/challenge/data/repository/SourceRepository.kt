package br.com.example.criticaltechworks.challenge.data.repository

import br.com.example.criticaltechworks.challenge.data.SourceRepository
import br.com.example.criticaltechworks.challenge.data.entity.SourceEntity
import br.com.example.criticaltechworks.challenge.data.local.SourceDao
import br.com.example.criticaltechworks.challenge.data.remote.RemoteDataSource
import br.com.example.criticaltechworks.challenge.model.Result
import br.com.example.criticaltechworks.challenge.model.SourceResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class SourceRepositoryTest {

    val fakeList= mutableListOf<SourceEntity>().apply {
        add(SourceEntity(
            sourceId = 1,
            "abc-news",
            "ABC News"
        ))
    }
    @Mock
    lateinit var remoteDataSource: RemoteDataSource

    @Mock
    lateinit var sourceDao: SourceDao

    lateinit var sourceRepository: SourceRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        sourceRepository = SourceRepository(remoteDataSource, sourceDao)
    }

    @Test
    fun `get list of sources should return Success`() = runBlocking {
        val fakeSourceList = fakeList

        // Mock the remoteDataSource.fetchSources() method
        `when`(remoteDataSource.fetchSources()).thenReturn(
            Result.success(SourceResponse(sources = fakeSourceList))
        )

        // Mock the sourceDao.getAll() method
        `when`(sourceDao.getAll()).thenReturn(fakeSourceList)

        // Call the fetchSources method and collect the results
        val resultFlow: Flow<Result<List<SourceEntity>>?> = sourceRepository.fetchSources()
        val resultList = mutableListOf<Result<List<SourceEntity>?>>()
        resultFlow.collect { resultList.add(it!!) }

        // Assert the results
        assertEquals(Result.loading<List<SourceEntity>?>(null), resultList[0])
        assertEquals(Result.success(fakeSourceList), resultList[1])
    }

}