package com.example.newslook.news.ui.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.newslook.NewsApp
import com.example.newslook.core.ui.ViewState
import com.example.newslook.news.domain.NewsRepository
import com.example.newslook.news.storage.DataPreference
import com.example.newslook.news.storage.entity.NewsArticleDb
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsArticleViewModel @Inject constructor(
    application: Application,
    newsRepository: NewsRepository
) : AndroidViewModel(application) {

    private val categoryDataStore = DataPreference(application)

    fun saveToDataStore(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            categoryDataStore.saveToDataStore(category)
        }
    }

    private val newsArticleDb: LiveData<ViewState<List<NewsArticleDb>>> = newsRepository.getNewsArticles().asLiveData()

    fun getNewsArticles(): LiveData<ViewState<List<NewsArticleDb>>> = newsArticleDb
}