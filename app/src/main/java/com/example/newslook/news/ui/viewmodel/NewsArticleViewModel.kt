package com.example.newslook.news.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.newslook.core.ui.ViewState
import com.example.newslook.news.domain.NewsRepository
import com.example.newslook.news.storage.CategoryItem
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

    private var mUrl: String = ""

    private val listThemes: MutableList<CategoryItem> = mutableListOf()

    fun prePopulateList(){
        listThemes.add(CategoryItem("Technology"))
        listThemes.add(CategoryItem("Apple"))
        listThemes.add(CategoryItem("Android"))
    }

    fun saveUrl(url: String){
        viewModelScope.launch(Dispatchers.IO) {
            mUrl = url
        }
    }

    fun getUrl(): String{
        return mUrl
    }

    fun getListThemes(): MutableList<CategoryItem>{
        return listThemes
    }

    fun addListThemes(category: String){
        listThemes.add(CategoryItem(category))
    }

    fun removeListThemes(category: String){
        listThemes.remove(CategoryItem(category))
    }

    fun editListTheme(editTarget: String, editText: String){
        var key = 0
        var tempKey = 0
        listThemes.forEach {
        key += 1
            if (it.name == editTarget) {
                tempKey = key - 1
            }
        }
        listThemes[tempKey].name = editText
    }

    fun saveData(category: String){
        viewModelScope.launch(Dispatchers.IO) {
            mCategory = category
        }
    }

    fun getNewsArticles(): LiveData<ViewState<List<NewsArticleDb>>> = newsRepository.getNewsArticles(mCategory).asLiveData()
}