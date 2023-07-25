package br.com.example.criticaltechworks.challenge.network

import br.com.example.criticaltechworks.challenge.model.ArticleResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticleService {

    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("sources") country: String): Response<ArticleResponse>
}