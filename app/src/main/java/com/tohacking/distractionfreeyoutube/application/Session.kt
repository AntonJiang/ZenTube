package com.tohacking.distractionfreeyoutube.application

import com.tohacking.distractionfreeyoutube.repository.data.User
import net.openid.appauth.AuthState

class Session {
    companion object {
        lateinit var authState: AuthState
        lateinit var user: User
    }


}