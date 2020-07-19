package com.tohacking.distractionfreeyoutube.application


import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX
import com.tohacking.distractionfreeyoutube.R
import com.tohacking.distractionfreeyoutube.databinding.UiBackboneBinding
import com.tohacking.distractionfreeyoutube.repository.data.User
import com.tohacking.distractionfreeyoutube.util.loadUser
import com.tohacking.distractionfreeyoutube.util.saveUser
import com.tohacking.distractionfreeyoutube.util.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    lateinit var binding: UiBackboneBinding
    lateinit var uiScope: CoroutineScope
    lateinit var uiJob: Job
    var youtubePlayer: YouTubePlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("Starting Main Activity...")


        // Data binding for ui_backbone.xml
        binding = DataBindingUtil.setContentView(this, R.layout.ui_backbone)

        // Coroutine
        uiJob = Job()
        uiScope = CoroutineScope(Dispatchers.Main + uiJob)

        // Populate top environment menu
        // Dummy Environments
        val environments =
            arrayOf("Env1", "Env2", "Env3", "Env4")
        val adapter: ArrayAdapter<String> = ArrayAdapter(
            this
            , R.layout.environment_item
            , environments
        )
        val editTextFilledExposedDropdown: AutoCompleteTextView =
            binding.environmentMenuLayout.filledExposedDropdown
        editTextFilledExposedDropdown.setAdapter(adapter)

        // Set up bottom navigation menu
        // Default button set to history button
        binding.bottomNavigation.selectedItemId = R.id.history_button

        initYoutubePlayer()
        setUpNavigation()

        uiScope.launch {
            application.loadUser()
        }
    }

    override fun onStop() {
        super.onStop()
        // Only save if isn't anon user
        if (Session.user != User())
            uiScope.launch { application.saveUser() }

    }

    private fun initYoutubePlayer() {
        val playerFragment = YouTubePlayerSupportFragmentX.newInstance() ?: return
        supportFragmentManager.beginTransaction().add(R.id.fragment_container, playerFragment)
            .commit()

        Timber.i("Initializing Player")
        playerFragment.initialize(
            EnvironmentVariable.GOOGLE_API_KEY,
            object : YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(
                    provider: YouTubePlayer.Provider?,
                    player: YouTubePlayer?,
                    wasRestored: Boolean
                ) {
                    Timber.i("provider is ${provider!!::class}")
                    toast("Initialized Successful")
                    if (player != null) {
                        youtubePlayer = player
                        player.cueVideo("c0KYU2j0TM4")
                        youtubePlayer!!.setPlaybackEventListener(playbackEventListener)
                        youtubePlayer!!.setPlayerStateChangeListener(playerStateChangeListener)
                    } else {
                        Timber.w("Player is null!")
                    }

                }

                override fun onInitializationFailure(
                    p0: YouTubePlayer.Provider?,
                    p1: YouTubeInitializationResult?
                ) {
                    Timber.e("Initialization Failure $p1")
                }
            })

    }

    private val playbackEventListener: YouTubePlayer.PlaybackEventListener = object :
        YouTubePlayer.PlaybackEventListener {
        override fun onPlaying() {
        }

        override fun onPaused() {
        }

        override fun onStopped() {
        }

        override fun onBuffering(b: Boolean) {}
        override fun onSeekTo(i: Int) {}
    }

    private val playerStateChangeListener: YouTubePlayer.PlayerStateChangeListener =
        object : YouTubePlayer.PlayerStateChangeListener {
            override fun onLoading() {}
            override fun onLoaded(s: String) {}
            override fun onAdStarted() {
            }

            override fun onVideoStarted() {
            }

            override fun onVideoEnded() {
            }

            override fun onError(errorReason: YouTubePlayer.ErrorReason) {
                Timber.e("Play State Error $errorReason")
            }
        }

    // Set up navigation for the main bottom menu
    private fun setUpNavigation() {
        val navController = findNavController(R.id.my_nav_host_fragment)
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.profile_button -> {
                    navController.navigate(R.id.action_global_profilePageFragment)
                    true
                }
                R.id.history_button -> {
                    navController.navigate(R.id.action_global_historyPageFragment)
                    true
                }
                R.id.subscription_button -> {
                    navController.navigate(R.id.action_global_subsPageFragment)
                    true
                }
                R.id.search_button -> {
                    navController.navigate(R.id.searchPageFragment)
                    true
                }
                R.id.playlist_button -> {
                    navController.navigate(R.id.action_global_playlistPageFragment)
                    true
                }
                else -> false
            }
        }
    }
}