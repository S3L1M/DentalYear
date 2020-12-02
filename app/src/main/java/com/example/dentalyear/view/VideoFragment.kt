package com.example.dentalyear.view

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dentalyear.R
import com.example.dentalyear.data.model.VideoModel
import com.example.dentalyear.utils.Status
import com.example.dentalyear.view.adapter.VideoAdapter
import com.example.dentalyear.viewmodel.MainViewModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import kotlinx.android.synthetic.main.fragment_video.*


class VideoFragment : Fragment() {

    companion object {
        private const val PLAYBACK_TIME = "playback_time"
        private const val PLAY_WHEN_READY = "play_when_ready"
    }

    private var playWhenReady: Boolean = true
    private var playBackPosition: Long = 0
    private var videosList: List<VideoModel>? = listOf()
    private lateinit var adapter: VideoAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the current position and play/pause | if there is any configuration changes
        savedInstanceState?.let {
            playBackPosition = it.getLong(PLAYBACK_TIME)
            playWhenReady = it.getBoolean(PLAY_WHEN_READY)
        }

        // Define ViewModel
        val viewModel: MainViewModel by viewModels()


        /**
         * Init Visibility of the views
         * By default the views will be HIDDEN until the data become available
         */
        changeViewVisibility(View.INVISIBLE)

        // Init Adapter
        initRecyclerViewAdapter()

        // Define media controller and attach it to videoView
//        initMediaPlayerController()

        // Get Videos
        viewModel.getVideos().observe(viewLifecycleOwner, { videos ->
            if (videos.status == Status.LOADING) {
                video_fragment_progressbar.visibility = View.VISIBLE
            } else if (videos.status == Status.SUCCESS) {
                video_fragment_progressbar.visibility = View.INVISIBLE
                changeViewVisibility(View.VISIBLE)
                Log.d("VideoFragment", "Status Data: ${videos.status}")
                Log.d("VideoFragment", "Message: ${videos.message}")
                Log.d("VideoFragment", "${videos.data?.size}")
                videosList = videos.data
                adapter.setData(videos.data)
                initPlayer("https://developers.google.com/training/images/tacoma_narrows.mp4")
            }
        })
    }

    private fun changeViewVisibility(visibility: Int) {
        video_fragment_category_options_text_view.visibility = visibility
        video_fragment_category_options_subtitle_text_view.visibility = visibility
        video_fragment_save_image_button.visibility = visibility
        video_fragment_tabLayout.visibility = visibility
        video_fragment_content_container.visibility = visibility
    }

    private fun initRecyclerViewAdapter() {
        video_fragment_recycler_view.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter = VideoAdapter()
        video_fragment_recycler_view.adapter = adapter
    }

    private fun initPlayer(url: String) {
        val player = SimpleExoPlayer.Builder(requireContext()).build()
        video_fragment_video_view.player = player
        player.setMediaItem(MediaItem.fromUri(url))
        player.playWhenReady = playWhenReady
        player.seekTo(playBackPosition)
        player.prepare()
    }

    private fun releasePlayer() {
        video_fragment_video_view?.player?.let {
            playWhenReady = it.playWhenReady
            playBackPosition = it.currentPosition
            it.release()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        video_fragment_video_view.player?.let {
            outState.putLong(
                PLAYBACK_TIME,
                it.currentPosition
            )
            outState.putBoolean(
                PLAY_WHEN_READY,
                it.playWhenReady
            )
        }
    }

    override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
            releasePlayer()
    }

    override fun onStop() {
        super.onStop()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            releasePlayer()
    }
}