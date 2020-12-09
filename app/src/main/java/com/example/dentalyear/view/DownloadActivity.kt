package com.example.dentalyear.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.dentalyear.R
import com.example.dentalyear.data.model.VideoModel
import com.example.dentalyear.utils.Status
import com.example.dentalyear.utils.Utility
import com.example.dentalyear.utils.asVideoDatabaseModel
import com.example.dentalyear.view.adapter.DownloadAdapter
import com.example.dentalyear.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.koushikdutta.ion.Ion
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_download.*
import kotlinx.android.synthetic.main.recycler_view_downloading_list_item.*
import java.io.File


@AndroidEntryPoint
class DownloadActivity : AppCompatActivity() {
    private lateinit var adapter: DownloadAdapter
    private lateinit var viewModel: MainViewModel
    private var videos = mutableListOf<VideoModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

        // Define viewModel
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // init recyclerView
        initRecyclerView()

        // Listen for videos
        viewModel.getVideos()?.observe(this, { videos ->
            when (videos.status) {
                Status.LOADING -> {
                    Log.d("DownloadActivity", "Loading...")
                }
                Status.SUCCESS -> {
                    Log.d("DownloadActivity", "${videos.data?.size}")
                    videos.data?.let {
                        Log.d("DownloadActivity", "Inside ***")
                        downloadNewVideoOrShowData(getDownloadedVideos(videos.data))
                        adapter.setData(getDownloadedVideos((videos.data)))
                    }
                    Log.d("DownloadActivity", "Success...")
                }
                Status.ERROR -> {
                }
            }
        })

    }

    private fun initRecyclerView() {
        adapter = DownloadAdapter()
        activity_download_recycler_view.adapter = adapter
    }

    private fun downloadNewVideoOrShowData(videos: List<VideoModel>) {
        this.videos = videos as MutableList<VideoModel>
        adapter.setData(videos)
        if (isIntentExists()) {
            intent.extras?.getParcelable<VideoModel>(VideoFragment.VIDEO_DATA)?.let {
                startDownload(it)
            }
        } else {
            Log.d("DownloadActivity", "HERE 1234")
            Log.d("DownloadActivity", "${videos.size}")
//            this.videos = videos as MutableList<VideoModel>
//            adapter.setData(videos)
        }

    }

    private fun isIntentExists() =
        intent.extras != null &&
                intent.extras?.containsKey(VideoFragment.VIDEO_DATA)!! &&
                intent.extras?.getParcelable<VideoModel>(VideoFragment.VIDEO_DATA) != null

    private fun getDownloadedVideos(videos: List<VideoModel>): MutableList<VideoModel> {
        val downloadedVideos = mutableListOf<VideoModel>()
        for (video in videos) {
            Log.d("DownloadActivity", "Outside: ${video.downloadStatus}")
            if (video.downloadStatus == Utility.DOWNLOADED) {
                Log.d("DownloadActivity", "Inside: ${video.videoTitle}")
                downloadedVideos.add(video)
            }
        }
        return downloadedVideos
    }

    private fun startDownload(data: VideoModel) {
        activity_download_downloading_item.visibility = View.VISIBLE
        downloading_recycler_view_video_title.text = data.videoTitle
        downloading_recycler_view_video_duration.text = data.videoDuration
        Log.d("DownloadActivity", "Start downloading...")
        Ion.with(this)
            .load(data.videoDownloadLink)
            .progress { downloaded, total ->
                run {
                    Log.d("DownloadActivity", "Progress: ${100.0 * downloaded / total}")
                    runOnUiThread {
                        downloading_recycler_view_video_current_size.text = (downloaded/1048576).toString()
                        downloading_recycler_view_total_downloaded_size.text = (total/1048576).toString()
                        downloading_recycler_view_video_progress_bar.progress =
                            (100.0 * downloaded / total).toInt()

                    }
                }
            }
            .write(File(getFilePath(), "${data.id}.mp4"))
            .setCallback { e, file ->
                run {
                    if (e == null) {
                        data.downloadStatus = Utility.DOWNLOADED
                        Log.d("DownloadActivity", "$data")
                        viewModel.updateVideo(data.asVideoDatabaseModel())
                        videos.add(data)
//                        adapter.addItem(data)
                        adapter.setData(videos)
                        activity_download_downloading_item.visibility = View.GONE
                        Log.d("DownloadActivity", "${data.asVideoDatabaseModel()}")
//                        viewModel.refreshVideos()
                        Log.d("DownloadActivity", "File: ${file.absoluteFile}")
                        Snackbar.make(
                            activity_download_container,
                            "File downloaded successfully",
                            Snackbar.LENGTH_SHORT
                        )
                    }
                }
            }
    }


    private fun getFilePath(): String {
        val file = File(filesDir, "videos")
        if (!file.exists())
            file.mkdir()
        return try {
            file.absolutePath
        } catch (e: Exception) {
            ""
        }
    }
}