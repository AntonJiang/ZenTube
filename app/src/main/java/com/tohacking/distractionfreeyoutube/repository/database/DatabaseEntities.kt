package com.tohacking.distractionfreeyoutube.repository.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tohacking.distractionfreeyoutube.repository.data.VideoSnippet

@Entity
data class DatabaseVideo(
    @PrimaryKey
    val videoId: String,
    val snippet: VideoSnippet,
    val currentSecond: Int = 0
)

@Entity
data class DatabasePlaylist(
    @PrimaryKey
    val index: Int,
    val content: List<String>,
    val type: Int = 0
)
