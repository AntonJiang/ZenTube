package com.tohacking.distractionfreeyoutube.search_page

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tohacking.distractionfreeyoutube.repository.data.toDatabaseVideo
import com.tohacking.distractionfreeyoutube.repository.database.DatabaseVideo
import com.tohacking.distractionfreeyoutube.repository.network.YoutubeApi
import com.tohacking.distractionfreeyoutube.util.toast
import com.tohacking.distractionfreeyoutube.util.useAccessToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class SearchPageViewModel(val app: Application) : AndroidViewModel(app) {
    private val _playlist = MutableLiveData<List<LiveData<DatabaseVideo>>>()

    private lateinit var nextSearchToken: String
    private lateinit var currentQuery: String

    val playlist: LiveData<List<LiveData<DatabaseVideo>>>
        get() = _playlist

    init {
        _playlist.value = mutableListOf()
    }

    private fun addToPlaylist(items: List<LiveData<DatabaseVideo>>) {
        Timber.d("Adding to search playlist ${items.size} items")
        _playlist.value = _playlist.value?.plus(items)
    }

    fun requestNextPage(maxResult: Int = 10) {
        if (nextSearchToken == ""){
            app.toast("No More Results")
            return
        }
        viewModelScope.launch {
            app.useAccessToken {
                val header =
                    mapOf(Pair("Authorization", "Bearer $it"))
                val getDeferred = YoutubeApi.retrofitService.getYoutubeNextPageSearchResultAsync(
                    header, maxResult, nextSearchToken, currentQuery
                )
                try {
                    // Get the SearchResult list and convert to list of VideoItem
                    val searchInfo = getDeferred.await()
                    nextSearchToken = searchInfo.nextPageToken
                    Timber.d("Next Page Token")
                    val videoList = searchInfo.items
                        .filter { it.id.kind == "youtube#video" }
                        .map {
                            MutableLiveData(it.toVideoItem().toDatabaseVideo())
                        }
                    withContext(Dispatchers.Main) {
                        addToPlaylist(videoList)
                    }

                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }
    }


    fun requestQuery(query: String, maxResult: Int = 10) {
        viewModelScope.launch {
            app.useAccessToken {
                val header =
                    mapOf(Pair("Authorization", "Bearer $it"))
                val getDeferred = YoutubeApi.retrofitService.getYoutubeSearchResultAsync(
                    header, query, maxResult
                )
                try {
                    // Get the SearchResult list and convert to list of VideoItem
                    val searchInfo = getDeferred.await()
                    nextSearchToken = searchInfo.nextPageToken
                    currentQuery = query
                    val videoList = searchInfo.items
                        .filter { it.id.kind == "youtube#video" }
                        .map { MutableLiveData(it.toVideoItem().toDatabaseVideo()) }
                    _playlist.postValue(videoList)
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }
    }
}

