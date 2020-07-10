package com.tohacking.distractionfreeyoutube.history_page

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HistoryPageViewModelFactory(private val application: Application)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryPageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoryPageViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}