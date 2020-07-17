package com.tohacking.distractionfreeyoutube.application

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tohacking.distractionfreeyoutube.R
import com.tohacking.distractionfreeyoutube.repository.data.VideoItem

class PlaylistAdapter : RecyclerView.Adapter<VideoItemViewHolder>() {
    var data = listOf<VideoItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.video_highlight_item_view, parent, false) as TextView
        return VideoItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: VideoItemViewHolder, position: Int) {
        val item = data[position]
        holder.textView.text = item.snippet.title
    }
}