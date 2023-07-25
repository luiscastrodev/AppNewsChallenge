package br.com.example.criticaltechworks.challenge.data.repository

import br.com.example.criticaltechworks.challenge.data.ArticleRepository
import br.com.example.criticaltechworks.challenge.data.entity.ArticleEntity
import br.com.example.criticaltechworks.challenge.data.entity.SourceEntity
import br.com.example.criticaltechworks.challenge.data.local.ArticleDao
import br.com.example.criticaltechworks.challenge.data.remote.RemoteDataSource
import br.com.example.criticaltechworks.challenge.model.ArticleResponse
import br.com.example.criticaltechworks.challenge.model.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class ArticleRepositoryTest {

    val fakeList= mutableListOf<ArticleEntity>().apply {
        add(ArticleEntity(
            id = 1,
            author = "one",
            content  = "",
            description = "The band has cancelled upcoming concerts in Indonesia - where homosexuality is shunned - and Taiwan.",
            publishedAt = "",
            source = SourceEntity(0, "bbc-news","BBC News"),
            title = "",
            url = "",
            urlToImage = "",
        ))
    }
    @Mock
    lateinit var remoteDataSource: RemoteDataSource

    @Mock
    lateinit var articleDao: ArticleDao

    lateinit var articleRepository: ArticleRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        articleRepository = ArticleRepository(remoteDataSource, articleDao)
    }

    @Test
    fun `get list of articles should return Success`() = runBlocking {
        val source = "bbc-news"
        val fakeArticleList = fakeList.filter { it.source?.id == source }

        // Mock the remoteDataSource.fetchArticles() method
        `when`(remoteDataSource.fetchArticles(source)).thenReturn(
            Result.success(ArticleResponse(articles = fakeArticleList))
        )

        // Mock the articleDao.getAll() method
        `when`(articleDao.getAll()).thenReturn(fakeArticleList)

        // Call the fetchArticles method and collect the results
        val resultFlow: Flow<Result<List<ArticleEntity>>?> = articleRepository.fetchArticles(source)
        val resultList = mutableListOf<Result<List<ArticleEntity>?>>()
        resultFlow.collect { resultList.add(it!!) }

        // Assert the results
        assertEquals(Result.loading<List<ArticleEntity>?>(null), resultList[0])
        assertEquals(Result.success(fakeArticleList), resultList[1])
    }

    @Test
    fun `get article by id should return Success`() = runBlocking {
        val id = 1
        val fakeArticle = fakeList

        // Mock the articleDao.getById() method
        `when`(articleDao.getById(id)).thenReturn(fakeArticle[0])

        // Call the fetchArticle method and collect the results
        val resultFlow: Flow<Result<ArticleEntity>> = articleRepository.fetchArticle(id)
        val resultList = mutableListOf<Result<ArticleEntity>>()
        resultFlow.collect { resultList.add(it) }

        // Assert the results
        assertEquals(Result.loading<ArticleEntity>(null), resultList[0])
        assertEquals(Result.success(fakeArticle[0]), resultList[1])
    }
}