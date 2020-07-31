package com.tohacking.distractionfreeyoutube.history_page

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.tohacking.distractionfreeyoutube.repository.VideoRepository
import com.tohacking.distractionfreeyoutube.repository.data.VideoSnippet
import com.tohacking.distractionfreeyoutube.repository.database.DatabaseVideo
import com.tohacking.distractionfreeyoutube.repository.database.getDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryPageViewModel(app: Application): AndroidViewModel(app) {

    private val database = getDatabase(app)
    private val videosRepository = VideoRepository(database.databaseDao)

    val playlist: LiveData<List<LiveData<DatabaseVideo>>>

    init {
        viewModelScope.launch(Dispatchers.IO) {
            database.databaseDao.insert(DatabaseVideo("test", VideoSnippet()))
        }
        playlist = videosRepository.historyPlaylist

    }
}