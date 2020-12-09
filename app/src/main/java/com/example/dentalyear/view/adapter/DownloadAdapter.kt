package com.example.dentalyear.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dentalyear.R
import com.example.dentalyear.data.model.VideoModel
import com.example.dentalyear.utils.DownloadedVideoItemClickListener

class DownloadAdapter(
    private val itemClicked: DownloadedVideoItemClickListener,
    private var data: MutableList<VideoModel> = mutableListOf()
) : RecyclerView.Adapter<DownloadAdapter.DownloadViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DownloadViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_view_downloaded_list_item, parent, false)
        )


    override fun onBindViewHolder(holder: DownloadViewHolder, position: Int) {
        val video = data[position]
        holder.videoTitle.text = video.videoTitle
        holder.videoDuration.text = "Duration: ${video.videoDuration}"
        holder.itemView.setOnClickListener{
            itemClicked.onDownloadedVideoClicked(video)
        }
    }


    override fun getItemCount() = data.size


    fun addItem(item: VideoModel) {
        data.add(item)
        notifyItemInserted(data.size-1)
    }

    fun setData(data: MutableList<VideoModel>) {
        this.data = data
        notifyDataSetChanged()
    }

    class DownloadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var videoTitle: TextView =
            itemView.findViewById(R.id.download_recycler_view_video_title)

        val videoDuration: TextView =
            itemView.findViewById(R.id.download_recycler_view_video_duration)
    }
}