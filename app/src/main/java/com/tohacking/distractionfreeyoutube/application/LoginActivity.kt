package com.tohacking.distractionfreeyoutube.application

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.tohacking.distractionfreeyoutube.R
import com.tohacking.distractionfreeyoutube.databinding.LoginScreenBinding
import com.tohacking.distractionfreeyoutube.util.toast
import timber.log.Timber


class LoginActivity : AppCompatActivity() {

    lateinit var binding: LoginScreenBinding
    lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.login_screen)

        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(Scope())
                .requestEmail()
                .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        // Dummy Button For Login
        binding.googleSignInButton.setOnClickListener {
            doSignIn()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EnvironmentVariable.RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }

    }

//    private fun useAccount(account: GoogleSignInAccount?){
//        val prefs = getSharedPreferences(EnvironmentVariable.USER_INFO, Context.MODE_PRIVATE) ?: return
//        account?.let {
//            Timber.i(account.idToken)
////            with(prefs.edit()){
////                putString(EnvironmentVariable.GOOGLE_USER_TOKEN, it.idToken)
////                putString(EnvironmentVariable.PREF_KEY_USERNAME, it.displayName)
////                putString(EnvironmentVariable.PREF_KEY_EMAIL, it.email)
////                putString(EnvironmentVariable.PREF_KEY_PHOTOURL, it.photoUrl.toString())
////                apply()
////            }
//        }
//    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account =
                completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            EnvironmentVariable.user = account!!
            toast("Logged In")
            setResult(Activity.RESULT_OK)
            finish()
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Timber.i("signInResult:failed code= ${e.statusCode}")
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    private fun doSignIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, EnvironmentVariable.RC_SIGN_IN)
    }
}