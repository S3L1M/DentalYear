package com.example.dentalyear.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dentalyear.R
import com.example.dentalyear.data.model.ExhibitModel
import com.example.dentalyear.utils.ExhibitItemClickListener

class ExhibitAdapter(
    private val context: Context,
    private val itemClick: ExhibitItemClickListener,
    private var data: List<ExhibitModel>? = null
) :
    RecyclerView.Adapter<ExhibitAdapter.ExhibitViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExhibitViewHolder {
        return ExhibitViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_view_exhibits_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ExhibitViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.exhibit_text).text = "Position: $position"
        data?.get(position)?.let { exhibit ->
            Glide.with(context).load(exhibit.acf.sponsorLogo).into(holder.logoImageView)
            holder.itemView.setOnClickListener {
                itemClick.onExhibitItemClicked(holder, exhibit)
            }
        }
    }

    override fun getItemCount() = data?.size ?: 0

    fun setData(data: List<ExhibitModel>) {
        this.data = data
        notifyDataSetChanged()
    }

    class ExhibitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val logoImageView: ImageView =
            itemView.findViewById(R.id.exhibit_recycler_view_logo_image_view)
    }
}