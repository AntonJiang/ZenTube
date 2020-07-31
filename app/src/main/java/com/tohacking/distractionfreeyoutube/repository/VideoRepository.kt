package com.tohacking.distractionfreeyoutube.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.tohacking.distractionfreeyoutube.repository.database.DatabaseVideo
import com.tohacking.distractionfreeyoutube.repository.database.VideoDao

class VideoRepository(private val videoDao: VideoDao) {

    fun getVideo(videoId: String): LiveData<DatabaseVideo> {
        return videoDao.getVideo(videoId)
    }

    companion object {
        private const val HISTORY_PLAYLIST_INDEX = 0
    }

    val historyPlaylist: LiveData<List<LiveData<DatabaseVideo>>> = Transformations.map(
        videoDao.getPlaylist(
            HISTORY_PLAYLIST_INDEX
        )
    ) { dbPlaylist ->
        dbPlaylist.content.map {
            getVideo(it)
        }
    }

}