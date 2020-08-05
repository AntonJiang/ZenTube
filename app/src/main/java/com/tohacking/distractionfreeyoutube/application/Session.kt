package com.tohacking.distractionfreeyoutube.application

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tohacking.distractionfreeyoutube.repository.data.User
import net.openid.appauth.AuthState

class Session {
    companion object {
        lateinit var authState: AuthState

        private val _user = MutableLiveData<User>()
        val user: LiveData<User>
            get() = _user

        fun setUser(u: User){
            _user.postValue(u)
        }
    }


}