package com.example.dentalyear.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dentalyear.R
import com.example.dentalyear.data.model.VideoModel
import com.example.dentalyear.utils.VideoItemClickListener
import com.squareup.picasso.Picasso

class VideoAdapter(
    private var data: List<VideoModel>? = null,
    private val itemClicked: VideoItemClickListener
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_view_video_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        data?.get(position)?.let { video ->
            holder.videoTitleTextView.text = video.videoTitle
            holder.durationTextView.text = video.videoDuration
            Picasso.get().load(video.acf.thumbImage).into(holder.videoThumbnail)

            holder.itemView.setOnClickListener {
                itemClicked.onVideoItemClicked(position, video)
            }
        }
    }

    override fun getItemCount() = data?.size ?: 0

    fun setData(newData: List<VideoModel>?) {
        data = newData
        notifyDataSetChanged()
    }

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val videoTitleTextView: TextView =
            itemView.findViewById(R.id.video_recycler_view_title_text_view)

        val durationTextView: TextView =
            itemView.findViewById(R.id.video_recycler_view_duration_text_view)

        val videoThumbnail: ImageView =
            itemView.findViewById(R.id.video_recycler_view_thumbnail_image_view)
    }
}