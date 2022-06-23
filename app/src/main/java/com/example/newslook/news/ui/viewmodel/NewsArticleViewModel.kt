package com.example.newslook.news.ui.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.newslook.core.ui.ViewState
import com.example.newslook.news.domain.NewsRepository
import com.example.newslook.news.storage.entity.NewsArticleDb
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsArticleViewModel @Inject constructor(
    application: Application,
    private val newsRepository: NewsRepository
) : AndroidViewModel(application) {

    private var mCategory: String = ""

    fun saveData(category: String){
        viewModelScope.launch(Dispatchers.IO) {
            mCategory = category
        }
    }

    fun getNewsArticles(): LiveData<ViewState<List<NewsArticleDb>>> = newsRepository.getNewsArticles(mCategory).asLiveData()
}