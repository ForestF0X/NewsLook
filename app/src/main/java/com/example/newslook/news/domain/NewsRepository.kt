package com.example.newslook.news.domain

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.newslook.BuildConfig
import com.example.newslook.core.ui.ViewState
import com.example.newslook.core.utils.httpError
import com.example.newslook.news.NewsMapper
import com.example.newslook.news.api.NewsResponse
import com.example.newslook.news.api.NewsService
import com.example.newslook.news.storage.NewsArticlesDao
import com.example.newslook.news.storage.entity.NewsArticleDb
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

interface NewsRepository {

    fun getNewsArticles(category: String): Flow<ViewState<List<NewsArticleDb>>>

    suspend fun getNewsFromWebservice(category: String): Response<NewsResponse>

}

const val DataStore_NAME = "CATEGORY"

val Context.datastore : DataStore<Preferences> by  preferencesDataStore(name = DataStore_NAME)

@Singleton
class DefaultNewsRepository @Inject constructor(
    private val newsDao: NewsArticlesDao,
    private val newsService: NewsService,
    private val application: Application
) : NewsRepository, NewsMapper {


    override fun getNewsArticles(category: String): Flow<ViewState<List<NewsArticleDb>>> = flow {
        // 1. Start with loading
        emit(ViewState.loading())

        // 2. Try to fetch fresh news from web + cache if any
        val freshNews = getNewsFromWebservice(category)
        freshNews.body()?.articles?.toStorage()?.let(newsDao::clearAndCacheArticles)

        // 3. Get news from cache
        val cachedNews = newsDao.getNewsArticles()
        emitAll(cachedNews.map { ViewState.success(it.sortedByDescending { it.publishedAt }) })
    }
        .flowOn(Dispatchers.IO)

    override suspend fun getNewsFromWebservice(category: String): Response<NewsResponse> {
        return try {
            newsService.getTopHeadlines(category, BuildConfig.NEWS_API_KEY)
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