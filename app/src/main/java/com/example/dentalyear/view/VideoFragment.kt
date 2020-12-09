package com.example.dentalyear.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.dentalyear.R
import com.example.dentalyear.data.model.VideoModel
import com.example.dentalyear.utils.Status
import com.example.dentalyear.utils.VideoItemClickListener
import com.example.dentalyear.view.adapter.VideoAdapter
import com.example.dentalyear.viewmodel.MainViewModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_video.*


@AndroidEntryPoint
class VideoFragment : Fragment(), VideoItemClickListener {

    companion object {
        const val VIDEO_POSITION = "video_position"
        const val VIDEO_DATA = "video_data"
        const val VIDEO_MODEL = "video_model"
    }

    private lateinit var adapter: VideoAdapter
    private lateinit var video: VideoModel
    private val funVibesList = mutableListOf<VideoModel>()
    private val oralHealthTipsList = mutableListOf<VideoModel>()
    private val smileQuotesList = mutableListOf<VideoModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Define ViewModel
        val viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        /**
         * Init Visibility of the views
         * By default the views will be HIDDEN until the data become available
         */
        changeViewVisibility(View.INVISIBLE)

        // Init Adapter
        initRecyclerViewAdapter()

        // Get Videos
        viewModel.getVideos()?.observe(viewLifecycleOwner, { videos ->
            Log.d("DownloadActivity", "HERE 1")
            when (videos.status) {
                Status.LOADING -> {
                    Log.d("DownloadActivity", "HERE 2")
                    video_fragment_progressbar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    Log.d("DownloadActivity", "HERE 3")
                    video_fragment_progressbar.visibility = View.INVISIBLE
                    changeViewVisibility(View.VISIBLE)
                    // Prepare data
                    prepareData(videos.data!!)
                    // Set the default data to funVibes
                    setAdapterData(funVibesList)
                }
                Status.ERROR -> {
                    Log.d("DownloadActivity", "HERE 4")
                }
            }
        })

        /*
         * Init view listeners
         */
        // thumbnail click listener
        video_fragment_video_thumbnail.setOnClickListener {
//            initPlayer(dummyVideos[position])
            initPlayer(video.videoLink)
        }
        // playButton click listener
        video_fragment_play_button_image_view.setOnClickListener {
//            initPlayer(dummyVideos[position])
            initPlayer(video.videoLink)
        }
        // TabLayout listener
        video_fragment_tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> setAdapterData(funVibesList)
                    1 -> setAdapterData(oralHealthTipsList)
                    2 -> setAdapterData(smileQuotesList)
                }
                Log.d("VideoFragment", "Position: ${tab?.position}")
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        // open saved videos
        video_fragment_save_image_button.setOnClickListener {
            openDownloadVideoActivity()
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
        adapter = VideoAdapter(requireContext(), this)
        video_fragment_recycler_view.adapter = adapter
    }

    /*
    * prepare videos list
    * by appending each video to its category
     */
    private fun prepareData(videos: List<VideoModel>) {
        for (video in videos) {
            for (category in video.acf.videoCategories) {
                when (category.id) {
                    // Fun Vibes Id
                    5524 -> funVibesList.add(video)
                    // Oral Health Id
                    5521 -> oralHealthTipsList.add(video)
                    // Smile Quotes Id
                    5518 -> smileQuotesList.add(video)
                }
            }
        }
    }

    private fun initPlayer(url: String) {
        val player = SimpleExoPlayer.Builder(requireContext()).build()
        /*
        *If the user clicked on thumbnail
        * hide thumbnail and playButton imageView
        * to show the video player behind it
         */
        video_fragment_video_thumbnail.visibility = View.INVISIBLE
        video_fragment_play_button_image_view.visibility = View.INVISIBLE
        video_fragment_playerView.visibility = View.VISIBLE
        video_fragment_playerView.player = player
        player.setMediaItem(MediaItem.fromUri(url))
        player.playWhenReady = true
        player.seekTo(0)
        player.prepare()
    }


    private fun openDownloadVideoActivity(
        position: Int = 0,
        data: VideoModel? = null
    ) {
        val intent = Intent(requireContext(), DownloadActivity::class.java)
        Log.d("VideoFragment", "HERE 1")
        if (data != null) {
            Log.d("VideoFragment", "HERE 2")
            intent.putExtra(VIDEO_POSITION, position)
            intent.putExtra(VIDEO_DATA, data)
        }
        startActivity(intent)
    }

    override fun onVideoItemClicked(data: VideoModel) {  //TODO Reuse this function
        // release player if the user click on another video
        releasePlayer()
        setVideoData(data)
    }

    override fun onVideoDownloadItemClicked(
        position: Int,
        data: VideoModel
    ) {
        openDownloadVideoActivity(position, data)
    }

    private fun setAdapterData(videos: List<VideoModel>) {
        // release player if the user clicked on the tabs
        releasePlayer()
        setVideoData(videos[0])
        adapter.setData(videos)
    }

    private fun setVideoData(video: VideoModel) {
        video_fragment_video_title.text = video.videoTitle
        video_fragment_video_duration.text = video.videoDuration
        // Keep the video to play it when needed
        this.video = video
        setThumbnail(video.acf.thumbImage)
    }

    private fun setThumbnail(url: String) {
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

        Glide.with(requireContext()).load(url).into(video_fragment_video_thumbnail)
    }

    private fun releasePlayer() {
        video_fragment_playerView?.player?.release()
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