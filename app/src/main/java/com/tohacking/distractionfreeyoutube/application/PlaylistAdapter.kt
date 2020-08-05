package com.tohacking.distractionfreeyoutube.application

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.tohacking.distractionfreeyoutube.R
import com.tohacking.distractionfreeyoutube.repository.database.DatabaseVideo
import timber.log.Timber


class PlaylistAdapter(
    val lifecycle: Lifecycle,
    val activity: MainActivity,
    val loadMore: () -> Unit
) :
    RecyclerView.Adapter<PlaylistAdapter.ViewHolder>() {

    var data = listOf<LiveData<DatabaseVideo>>()
        set(value) {
            Timber.d("${value.size}")
            field = value
            // TODO("Need to make this more efficient")
            notifyDataSetChanged()
        }
    private val attachedView = mutableSetOf<ViewHolder>()

    fun onPauseWithFragment() {
        Timber.d("Detaching attached cards...")
        Timber.d(attachedView.toString())
        val attachedViewIterator = attachedView.iterator()
        while (attachedViewIterator.hasNext()) {
            val holder = attachedViewIterator.next()

            if (holder.currentlyPlaying()) {
                Timber.d(holder.tracker.state.toString())
                shiftViewToTop(holder)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.video_card, parent, false) as YouTubePlayerView
        lifecycle.addObserver(view)
        Timber.d("onCreatePlaylistViewHolder")
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        Timber.d("RecycleView Detached")
        attachedView.remove(holder)
        if (holder.currentlyPlaying()) {
            Timber.d(holder.tracker.state.toString())
            shiftViewToTop(holder)
        }
        super.onViewDetachedFromWindow(holder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Timber.d("onBindView: $holder")
        data[position].value?.videoId?.let { holder.cueVideo(it) }
        if (position == itemCount - 1) loadMore()
    }

    private fun shiftViewToTop(holder: ViewHolder) {
        Timber.d("TopVideoPlayer Status: ${activity.youTubePlayer}, Holder status ${holder.currentVideoId} ${holder.tracker.currentSecond}")
//         Load top video player
        activity.youTubePlayer?.cueVideo(holder.currentVideoId!!, holder.tracker.currentSecond)
        holder.youTubePlayer?.pause()
        activity.youTubePlayer?.play()
    }

    class ViewHolder(youTubePlayerView: YouTubePlayerView) :
        RecyclerView.ViewHolder(youTubePlayerView) {

        var youTubePlayer: YouTubePlayer? =
            null
        var currentVideoId: String? = null

        var tracker: YouTubePlayerTracker = YouTubePlayerTracker()

        fun cueVideo(videoId: String) {
            currentVideoId = videoId
            if (youTubePlayer == null) return
            youTubePlayer!!.cueVideo(videoId, 0f)
        }

        init {
            youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    this@ViewHolder.youTubePlayer = youTubePlayer
                    this@ViewHolder.youTubePlayer!!.addListener(tracker)
                }
            })
        }

        fun currentlyPlaying(): Boolean {
            return youTubePlayer != null && tracker.state == PlayerConstants.PlayerState.PLAYING
                    || PlayerConstants.PlayerState.BUFFERING == tracker.state
        }
    }
}