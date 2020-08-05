package com.tohacking.distractionfreeyoutube.application

import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.tohacking.distractionfreeyoutube.R
import com.tohacking.distractionfreeyoutube.application.EnvironmentVariable.PACKAGE_NAME
import com.tohacking.distractionfreeyoutube.application.EnvironmentVariable.USED_INTENT
import com.tohacking.distractionfreeyoutube.databinding.LoginScreenBinding
import com.tohacking.distractionfreeyoutube.util.persistAuthState
import com.tohacking.distractionfreeyoutube.util.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.openid.appauth.*
import timber.log.Timber


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginScreenBinding

    private val viewJob = Job()
    private val scope = CoroutineScope(viewJob + Dispatchers.Main)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Data Binding For login_screen.xml
        binding = DataBindingUtil.setContentView(this, R.layout.login_screen)
        binding.googleSignInButton.setOnClickListener {
            scope.launch {
                startSignIn(it)
            }
        }
    }

    /*
    Start OAuth Process
     */
    private fun startSignIn(view: View) {
        val serviceConfiguration = AuthorizationServiceConfiguration(
            Uri.parse("https://accounts.google.com/o/oauth2/v2/auth"),
            Uri.parse("https://www.googleapis.com/oauth2/v4/token")
        )

        val clientId = EnvironmentVariable.GOOGLE_CLIENT_ID
        val redirectUri = Uri.parse("${PACKAGE_NAME}:/oauth2callback")

        val authRequest = AuthorizationRequest.Builder(
            serviceConfiguration,
            clientId,
            ResponseTypeValues.CODE,
            redirectUri
        ).setScopes(
            "profile",
            "https://www.googleapis.com/auth/youtube",
            "https://www.googleapis.com/auth/youtube.readonly"
        ).build()

        val authService: AuthorizationService = AuthorizationService(this)

        // Redirect to HANDLE_AUTHORIZATION_RESPONSE after initial request
        val action = "${PACKAGE_NAME}.HANDLE_AUTHORIZATION_RESPONSE"
        val postAuthIntent = Intent(action)
        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            postAuthIntent, 0
        )

//            // Perform Authorization, get access token
//            authService.performAuthorizationRequest(authRequest, pendingIntent, pendingIntent)
        Timber.d("Performing Auth request")
        authService.performAuthorizationRequest(
            authRequest,
                    pendingIntent
        )
    }


    override fun onStart() {
        super.onStart()
        checkIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        checkIntent(intent)
    }

    /*
    Process Authorization response Intent
     */
    private fun checkIntent(intent: Intent?) {
        if (intent != null) {
            when (intent.action) {
                "${PACKAGE_NAME}.HANDLE_AUTHORIZATION_RESPONSE" ->
                    if (!intent.hasExtra(USED_INTENT)) {
                        Timber.i("Processing OAuth Response Intent..")
                        handleAuthorizationResponse(intent)
                        intent.putExtra(USED_INTENT, true)
                    }
                else -> {
                }
            }
        }
    }


    /*
    Process OAuth Response
     */
    private fun handleAuthorizationResponse(intent: Intent) {
        val response = AuthorizationResponse.fromIntent(intent)
        val error = AuthorizationException.fromIntent(intent)

        val authState = AuthState(response, error)
        if (response != null) {
            Timber.i("Handled Authorization Response ${authState.jsonSerializeString()}")

            // Request access token and others...
            val service = AuthorizationService(applicationContext)

            service.performTokenRequest(
                response.createTokenExchangeRequest()
            ) { tokenResponse, exception ->
                if (exception != null) {
                    Timber.w("Token Exchange failed $exception")
                } else {
                    if (tokenResponse != null) {
                        authState.update(tokenResponse, exception)
                        // Save auth state
                        Session.authState = authState
                        application.persistAuthState(authState)
                        Timber.i(
                            String.format(
                                "Token Response [ Access Token: %s, ID Token: %s ]",
                                tokenResponse.accessToken,
                                tokenResponse.idToken
                            )
                        )
                        // Redirect logged in user to main activity
                        val startupIntent = Intent(this, MainActivity::class.java)
                        startupIntent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(startupIntent)
                    } else {
                        Timber.w("Token Exchange received no response or error")
                    }
                }
            }
        }
    }
}

