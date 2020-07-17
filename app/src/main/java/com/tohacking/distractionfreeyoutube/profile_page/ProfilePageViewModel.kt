package com.tohacking.distractionfreeyoutube.profile_page

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tohacking.distractionfreeyoutube.application.Session
import com.tohacking.distractionfreeyoutube.repository.data.User
import com.tohacking.distractionfreeyoutube.util.clearAuthState
import com.tohacking.distractionfreeyoutube.util.removeUser
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

    private var _loggedOff = MutableLiveData<Boolean>()

    val displayName: LiveData<String>
        get() = _displayName

    val email: LiveData<String>
        get() = _email

    val photoUrl: LiveData<String>
        get() = _photourl

    val loggedOff: LiveData<Boolean>
        get() = _loggedOff

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
        Timber.i(Session.user.username)
        _displayName.value = Session.user.username
        _loggedOff.value = false
    }

    fun logout() {
        Session.user = User()
        scope.launch {
            app.removeUser()
            app.clearAuthState()
        }
        _displayName.value = "Anonymous"
        _loggedOff.value = true
        // Redirect to Login
    }


}