package com.tohacking.distractionfreeyoutube.history_page

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tohacking.distractionfreeyoutube.repository.data.VideoItem
import com.tohacking.distractionfreeyoutube.repository.network.YoutubeApi
import com.tohacking.distractionfreeyoutube.util.useAccessToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class HistoryPageViewModel(app: Application): AndroidViewModel(app) {

    private val _playlist = MutableLiveData<List<VideoItem>>()

    val playlist: LiveData<List<VideoItem>>
        get() = _playlist


    init {
        viewModelScope.launch {
            app.useAccessToken {
                val ids = listOf("Ks-_Mh1QhMc","c0KYU2j0TM4","eIho2S0ZahI")
                val header =
                    mapOf(Pair("Authorization", "Bearer $it"))
                val getDeferred = YoutubeApi.retrofitService.getYoutubeVideoInfoAsync(header, ids)

                try {
                    val videoInfo = getDeferred.await()
                    withContext(Dispatchers.Main) {
                        _playlist.value = videoInfo.items
                    }

                } catch (e: Exception){
                    Timber.e(e)
                }
            }
        }
    }


}