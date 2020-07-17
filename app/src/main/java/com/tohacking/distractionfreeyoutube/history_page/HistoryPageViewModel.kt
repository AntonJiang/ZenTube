package com.tohacking.distractionfreeyoutube.history_page

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tohacking.distractionfreeyoutube.repository.data.YoutubeVideo

class HistoryPageViewModel(app: Application): AndroidViewModel(app) {


    private val _playlist = MutableLiveData<List<YoutubeVideo>>()


    val playlist: LiveData<List<YoutubeVideo>>
        get() = _playlist

    init {
        _playlist.value = listOf(
            "aa", "bb", "cc", "dd", "ee", "ff", "gg", "hh", "kk",
            "aa", "bb", "cc", "dd", "ee", "ff", "gg", "hh", "kk",
            "aa", "bb", "cc", "dd", "ee", "ff", "gg", "hh", "kk"
        ).map {
            YoutubeVideo(it)
        }
    }
}