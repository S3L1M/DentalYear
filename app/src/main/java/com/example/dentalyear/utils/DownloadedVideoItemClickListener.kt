package com.example.dentalyear.utils

import com.example.dentalyear.data.model.VideoModel

interface DownloadedVideoItemClickListener {
    fun onDownloadedVideoClicked(data: VideoModel)
}