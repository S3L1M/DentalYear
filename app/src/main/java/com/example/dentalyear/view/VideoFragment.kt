package com.example.dentalyear.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.dentalyear.R
import com.example.dentalyear.viewmodel.MainViewModel


class VideoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: MainViewModel by viewModels()
        val videos = viewModel.getVideos().observe(viewLifecycleOwner, {videos->
            if(videos == null)
                return@observe
            Log.d("VideoFragment", "Status Data: ${videos.status}")
            Log.d("VideoFragment", "Message: ${videos.message}")
            Log.d("VideoFragment", "${videos.data?.size}")
        })
    }

}