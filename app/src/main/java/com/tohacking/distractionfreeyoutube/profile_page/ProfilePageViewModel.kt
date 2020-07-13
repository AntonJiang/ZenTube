package com.tohacking.distractionfreeyoutube.profile_page

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tohacking.distractionfreeyoutube.repository.network.YoutubeApi
import com.tohacking.distractionfreeyoutube.util.useAccessTokenWith
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber


class ProfilePageViewModel(val app: Application) : AndroidViewModel(app) {

    private val viewModelJob = Job()
    private val scope = CoroutineScope(viewModelJob + Dispatchers.Main)


    private var _displayName = MutableLiveData<String>()

    private var _email = MutableLiveData<String>()

    private var _photourl = MutableLiveData<String>()

    val displayName: LiveData<String>
        get() = _displayName

    val email: LiveData<String>
        get() = _email

    val photoUrl: LiveData<String>
        get() = _photourl

    fun updateDisplayName(name: String) {
        _displayName.value = name
    }

    fun updateEmail(email: String) {
        _email.value = email
    }

    fun updatePhotoUrl(url: String) {
        _photourl.value = url

    }

    init {
        getUserProfile()
    }

    private fun getUserProfile() {
        app.useAccessTokenWith {
            scope.launch {
                val header =
                    mapOf(Pair("Authorization", "Bearer $it"))
                val getDeferredProfile =
                    YoutubeApi.retrofitService.getYoutubeChannelInfoAsync(map = header)
                try {
                    val channelInfo = getDeferredProfile.await()
                    Timber.i("Profile: $channelInfo")
                } catch (e: Exception) {
                    Timber.i("$e Access Token: $it")
                }
            }
        }
    }


}