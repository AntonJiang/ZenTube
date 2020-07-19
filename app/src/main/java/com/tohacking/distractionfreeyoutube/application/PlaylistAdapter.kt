package com.tohacking.distractionfreeyoutube.application

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.tohacking.distractionfreeyoutube.R
import com.tohacking.distractionfreeyoutube.repository.data.VideoItem


class PlaylistAdapter(val lifecycle: Lifecycle) :
    RecyclerView.Adapter<PlaylistAdapter.ViewHolder>() {
    var data = listOf<VideoItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.video_player_view, parent, false) as YouTubePlayerView
        lifecycle.addObserver(view)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cueVideo(data[position].id)
    }

    class ViewHolder(private val youTubePlayerView: YouTubePlayerView) :
        RecyclerView.ViewHolder(youTubePlayerView) {

        private var youTubePlayer: YouTubePlayer? =
            null
        private var currentVideoId: String? = null

        fun cueVideo(videoId: String?) {
            currentVideoId = videoId
            if (youTubePlayer == null) return
            youTubePlayer!!.cueVideo(videoId!!, 0f)
        }

        init {
            youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    this@ViewHolder.youTubePlayer = youTubePlayer
                    this@ViewHolder.youTubePlayer!!.cueVideo(currentVideoId!!, 0f)
                }
            })
        }
    }
}