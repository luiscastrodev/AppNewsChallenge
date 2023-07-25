package br.com.example.criticaltechworks.challenge.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Article")
data class ArticleEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    val author: String?,

    val content: String?,

    val description: String?,

    val publishedAt: String?,

    val source: SourceEntity?,

    val title: String?,

    val url: String?,

    val urlToImage: String?
)