package com.example.newslook.news.di

import android.app.Application
import com.example.newslook.news.storage.NewsArticlesDao
import com.example.newslook.news.storage.NewsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NewsDatabaseModule {

    @Singleton
    @Provides
    fun provideDb(app: Application): NewsDatabase = NewsDatabase.buildDefault(app)

    @Singleton
    @Provides
    fun provideUserDao(db: NewsDatabase): NewsArticlesDao = db.newsArticlesDao()
}