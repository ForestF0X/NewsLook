package com.example.newslook

import android.app.Application
import dagger.Provides
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Singleton

@HiltAndroidApp
class NewsApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}