package br.com.example.criticaltechworks.challenge.model

import android.os.Parcelable
import br.com.example.criticaltechworks.challenge.data.entity.SourceEntity
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Article(
    @SerializedName("author")
    val author: String?,

    @SerializedName("content")
    val content: String?,

    @SerializedName("description")
    val description: String?,

    @SerializedName("publishedAt")
    val publishedAt: String?,

    val source: Source?,

    @SerializedName("title")
    val title: String?,

    @SerializedName("url")
    val url: String?,

    @SerializedName("urlToImage")
    val urlToImage: String?
):Parcelable