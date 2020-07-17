package com.tohacking.distractionfreeyoutube.repository.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tohacking.distractionfreeyoutube.application.EnvironmentVariable
import com.tohacking.distractionfreeyoutube.repository.data.GoogleUserInfo
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Query

private const val BASE_URL = "https://www.googleapis.com/oauth2/v2/"

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

//object GoogleUser{
//    fun getGoogleUserInfo(app: Application): GoogleUserInfo {
//        return getTemplate(app){
//            UserInfoApi.retrofitService.getUserInfoAsync(map = it)
//        } as GoogleUserInfo
//    }
//}

interface GoogleUserInfoApiService {
    @GET("userinfo")
    fun getUserInfoAsync(
        @HeaderMap map: Map<String, String>,
        @Query("key") apiKey: String = EnvironmentVariable.GOOGLE_API_KEY
    ):
            Deferred<GoogleUserInfo>
}

object GoogleUserInfoApi {
    val retrofitService: GoogleUserInfoApiService by lazy {
        retrofit.create(GoogleUserInfoApiService::class.java)
    }
}