package com.example.dentalyear.utils

import com.example.dentalyear.data.model.ExhibitModel
import com.example.dentalyear.data.model.VideoModel
import com.example.dentalyear.view.adapter.ExhibitAdapter
import com.example.dentalyear.view.adapter.VideoAdapter

interface DownloadedVideoItemClickListener {
    fun onDownloadedVideoClicked(data: VideoModel)
}