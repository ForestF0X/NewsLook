package com.example.newslook.news.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataPreference(context: Context) {

    private val dataStore: DataStore<Preferences> = context.createDataStore(
        name = "ui_mode_preference"
    )

    suspend fun saveToDataStore(category: String) {
        dataStore.edit { preferences ->
            preferences[CATEGORY_KEY] = category
        }
    }

    val category: Flow<String?> = dataStore.data
        .map { preferences ->
            val category = preferences[CATEGORY_KEY]
            category
        }

    companion object {
        private val CATEGORY_KEY = preferencesKey<String>("category")
    }

}