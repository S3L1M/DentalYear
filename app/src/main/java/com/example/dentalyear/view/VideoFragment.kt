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
import com.example.dentalyear.utils.VideoItemClickListener
import com.example.dentalyear.view.adapter.VideoAdapter
import com.example.dentalyear.viewmodel.MainViewModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.material.tabs.TabLayout
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_video.*


class VideoFragment : Fragment(), VideoItemClickListener {

    companion object {
        private const val PLAYBACK_TIME = "playback_time"
        private const val PLAY_WHEN_READY = "play_when_ready"
    }

    private var playWhenReady: Boolean = true
    private var playBackPosition: Long = 0
    private lateinit var adapter: VideoAdapter
    private lateinit var video: VideoModel
    // Default index for video from videos list is 0
    private  var position: Int = 0
    private val dummyVideos = mutableListOf<String>()


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
                adapter.setData(videos.data)
                video = videos.data?.get(0)!!
                setVideoData(video)
            }
        })

        // populate dummyData
        populateDummyData()

        /*
         * Init view listener
         */
        // thumbnail click listener
        video_fragment_video_thumbnail.setOnClickListener {
            initPlayer(dummyVideos[position])
        }
        // playButton click listener
        video_fragment_play_button_image_view.setOnClickListener {
            initPlayer(dummyVideos[position])
        }
        // TabLayout listener
        video_fragment_tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                /**
                 * TODO: Handle onTapSelected event
                 * I cannot configure how to get videos from category | from API
                 * URL: https://dentalyear.com/wp-json/wp/v2/
                 * endpoint: /video
                 * endpoint: /videocategory
                 */
                Log.d("VideoFragment", "Position: ${tab?.position}")
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                TODO("Not yet implemented")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun populateDummyData(){
        for(i in 1..10){
            dummyVideos.add("https://developers.google.com/training/images/tacoma_narrows.mp4")
            dummyVideos.add("https://storage.googleapis.com/exoplayer-test-media-0/play.mp3")
        }
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
        adapter = VideoAdapter(itemClicked = this)
        video_fragment_recycler_view.adapter = adapter
    }

    private fun initPlayer(url: String) {
        val player = SimpleExoPlayer.Builder(requireContext()).build()
        // If the user clicked on thumbnail | hide it and playButton to show the video player behind it
        video_fragment_video_thumbnail.visibility = View.INVISIBLE
        video_fragment_play_button_image_view.visibility = View.INVISIBLE
        video_fragment_playerView.visibility = View.VISIBLE
        video_fragment_playerView.player = player
        player.setMediaItem(MediaItem.fromUri(url))
        player.playWhenReady = playWhenReady
        player.seekTo(playBackPosition)
        player.prepare()
    }

    private fun releasePlayer() {
        video_fragment_playerView?.player?.let {
            playWhenReady = it.playWhenReady
            playBackPosition = it.currentPosition
            it.release()
        }
    }

    private fun resetPlayer(){
        video_fragment_playerView?.player?.let {
            playBackPosition = 0
            it.release()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        video_fragment_playerView.player?.let {
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

    override fun onVideoItemClicked(position: Int, data: VideoModel) {
        Log.d("VideoFragment", "Position: $position")
        // reset player if the user click on onther video
        resetPlayer()
        // Set thumbnail to video_view
        video = data
        this.position = position
        setVideoData(video)
    }

    private fun setVideoData(video: VideoModel){
        video_fragment_video_title.text = video.videoTitle
        video_fragment_video_duration.text = video.videoDuration
        setThumbnail(video.acf.thumbImage)
    }

    private fun setThumbnail(url: String){
        /*
         * Show thumbnail for the first time the of the Fragment
         * or
         * when the user click on another video
         * REMEMBER: That we make the thumbnail INVISIBLE
         * ever time the user click on the video to play (click on the thumbnail)
         */
        video_fragment_video_thumbnail.visibility = View.VISIBLE
        video_fragment_play_button_image_view.visibility = View.VISIBLE
        // Hide video_view
        video_fragment_playerView.visibility = View.INVISIBLE

        Picasso.get().load(url).into(video_fragment_video_thumbnail)
    }
}