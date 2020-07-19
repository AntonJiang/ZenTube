package com.tohacking.distractionfreeyoutube.application

import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.youtube.player.YouTubeThumbnailView
import com.tohacking.distractionfreeyoutube.R

class VideoItemViewHolder(val cardView: CardView) : RecyclerView.ViewHolder(cardView) {
    val thumbnailView: YouTubeThumbnailView = cardView.findViewById(R.id.video_thumbnail)
}