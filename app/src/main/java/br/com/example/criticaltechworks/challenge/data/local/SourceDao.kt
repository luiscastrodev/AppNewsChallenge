package br.com.example.criticaltechworks.challenge.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.example.criticaltechworks.challenge.data.entity.ArticleEntity
import br.com.example.criticaltechworks.challenge.data.entity.SourceEntity

@Dao
interface SourceDao {

    @Query("SELECT * FROM source")
    suspend fun getAll(): List<SourceEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<SourceEntity>)

    @Delete
    suspend fun deleteAll(entity: List<SourceEntity>)
}