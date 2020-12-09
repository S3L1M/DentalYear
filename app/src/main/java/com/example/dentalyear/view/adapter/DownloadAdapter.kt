package com.example.dentalyear.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dentalyear.R
import com.example.dentalyear.data.model.VideoModel

class DownloadAdapter(
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

//        if (currentDownloadingStatus == Utility.DOWNLOADING && position == 0) {
//            holder.downloadingProgressBar.visibility = View.VISIBLE
//            holder.currentSizeTextView.visibility = View.VISIBLE
//            holder.totalSizeTextView.visibility = View.VISIBLE
//            holder.dashTextView.visibility = View.VISIBLE
//        } else {
//            holder.downloadingProgressBar.visibility = View.INVISIBLE
//            holder.downloadingProgressBar.visibility = View.INVISIBLE
//            holder.currentSizeTextView.visibility = View.INVISIBLE
//            holder.totalSizeTextView.visibility = View.INVISIBLE
//            holder.dashTextView.visibility = View.INVISIBLE
//        }
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