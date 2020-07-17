package com.tohacking.distractionfreeyoutube.repository.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class YoutubeChannelInfo(
    val kind: String,
    @Json(name = "etag")
    val eTag: String,
    val pageInfo: PageInfo,
    @Json(name = "items")
    val channelItems: List<ChannelItem>
) : Parcelable

@Parcelize
data class PageInfo(
    val totalResults: Int,
    val resultsPerPage: Int
) : Parcelable

@Parcelize
data class ChannelItem(
    val kind: String,
    @Json(name = "etag")
    val eTag: String,
    @Json(name = "id")
    val channelId: String,
    val snippet: ChannelSnippet

) : Parcelable

@Parcelize
data class ChannelSnippet(
    @Json(name = "title")
    val channelName: String,
    val description: String,
    val publishedAt: String,
    val localized: LocalizedSnippet
) : Parcelable

@Parcelize
data class LocalizedSnippet(
    @Json(name = "title")
    val title: String,
    val description: String
) : Parcelable