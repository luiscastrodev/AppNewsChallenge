package br.com.example.criticaltechworks.challenge.data.local.remote

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.example.criticaltechworks.challenge.data.Converter
import br.com.example.criticaltechworks.challenge.data.entity.ArticleEntity
import br.com.example.criticaltechworks.challenge.data.entity.SourceEntity
import br.com.example.criticaltechworks.challenge.data.local.ArticleDao
import br.com.example.criticaltechworks.challenge.data.local.SourceDao

@Database(entities = [ArticleEntity::class, SourceEntity::class], version = 1)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
    abstract fun sourceDao(): SourceDao

}