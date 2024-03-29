package com.tohacking.distractionfreeyoutube.application

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.tohacking.distractionfreeyoutube.util.restoreAuthState
import net.openid.appauth.AuthState
import timber.log.Timber

class StartUpActivity : Activity() {
    companion object {
        const val AUTHENTICATION_REQUEST_CODE = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val autoState: AuthState? = application.restoreAuthState()
        if (autoState != null && autoState.isAuthorized) {
            // Redirect logged in user to main activity
            Session.authState = autoState

            Timber.i("Redirecting logged in user...")
            val mainActivityIntent = Intent(this, MainActivity::class.java)
            mainActivityIntent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(mainActivityIntent)
            finish()
        } else {
            // Redirect Not logged in user to login prompt
            Timber.i("Attempting to log in user...")
            val startupIntent = Intent(this, LoginActivity::class.java)
            startActivityForResult(startupIntent, AUTHENTICATION_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.d("Finished LoginActivity result code: $resultCode")

        // Redirect to main activity after successful login
        if (requestCode == AUTHENTICATION_REQUEST_CODE && resultCode == RESULT_OK) {
            val startupIntent = Intent(this, MainActivity::class.java)
            startupIntent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP
            Timber.i("Redirecting logged in new user...")
            startActivity(startupIntent)
        }
        finish()
    }
}