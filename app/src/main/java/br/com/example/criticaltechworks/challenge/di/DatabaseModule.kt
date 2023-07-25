package br.com.example.criticaltechworks.challenge.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import br.com.example.criticaltechworks.challenge.data.local.ArticleDao
import br.com.example.criticaltechworks.challenge.data.local.SourceDao
import br.com.example.criticaltechworks.challenge.data.local.remote.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "app.db"
        ).build()
    }

    @Provides
    fun provideArticleDao(appDatabase: AppDatabase): ArticleDao {
        return appDatabase.articleDao()
    }

    @Provides
    fun provideSourceDao(appDatabase: AppDatabase): SourceDao {
        return appDatabase.sourceDao()
    }
    @Module
    @InstallIn(SingletonComponent::class)
    class AppModule {
        @Singleton
        @Provides
        fun provideContext(application: Application): Context = application.applicationContext
    }
}