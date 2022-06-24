package com.example.newslook.news.ui.viewmodel

import android.app.Application
import android.util.Log
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

    private val listThemes: MutableList<String> = mutableListOf()

    fun prePopulateList(){
        listThemes.add("Technology")
        listThemes.add("Apple")
        listThemes.add("Android")
    }

    fun getListThemes(): MutableList<String>{
        return listThemes
    }

    fun addListThemes(category: String){
        listThemes.add(category)
    }

    fun removeListThemes(category: String){
        listThemes.remove(category)
    }

    fun editListTheme(editTarget: String, editText: String){
        var key = 0
        var tempKey = 0
        listThemes.forEach {
        key += 1
            if (it == editTarget) {
                tempKey = key - 1
            }
        }
        listThemes[tempKey] = editText
    }

    fun saveData(category: String){
        viewModelScope.launch(Dispatchers.IO) {
            mCategory = category
        }
    }

    fun getNewsArticles(): LiveData<ViewState<List<NewsArticleDb>>> = newsRepository.getNewsArticles(mCategory).asLiveData()
}