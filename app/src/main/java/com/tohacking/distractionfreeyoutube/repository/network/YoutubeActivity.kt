package com.tohacking.distractionfreeyoutube.repository.network

import android.os.Bundle
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener
import com.google.android.youtube.player.YouTubePlayerView
import com.tohacking.distractionfreeyoutube.R
import com.tohacking.distractionfreeyoutube.application.EnvironmentVariable.GOOGLE_API_KEY
import com.tohacking.distractionfreeyoutube.databinding.ActivityYoutubeBinding
import com.tohacking.distractionfreeyoutube.util.toast
import timber.log.Timber


class YoutubeActivity(val videoId: String = "UnJ3amzJM94") : YouTubeBaseActivity(),
    YouTubePlayer.OnInitializedListener {

    lateinit var binding: ActivityYoutubeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_youtube
        )

        val youtubePlayerView = YouTubePlayerView(this)
        youtubePlayerView.layoutParams = ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT
            , ViewGroup.LayoutParams.MATCH_PARENT
        )
        binding.activityYoutube.addView(youtubePlayerView)
        youtubePlayerView.initialize(GOOGLE_API_KEY, this)
    }

    override fun onInitializationSuccess(
        provider: YouTubePlayer.Provider?,
        youtubePlayer: YouTubePlayer?,
        wasRestored: Boolean
    ) {
        Timber.i("provider is ${provider!!::class}")
        toast("Initialized Successful")
        youtubePlayer?.setPlaybackEventListener(playbackEventListener)
        youtubePlayer?.setPlayerStateChangeListener(playerStateChangeListener)
        if (!wasRestored){
            youtubePlayer?.cueVideo(videoId)
        }
    }

    override fun onInitializationFailure(
        p0: YouTubePlayer.Provider?,
        p1: YouTubeInitializationResult?
    ) {
        val REQUEST_CODE = 1
        if (p1 != null) {
            if (p1.isUserRecoverableError)
                p1.getErrorDialog(this, REQUEST_CODE).show()
            else{
                val errorMessage = "There was an error initializing the Player $p1"
                toast(errorMessage)
            }
        }
    }

private val playbackEventListener: PlaybackEventListener = object : PlaybackEventListener {
    override fun onPlaying() {
        this@YoutubeActivity.toast("Good, video is playing ok")
    }

    override fun onPaused() {
        this@YoutubeActivity.toast("Video has paused")
    }

    override fun onStopped() {
        this@YoutubeActivity.toast("Video has stopped")
    }

    override fun onBuffering(b: Boolean) {}
    override fun onSeekTo(i: Int) {}
}

    private val playerStateChangeListener: PlayerStateChangeListener =
        object : PlayerStateChangeListener {
            override fun onLoading() {}
            override fun onLoaded(s: String) {}
            override fun onAdStarted() {
                this@YoutubeActivity.toast("Add started")
            }

            override fun onVideoStarted() {
                this@YoutubeActivity.toast("Video has started")
            }

            override fun onVideoEnded() {
                this@YoutubeActivity.toast("Video has ended")
            }

            override fun onError(errorReason: YouTubePlayer.ErrorReason) {}
        }

}