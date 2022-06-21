package com.example.newslook.news.di

import com.example.newslook.news.api.NewsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NewsServiceModule {

    @Singleton
    @Provides
    fun provideNewsService(retrofit: Retrofit): NewsService = retrofit.create(NewsService::class.java)
}