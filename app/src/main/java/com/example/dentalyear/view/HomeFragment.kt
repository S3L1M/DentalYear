package com.example.dentalyear.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.dentalyear.R
import com.example.dentalyear.data.model.HomeData
import com.example.dentalyear.view.adapter.HomeAdapter
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // init recyclerView
        home_recycler_view.adapter = HomeAdapter(requireContext(), populateDummyData(), home_recycler_view)

    }

    private fun populateDummyData(): List<HomeData>{
        val dummyData: MutableList<HomeData> = mutableListOf()

        for(i in 1..10){
            val homeData = HomeData("Title: $i", "Description: $i", R.drawable.analysis)
            dummyData.add(homeData)
        }
        return dummyData
    }

}