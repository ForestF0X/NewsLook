package com.example.newslook.news.api

import com.example.newslook.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    @GET("everything")
    suspend fun getTopHeadlines(@Query("q") category: String, @Query("apiKey") apiKey: String): Response<NewsResponse>

}
