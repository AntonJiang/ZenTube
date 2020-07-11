package com.tohacking.distractionfreeyoutube.profile_page

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProfilePageViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfilePageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfilePageViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}