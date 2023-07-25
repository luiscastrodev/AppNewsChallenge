package br.com.example.criticaltechworks.challenge.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.example.criticaltechworks.challenge.data.entity.ArticleEntity
import br.com.example.criticaltechworks.challenge.data.entity.SourceEntity

@Dao
interface ArticleDao {

    @Query("SELECT * FROM article WHERE id == :id order by publishedAt DESC")
    suspend fun getById(id: Int): ArticleEntity

    @Query("SELECT * FROM article order by publishedAt DESC")
    suspend fun getAll(): List<ArticleEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<ArticleEntity>)

    @Delete
    suspend fun delete(entity: ArticleEntity)

    @Delete
    suspend fun deleteAll(entity: List<ArticleEntity>)
}