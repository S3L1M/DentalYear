package com.example.dentalyear.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.dentalyear.R
import com.example.dentalyear.data.model.VideoModel
import com.example.dentalyear.utils.DownloadedVideoItemClickListener
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
class DownloadActivity : AppCompatActivity(), DownloadedVideoItemClickListener {
    private lateinit var adapter: DownloadAdapter
    private lateinit var viewModel: MainViewModel
    private var videos = mutableListOf<VideoModel>()
    private var isDownloaded = false


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
                }
                Status.SUCCESS -> {
                    Log.d("DownloadActivity", "${videos.data?.size}")
                    videos.data?.let {
                        downloadNewVideoOrShowData(getDownloadedVideos(videos.data))
                        adapter.setData(getDownloadedVideos((videos.data)))
                    }
                }
                Status.ERROR -> {
                }
            }
        })

    }

    private fun initRecyclerView() {
        adapter = DownloadAdapter(this)
        activity_download_recycler_view.adapter = adapter
    }

    private fun downloadNewVideoOrShowData(videos: List<VideoModel>) {
        this.videos = videos as MutableList<VideoModel>
        adapter.setData(videos)
        if (isIntentExists()) {
            intent.extras?.getParcelable<VideoModel>(VideoFragment.VIDEO_DATA)?.let {
                startDownload(it)
            }
        }
    }

    override fun onDownloadedVideoClicked(data: VideoModel) {
        val intent = Intent()
        intent.putExtra(VideoFragment.VIDEO_MODEL, data)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun isIntentExists() =
        intent.extras != null &&
                intent.extras?.containsKey(VideoFragment.VIDEO_DATA)!! &&
                intent.extras?.getParcelable<VideoModel>(VideoFragment.VIDEO_DATA) != null

    private fun getDownloadedVideos(videos: List<VideoModel>): MutableList<VideoModel> {
        val downloadedVideos = mutableListOf<VideoModel>()
        for (video in videos) {
            if (video.downloadStatus == Utility.DOWNLOADED) {
                downloadedVideos.add(video)
            }
        }
        return downloadedVideos
    }

    private fun startDownload(data: VideoModel) {

        activity_download_downloading_item.visibility = View.VISIBLE
        downloading_recycler_view_video_title.text = data.videoTitle
        downloading_recycler_view_video_duration.text = data.videoDuration
        Ion.getDefault(this).conscryptMiddleware.enable(false)
        Ion.with(this)
            .load(data.videoDownloadLink)
            .progress { downloaded, total ->
                run {
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
                        if (!isDownloaded) {
                            data.downloadStatus = Utility.DOWNLOADED
                            data.videoLink = file.absolutePath
                            viewModel.updateVideo(data.asVideoDatabaseModel())
                            videos.add(data)
                            adapter.addItem(data)
                            activity_download_downloading_item.visibility = View.GONE
                            Snackbar.make(
                                activity_download_container,
                                "File downloaded successfully",
                                Snackbar.LENGTH_SHORT
                            ).show()
                            isDownloaded = true
                        }
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

    fun back(v: View) = finish()
}