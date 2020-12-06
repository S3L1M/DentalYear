package com.example.dentalyear.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.dentalyear.R
import com.example.dentalyear.data.model.ExhibitModel
import com.example.dentalyear.utils.ExhibitItemClickListener
import com.example.dentalyear.utils.Status
import com.example.dentalyear.view.adapter.ExhibitAdapter
import com.example.dentalyear.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_exhibit.*


@AndroidEntryPoint
class ExhibitFragment : Fragment(), ExhibitItemClickListener {

    companion object {
        const val EXHIBIT_KEY = "exhibit_key"
    }

    private lateinit var adapter: ExhibitAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exhibit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // init view visibility | make it invisible
        changeViewsVisibility(View.INVISIBLE)

        // init RecyclerView
        adapter = ExhibitAdapter(requireContext(), this)
        fragment_exhibit_recycler_view.adapter = adapter

        // Define ViewModel
        val viewModel: MainViewModel by activityViewModels()


        // Get Exhibits
        viewModel.getExhibits()?.observe(viewLifecycleOwner, { exhibits ->
            if (exhibits.status == Status.LOADING) {
                fragment_exhibit_progress_bar.visibility = View.VISIBLE
            } else if (exhibits.status == Status.SUCCESS) {
                fragment_exhibit_progress_bar.visibility = View.INVISIBLE
                changeViewsVisibility(View.VISIBLE)
                adapter.setData(exhibits.data!!)
            }
        })
    }

    private fun changeViewsVisibility(visibility: Int) {
        fragment_exhibit_recycler_view.visibility = visibility
    }

    override fun onExhibitItemClicked(
        holder: ExhibitAdapter.ExhibitViewHolder,
        data: ExhibitModel
    ) {
        Log.d("ExhibitFragment", "Message: ${data.sponsorMessage}")
        val intent = Intent(requireContext(), ExhibitDetailsActivity::class.java)
        intent.putExtra(EXHIBIT_KEY, data)
        startActivity(intent)
    }

}