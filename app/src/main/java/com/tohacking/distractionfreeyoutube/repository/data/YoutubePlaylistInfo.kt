package com.tohacking.distractionfreeyoutube.repository.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

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
    val kind:String,
    @Json(name = "etag")
    val eTag: String,
    val id: String,
    val sinppet: VideoSnippet,
    val contentDetails: VideoContentDetail,
    val statistics: VideoStatistics
): Parcelable

@Parcelize
data class VideoSnippet(
    val publishedAt: String,
    val channelId: String,
    val title: String,
    val description: String,
    val channelTitle: String,
    val categoryId: String,
    val defaultLanguage: String,
    val defaultAudioLanguage: String,
    val tags: List<String>,
    val localized: LocalizedSnippet

): Parcelable

@Parcelize
data class VideoContentDetail(
    val duration: String,
    val dimension: String,
    val definition: String,
    val caption: String,
    val licensedContent: Boolean,
    val projection: String
): Parcelable

@Parcelize
data class VideoStatistics(
    val viewCount: String,
    val likeCount: String,
    val dislikeCount: String,
    val favoriteCount: String,
    val commentCount: String
): Parcelable