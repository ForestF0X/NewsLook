package com.example.newslook.news.domain

import android.app.Application
import com.example.newslook.core.ui.ViewState
import com.example.newslook.core.utils.httpError
import com.example.newslook.news.NewsMapper
import com.example.newslook.news.api.NewsResponse
import com.example.newslook.news.api.NewsService
import com.example.newslook.news.storage.DataPreference
import com.example.newslook.news.storage.NewsArticlesDao
import com.example.newslook.news.storage.entity.NewsArticleDb
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

interface NewsRepository {

    fun getNewsArticles(): Flow<ViewState<List<NewsArticleDb>>>

    suspend fun getNewsFromWebservice(): Response<NewsResponse>
}

@Singleton
class DefaultNewsRepository @Inject constructor(
    private val newsDao: NewsArticlesDao,
    private val newsService: NewsService,
    application: Application
) : NewsRepository, NewsMapper {
    private val categoryDataStore = DataPreference(application)
    override fun getNewsArticles(): Flow<ViewState<List<NewsArticleDb>>> = flow {
        // 1. Start with loading
        emit(ViewState.loading())

        // 2. Try to fetch fresh news from web + cache if any
        val freshNews = getNewsFromWebservice()
        freshNews.body()?.articles?.toStorage()?.let(newsDao::clearAndCacheArticles)

        // 3. Get news from cache
        val cachedNews = newsDao.getNewsArticles()
        emitAll(cachedNews.map { ViewState.success(it) })
    }
        .flowOn(Dispatchers.IO)

    override suspend fun getNewsFromWebservice(): Response<NewsResponse> {
        return try {
            newsService.getTopHeadlines(categoryDataStore.category.toString())
        } catch (e: Exception) {
            httpError(404)
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
interface NewsRepositoryModule {
    /* Exposes the concrete implementation for the interface */
    @Binds
    fun it(it: DefaultNewsRepository): NewsRepository
}