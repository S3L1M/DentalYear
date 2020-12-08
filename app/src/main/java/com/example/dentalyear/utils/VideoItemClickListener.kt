package com.example.dentalyear.utils

import com.example.dentalyear.data.model.VideoModel

interface VideoItemClickListener {
    fun onVideoItemClicked(position: Int, data: VideoModel)
    fun onVideoDownloadItemClicked(position: Int, data: VideoModel)
}