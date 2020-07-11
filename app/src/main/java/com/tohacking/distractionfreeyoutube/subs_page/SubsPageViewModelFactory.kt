package com.tohacking.distractionfreeyoutube.subs_page

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SubsPageViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubsPageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SubsPageViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}