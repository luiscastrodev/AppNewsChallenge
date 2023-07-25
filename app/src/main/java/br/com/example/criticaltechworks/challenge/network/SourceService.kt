package br.com.example.criticaltechworks.challenge.network

import br.com.example.criticaltechworks.challenge.data.entity.SourceEntity
import br.com.example.criticaltechworks.challenge.model.ArticleResponse
import br.com.example.criticaltechworks.challenge.model.SourceResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SourceService {

    @GET("v2/top-headlines/sources")
    suspend fun getSources(): Response<SourceResponse>
}