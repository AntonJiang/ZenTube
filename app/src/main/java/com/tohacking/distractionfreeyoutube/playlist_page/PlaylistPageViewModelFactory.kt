package com.tohacking.distractionfreeyoutube.playlist_page

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PlaylistPageViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlaylistPageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlaylistPageViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}