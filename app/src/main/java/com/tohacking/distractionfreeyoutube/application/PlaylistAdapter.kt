package com.tohacking.distractionfreeyoutube.application

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubeThumbnailLoader
import com.google.android.youtube.player.YouTubeThumbnailView
import com.tohacking.distractionfreeyoutube.R
import com.tohacking.distractionfreeyoutube.application.EnvironmentVariable.GOOGLE_API_KEY
import com.tohacking.distractionfreeyoutube.repository.data.VideoItem
import timber.log.Timber


class PlaylistAdapter(val activity: MainActivity) : RecyclerView.Adapter<VideoItemViewHolder>() {
    var data = listOf<VideoItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.video_player_view, parent, false) as CardView
        return VideoItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: VideoItemViewHolder, position: Int) {
        val item = data[position]
        holder.thumbnailView.initialize(
            GOOGLE_API_KEY, object : YouTubeThumbnailView.OnInitializedListener {
                override fun onInitializationSuccess(
                    thumbnailView: YouTubeThumbnailView?,
                    thumbnailLoader: YouTubeThumbnailLoader?
                ) {
                    Timber.d("Binding Thumbnail ${item.id} Loader status ${thumbnailLoader}")
                    thumbnailLoader?.setVideo(item.id)
                    thumbnailLoader?.setOnThumbnailLoadedListener(object :
                        YouTubeThumbnailLoader.OnThumbnailLoadedListener {
                        override fun onThumbnailLoaded(p0: YouTubeThumbnailView?, p1: String?) {
                            thumbnailLoader.release()
                        }

                        override fun onThumbnailError(
                            p0: YouTubeThumbnailView?,
                            p1: YouTubeThumbnailLoader.ErrorReason?
                        ) {
                            Timber.e("Thumbnail loaded error $p1")
                        }
                    })
                }

                override fun onInitializationFailure(
                    thumbnailView: YouTubeThumbnailView?,
                    error: YouTubeInitializationResult?
                ) {
                    Timber.e("Thumbnail loaded error $error")
                }
            }
        )
        holder.cardView.setOnClickListener {
            //load selected video
            Timber.i("Starting Video ${item.id} Player status ${activity.youtubePlayer}")
            if (activity.youtubePlayer != null)
                activity.youtubePlayer?.cueVideo(item.id)
            Timber.d("Testing for error location")
        }
    }
}