package com.tohacking.distractionfreeyoutube.repository.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class YoutubePlaylist(
    val playlistID: String
) : Parcelable