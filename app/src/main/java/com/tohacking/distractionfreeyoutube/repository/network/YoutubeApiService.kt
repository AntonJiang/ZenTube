package com.tohacking.distractionfreeyoutube.repository.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tohacking.distractionfreeyoutube.application.EnvironmentVariable
import com.tohacking.distractionfreeyoutube.repository.data.YoutubeChannelInfo
import com.tohacking.distractionfreeyoutube.repository.data.YoutubePlaylistInfo
import com.tohacking.distractionfreeyoutube.repository.data.YoutubeSearchInfo
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Query


private const val BASE_URL = "https://www.googleapis.com/youtube/v3/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

private val httpClient = OkHttpClient.Builder().addInterceptor(logging)

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .client(httpClient.build())
    .build()

interface YoutubeApiService {

    @GET("channels?part=snippet,contentDetails&mine=true")
    fun getYoutubeChannelInfoAsync(
        @HeaderMap map: Map<String, String>,
        @Query("key") apiKey: String = EnvironmentVariable.GOOGLE_API_KEY
    ):
            Deferred<YoutubeChannelInfo>

    @GET("videos?part=snippet,contentDetails,statistics")
    fun getYoutubeVideoInfoAsync(
        @HeaderMap map: Map<String, String>,
        @Query("id") videoId: List<String>
    ):
            Deferred<YoutubePlaylistInfo>

    @GET("search?type=video&part=snippet")
    fun getYoutubeSearchResultAsync(
        @HeaderMap map: Map<String, String>,
        @Query("q") keyword: String,
        @Query("maxResults") maxResult: Int,
        @Query("key") apiKey: String = EnvironmentVariable.GOOGLE_API_KEY
    ):
            Deferred<YoutubeSearchInfo>

    @GET("search?type=video&part=snippet")
    fun getYoutubeNextPageSearchResultAsync(
        @HeaderMap map: Map<String, String>,
        @Query("maxResults") maxResult: Int,
        @Query("pageToken") nextPageToken: String = "",
        @Query("q") keyword: String,
        @Query("key") apiKey: String = EnvironmentVariable.GOOGLE_API_KEY
    ):
            Deferred<YoutubeSearchInfo>

}

object YoutubeApi {
    val retrofitService: YoutubeApiService by lazy {
        retrofit.create(YoutubeApiService::class.java)
    }
}