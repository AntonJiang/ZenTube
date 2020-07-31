package com.tohacking.distractionfreeyoutube.repository.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class YoutubeSearchInfo(
    val kind: String,
    @Json(name = "etag")
    val eTag: String,
    val nextPageToken: String = "",
    val prevPageToken: String = "",
    val pageInfo: PageInfo,
    val items: List<SearchResultItem>
) : Parcelable

@Parcelize
data class SearchResultItem(
    val kind: String,
    @Json(name = "etag")
    val eTag: String,
    val id: SearchVideoItemId,
    val snippet: VideoSnippet
) : Parcelable {
    fun toVideoItem(): VideoItem {
        return VideoItem(kind, eTag, id.videoId, snippet)
    }
}


@Parcelize
data class SearchVideoItemId(
    val kind: String,
    val videoId: String
) : Parcelable
