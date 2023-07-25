package br.com.example.criticaltechworks.challenge.data

import br.com.example.criticaltechworks.challenge.data.entity.ArticleEntity
import br.com.example.criticaltechworks.challenge.data.entity.SourceEntity
import br.com.example.criticaltechworks.challenge.model.Article
import br.com.example.criticaltechworks.challenge.model.Source

object Transformer {

    fun convertArticleModelToArticleEntity(article: Article): ArticleEntity {
        return ArticleEntity(
            author = article.author,
            content = article.content,
            source = convertSourceModelToSourceEntity(article.source),
            description = article.description,
            publishedAt = article.publishedAt,
            url = article.url,
            urlToImage = article.urlToImage,
            title = article.title
        )
    }

    fun convertArticleEntityToArticleModel(articleEntity: ArticleEntity): Article {
        return Article(
            author = articleEntity.author,
            content = articleEntity.content,
            source = convertSourceEntityToSourceModel(articleEntity.source),
            description = articleEntity.description,
            publishedAt = articleEntity.publishedAt,
            url = articleEntity.url,
            urlToImage = articleEntity.urlToImage,
            title = articleEntity.title

        )
    }

    private fun convertSourceModelToSourceEntity(source: Source?): SourceEntity? {

        source?.let {
            return SourceEntity(0,source.id, source.name)
        }

        return null
    }

    private fun convertSourceEntityToSourceModel(sourceEntity: SourceEntity?): Source? {

        sourceEntity?.let {

            return Source(sourceEntity.id, sourceEntity.name)
        }

        return null
    }
}