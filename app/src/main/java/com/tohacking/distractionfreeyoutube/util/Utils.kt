package com.tohacking.distractionfreeyoutube.util

import android.app.Application
import android.content.Context
import android.text.TextUtils
import com.google.gson.Gson
import com.tohacking.distractionfreeyoutube.application.EnvironmentVariable.AUTH_STATE
import com.tohacking.distractionfreeyoutube.application.EnvironmentVariable.PACKAGE_NAME
import com.tohacking.distractionfreeyoutube.application.EnvironmentVariable.USER_INFO
import com.tohacking.distractionfreeyoutube.application.Session
import com.tohacking.distractionfreeyoutube.application.Session.Companion.authState
import com.tohacking.distractionfreeyoutube.repository.data.User
import com.tohacking.distractionfreeyoutube.repository.network.GoogleUserInfoApi
import com.tohacking.distractionfreeyoutube.repository.network.YoutubeApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationService
import org.json.JSONException
import timber.log.Timber

fun Application.persistAuthState(authState: AuthState) {
    getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE).edit()
        .putString(AUTH_STATE, authState.toJsonString())
        .apply()
}

fun Application.clearAuthState() {
    getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE)
        .edit()
        .remove(AUTH_STATE)
        .apply()
}

fun Application.restoreAuthState(): AuthState? {
    val jsonString =
        getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE)
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

suspend fun Application.useAccessToken(callback: suspend (String) -> Unit) {
    val needRefresh = authState.needsTokenRefresh
    authState.performActionWithFreshTokens(
        AuthorizationService(applicationContext),
        AuthState.AuthStateAction { accessToken, _, ex ->
            Timber.d("New Access Token: ${authState.accessToken}")
            if (ex != null) {
                Timber.w(ex.toJsonString())
                return@AuthStateAction
            }
            if (accessToken != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    if (needRefresh) persistAuthState(authState)
                    callback(accessToken)
                }
            } else
                Timber.w("Null access token detected")
        })
}

suspend fun Application.loadUser() {
    val prefs = getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE)
    val userString = prefs.getString(USER_INFO, "NONE")
    Timber.d("Getting saved userString $userString")
    if (userString == null || userString == "NONE") {
        // No user saved or getting user failed
        Timber.i("Creating new user profile...")
        // Creating user from authState
        val user = User()
        useAccessToken {
            val header =
                mapOf(Pair("Authorization", "Bearer $it"))
            val getDeferredChannelInfo =
                YoutubeApi.retrofitService.getYoutubeChannelInfoAsync(map = header)
            val getDeferredGoogleUserInfo =
                GoogleUserInfoApi.retrofitService.getUserInfoAsync(map = header)

            user.username =
                getDeferredChannelInfo.await().toString() + "\n" + getDeferredGoogleUserInfo.await()
                    .toString()
            Timber.i("Getting User info ${user.username}")
            Session.user = user
            saveUser()
        }
    } else {
        Session.user = Gson().fromJson(userString, User::class.java)
    }
}

suspend fun Application.saveUser() {
    val user = Session.user
    Timber.d(user.toString())
    val prefs = getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE)
    withContext(Dispatchers.IO) {
        prefs.edit().let {
            it.putString(USER_INFO, Gson().toJson(user))
            it.commit()
        }
    }
}

suspend fun Application.removeUser() {
    Timber.i("Logging user off")
    val prefs = getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE)
    withContext(Dispatchers.IO) {
        prefs.edit().remove(USER_INFO).commit()
    }
}
