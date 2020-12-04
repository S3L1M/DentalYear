package com.example.dentalyear.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.dentalyear.R
import com.example.dentalyear.data.model.HomeModel
import com.example.dentalyear.utils.Status
import com.example.dentalyear.utils.Utility
import com.example.dentalyear.view.adapter.HomeAdapter
import com.example.dentalyear.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var adapter: HomeAdapter
    private var usaPrompt: HomeModel? = null
    private var australiaPrompt: HomeModel? = null
    private var canadaPrompt: HomeModel? = null


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
        initRecyclerViewAdapter()

        // change view visibility | By default Invisible
        changeViewVisibility(View.INVISIBLE)

        // Define viewModel
        val viewModel: MainViewModel by activityViewModels()


        /*
        * Init Listeners
         */
        // prompts listener
        val simpleDateFormat = SimpleDateFormat("yyyyMMdd").format(Date())
        viewModel.getPrompts(simpleDateFormat)?.observe(viewLifecycleOwner, { prompts ->
            when (prompts.status) {
                Status.LOADING -> {
                    home_fragment_progress_bar.visibility = View.VISIBLE
                    Log.d("HomeFragment", "Loading")
                }
                Status.SUCCESS -> {
                    prepareData(prompts.data!!)
                    // change visibility to visible
                    home_fragment_progress_bar.visibility = View.INVISIBLE
                    changeViewVisibility(View.VISIBLE)
                    // Set adapter based on the country
                    adapter.setData(usaPrompt!!)
                }
                Status.ERROR -> {
                    Log.d("HomeFragment", "Error: ${prompts.message}")
                    home_fragment_progress_bar.visibility = View.INVISIBLE
                }
            }

        })


    }

    private fun initRecyclerViewAdapter() {
        adapter = HomeAdapter(requireContext())
        home_recycler_view.adapter = adapter
    }

    private fun changeViewVisibility(visibility: Int){
        home_recycler_view.visibility = visibility
        home_fragment_title_logo.visibility = visibility
        home_fragment_dots_image_view.visibility = visibility
        home_fragmnet_dental_logo.visibility = visibility
    }

    private fun prepareData(prompts: List<HomeModel>){
        for(prompt in prompts){
            when(prompt.promptCountry.toLowerCase(Locale.ROOT)) {
                Utility.UNITED_STATES ->{
                    usaPrompt = prompt
                }
                Utility.AUSTRALIA->{
                    australiaPrompt = prompt
                }
                Utility.CANADA->{
                    canadaPrompt = prompt
                }

            }
        }
    }


}