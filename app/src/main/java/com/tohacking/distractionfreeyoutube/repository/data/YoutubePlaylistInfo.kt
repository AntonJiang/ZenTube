package com.tohacking.distractionfreeyoutube.repository.data

import android.os.Parcelable
import com.squareup.moshi.Json
import com.tohacking.distractionfreeyoutube.repository.database.DatabaseVideo
import kotlinx.android.parcel.Parcelize

@Parcelize
data class YoutubePlaylistInfo(
    val kind: String,
    @Json(name = "etag")
    val eTag: String,
    val pageInfo: PageInfo,
    val items: List<VideoItem>

): Parcelable

@Parcelize
data class VideoItem(
    val kind: String = "",
    @Json(name = "etag")
    val eTag: String = "",
    val id: String,
    val snippet: VideoSnippet,
    val contentDetails: VideoContentDetail = VideoContentDetail(),
    val statistics: VideoStatistics = VideoStatistics()
) : Parcelable

fun VideoItem.toDatabaseVideo(): DatabaseVideo {
    return DatabaseVideo(id, snippet)
}

@Parcelize
data class VideoSnippet(
    val publishedAt: String = "",
    val channelId: String = "",
    val title: String = "",
    val description: String = "",
    val channelTitle: String = "",
    val categoryId: String = "",
    val defaultLanguage: String = "en",
    val defaultAudioLanguage: String = "",
    val tags: List<String> = listOf(),
    val localized: LocalizedSnippet = LocalizedSnippet()
): Parcelable


@Parcelize
data class VideoContentDetail(
    val duration: String = "",
    val dimension: String = "",
    val definition: String = "",
    val caption: String = "",
    val licensedContent: Boolean = true,
    val projection: String = ""
): Parcelable

@Parcelize
data class VideoStatistics(
    val viewCount: String = "",
    val likeCount: String = "",
    val dislikeCount: String = "",
    val favoriteCount: String = "",
    val commentCount: String = ""
): Parcelable