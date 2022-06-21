package com.example.newslook.news.api

import com.example.newslook.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    @GET("top-headlines?apiKey=${BuildConfig.NEWS_API_KEY}&category=technology")
    suspend fun getTopHeadlines(@Query("category") category: String): Response<NewsResponse>

}
