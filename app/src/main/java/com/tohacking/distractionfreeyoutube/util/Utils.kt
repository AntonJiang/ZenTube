package com.tohacking.distractionfreeyoutube.util

import android.app.Application
import android.content.Context
import android.text.TextUtils
import com.tohacking.distractionfreeyoutube.application.EnvironmentVariable.AUTH_STATE
import com.tohacking.distractionfreeyoutube.application.EnvironmentVariable.PREF_OAUTH
import net.openid.appauth.AuthState
import org.json.JSONException

fun Application.persistAuthState(authState: AuthState) {
    getSharedPreferences(PREF_OAUTH, Context.MODE_PRIVATE).edit()
        .putString(AUTH_STATE, authState.toJsonString())
        .apply()
}

fun Application.clearAuthState() {
    getSharedPreferences(PREF_OAUTH, Context.MODE_PRIVATE)
        .edit()
        .remove(AUTH_STATE)
        .apply()
}

fun Application.restoreAuthState(): AuthState? {
    val jsonString =
        getSharedPreferences(PREF_OAUTH, Context.MODE_PRIVATE)
            .getString(AUTH_STATE, null)
    if (!TextUtils.isEmpty(jsonString)) {
        try {
            return AuthState.fromJson(jsonString!!)
        } catch (jsonException: JSONException) {
            // should never happen
        }
    }
    return null
}

fun Application.getAccessToken(): String {
    val authState = restoreAuthState()
    if (authState != null) {
        if (authState.isAuthorized and authState.needsTokenRefresh) {
            authState.update(authState.lastTokenResponse, authState.authorizationException)
            persistAuthState(authState)
        }
        return authState.accessToken!!
    }
    return "Error Token"
}

class Utils