package com.tohacking.distractionfreeyoutube.application

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import timber.log.Timber

class StartUpActivity : Activity() {
    companion object {
        const val AUTHENTICATION_REQUEST_CODE = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (isLoggedIn()) {
            // Redirect logged in user to main activity
            val startupIntent = Intent(this, MainActivity::class.java)
            startupIntent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(startupIntent)
            finish()
        } else {
            // Redirect Not logged in user to login prompt
            val startupIntent = Intent(this, LoginActivity::class.java)
            startActivityForResult(startupIntent, AUTHENTICATION_REQUEST_CODE)
        }

        super.onCreate(savedInstanceState)
    }

    // Check if the user is currently logged in based on google SSO
    private fun isLoggedIn(): Boolean {
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            EnvironmentVariable.user = account
            return true
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.i("Finished LoginActivity resultcode: $resultCode")

        // Redirect to main activity after successful login
        if (requestCode == AUTHENTICATION_REQUEST_CODE && resultCode == RESULT_OK) {
            val startupIntent = Intent(this, MainActivity::class.java)
            startupIntent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(startupIntent)
        }
        finish()
    }
}