package com.tohacking.distractionfreeyoutube.profile_page

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
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

    private var _loggedOff = MutableLiveData<Boolean>()

    val displayName: LiveData<String>
        get() = Transformations.map(Session.user){
            it.username
        }
    val loggedOff: LiveData<Boolean>
        get() = _loggedOff


    init {
        _loggedOff.value = false
    }

    fun logout() {
        Session.setUser(User())
        scope.launch {
            app.removeUser()
            app.clearAuthState()
        }
        _loggedOff.value = true
        // Redirect to Login
    }


}