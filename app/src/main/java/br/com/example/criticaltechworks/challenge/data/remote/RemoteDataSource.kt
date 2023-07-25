package br.com.example.criticaltechworks.challenge.data.remote

import br.com.example.criticaltechworks.challenge.model.ArticleResponse
import br.com.example.criticaltechworks.challenge.model.Result
import br.com.example.criticaltechworks.challenge.model.SourceResponse
import br.com.example.criticaltechworks.challenge.network.ArticleService
import br.com.example.criticaltechworks.challenge.network.SourceService
import br.com.example.criticaltechworks.challenge.util.ErrorUtils
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val retrofit: Retrofit) {

    suspend fun fetchArticles(source : String): Result<ArticleResponse> {
        val articleService = retrofit.create(ArticleService::class.java);
        return getResponse(
            request = { articleService.getTopHeadlines(source) },
            defaultErrorMessage = "Error fetching Article list")

    }

    suspend fun fetchSources(): Result<SourceResponse> {
        val sourceService = retrofit.create(SourceService::class.java);
        return getResponse(
            request = { sourceService.getSources() },
            defaultErrorMessage = "Error fetching source list")

    }

    private suspend fun <T> getResponse(request: suspend () -> Response<T>, defaultErrorMessage: String): Result<T> {
        return try {
            println("I'm working in thread ${Thread.currentThread().name}")
            val result = request.invoke()
            if (result.isSuccessful) {
                return Result.success(result.body())
            } else {
                val errorResponse = ErrorUtils.parseError(result, retrofit)
                Result.error(errorResponse?.status_message ?: defaultErrorMessage, errorResponse)
            }
        } catch (e: Throwable) {
            Result.error("Unknown Error", null)
        }
    }
}