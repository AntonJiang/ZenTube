package com.tohacking.distractionfreeyoutube.application

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

object EnvironmentVariable {
    const val GOOGLE_API_KEY = "AIzaSyDNB7R36eO-UDbLvnmtmbCE3qvyTefDKr8"
    const val GOOGLE_CLIENT_ID =
        "395577048636-teii7tg3ou23n1924q9il7uso4rq6178.apps.googleusercontent.com"
    const val USER_INFO = "pref_file_user_info"
    const val RC_SIGN_IN = 9000
    lateinit var user: GoogleSignInAccount
}