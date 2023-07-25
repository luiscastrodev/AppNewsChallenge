package br.com.example.criticaltechworks.challenge.data

import br.com.example.criticaltechworks.challenge.data.entity.ArticleEntity
import br.com.example.criticaltechworks.challenge.data.local.ArticleDao
import br.com.example.criticaltechworks.challenge.data.remote.RemoteDataSource
import br.com.example.criticaltechworks.challenge.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val articleDao: ArticleDao
) {
    suspend fun fetchArticles(source : String): Flow<Result<List<ArticleEntity>>?> {
        return flow {
            val cachedData = fetchArticlesCached()
            emit(Result.loading())

            val result = remoteDataSource.fetchArticles(source)

            //Cache to database if response is successful
            if (result.status == Result.Status.SUCCESS) {
                result.data?.articles?.let { newArticles ->
                    cachedData?.data?.let { articlesCached -> articleDao.deleteAll(articlesCached) }
                    articleDao.insertAll(newArticles)
                }
            }
           emit(fetchArticlesCached())
        }.flowOn(Dispatchers.IO)
    }

     suspend fun fetchArticlesCached(): Result<List<ArticleEntity>>? =
        articleDao.getAll()?.let {article ->
            Result.success(article)
        }


    suspend fun fetchArticle(id: Int): Flow<Result<ArticleEntity>> {
        return flow {
            emit(Result.loading())

            val modelConverter = articleDao.getById(id)

            emit(Result.success(modelConverter))

        }.flowOn(Dispatchers.IO)
    }

}