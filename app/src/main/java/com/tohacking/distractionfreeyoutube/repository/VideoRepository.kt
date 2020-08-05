package com.tohacking.distractionfreeyoutube.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.tohacking.distractionfreeyoutube.repository.database.DatabasePlaylist
import com.tohacking.distractionfreeyoutube.repository.database.DatabaseVideo
import com.tohacking.distractionfreeyoutube.repository.database.VideoDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception

class VideoRepository(private val videoDao: VideoDao) {

    fun getVideo(videoId: String): LiveData<DatabaseVideo> {
        return videoDao.getVideo(videoId)
    }

    companion object {
        private const val HISTORY_PLAYLIST_INDEX = 0
    }



    fun getHistoryPlaylist(): LiveData<List<LiveData<DatabaseVideo>>>{
        return Transformations.map(
            videoDao.getPlaylist(
                HISTORY_PLAYLIST_INDEX
            )
        ) playlist@{ dbPlaylist ->
            if (dbPlaylist != null){
                if (dbPlaylist.content.isEmpty()){
                    Timber.d("Playlist returned: 0")
                    return@playlist listOf<LiveData<DatabaseVideo>>()
                }
                Timber.d("Playlist returned: '${dbPlaylist.content[0]}' ${dbPlaylist.content.size} ${dbPlaylist.content.isEmpty()}")
                return@playlist dbPlaylist.content.map {
                    getVideo(it)
                }
            }else {
                Timber.d("No History found, returning")
                CoroutineScope(Dispatchers.IO).launch{
                    videoDao.insert(DatabasePlaylist(HISTORY_PLAYLIST_INDEX, listOf<String>()))
                    Timber.d("After Insert${videoDao.getPlaylist(HISTORY_PLAYLIST_INDEX).value?.content?.isEmpty()}")
                }
                return@playlist listOf<LiveData<DatabaseVideo>>()
            }
        }
    }

}