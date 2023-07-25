package br.com.example.criticaltechworks.challenge.model

import br.com.example.criticaltechworks.challenge.data.entity.ArticleEntity

data class ArticleResponse(
    val articles: List<ArticleEntity>,
    val status: String = "",
    val totalResults: Int = 0
)

