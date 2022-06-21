package com.example.newslook.news.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.newslook.news.storage.entity.NewsArticleDb
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsArticlesDao {

    @Insert
    fun insertArticles(articles: List<NewsArticleDb>): List<Long>

    @Query("DELETE FROM news_article")
    fun clearAllArticles()

    @Transaction
    fun clearAndCacheArticles(articles: List<NewsArticleDb>) {
        clearAllArticles()
        insertArticles(articles)
    }

    @Query("SELECT * FROM news_article")
    fun getNewsArticles(): Flow<List<NewsArticleDb>>
}