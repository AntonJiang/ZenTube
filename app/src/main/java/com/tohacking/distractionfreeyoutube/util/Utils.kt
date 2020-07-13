package com.tohacking.distractionfreeyoutube.util

import android.app.Application
import android.content.Context
import android.text.TextUtils
import com.tohacking.distractionfreeyoutube.application.EnvironmentVariable
import com.tohacking.distractionfreeyoutube.application.EnvironmentVariable.AUTH_STATE
import com.tohacking.distractionfreeyoutube.application.EnvironmentVariable.PREF_OAUTH
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationService
import org.json.JSONException
import timber.log.Timber

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

fun Application.useAccessTokenWith(block: (String) -> Unit) {
    EnvironmentVariable.authState.needsTokenRefresh = true
    Timber.d("Access Token before checking for refresh: ${EnvironmentVariable.authState.accessToken}")
    EnvironmentVariable.authState.performActionWithFreshTokens(
        AuthorizationService(applicationContext)
    ) { accessToken, _, ex ->

        if (ex != null) {
            Timber.w(ex.toJsonString())
            return@performActionWithFreshTokens
        }
        if (accessToken != null)
            block(accessToken)
        else
            Timber.w("Null access token detected")
    }
}

class Utils