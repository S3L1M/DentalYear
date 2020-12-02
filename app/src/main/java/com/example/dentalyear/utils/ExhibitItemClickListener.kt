package com.example.dentalyear.utils

import com.example.dentalyear.data.model.ExhibitModel
import com.example.dentalyear.view.adapter.ExhibitAdapter

interface ExhibitItemClickListener {
    fun onExhibitItemClicked(holder: ExhibitAdapter.ExhibitViewHolder, data: ExhibitModel)
}