package com.tohacking.distractionfreeyoutube.search_page

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SearchPageViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchPageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchPageViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}