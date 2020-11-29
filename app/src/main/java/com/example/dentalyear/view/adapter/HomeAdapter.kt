package com.example.dentalyear.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.recyclerview.widget.RecyclerView
import com.example.dentalyear.R
import com.example.dentalyear.data.model.HomeData
import net.cachapa.expandablelayout.ExpandableLayout

class HomeAdapter(private val data: List<HomeData>?, private val recyclerView: RecyclerView) :
    RecyclerView.Adapter<HomeAdapter.MainViewHolder>() {

    private var openedItems:MutableMap<Int, Boolean> = mutableMapOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MainViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_view_main_list_item, parent, false)
        )

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        // Set item title
        holder.titleTextView.text = "${data?.get(position)?.title}"
        // Set item description (Expanded Text)
        holder.descriptionTextView.text = "${data?.get(position)?.description}"
        holder.expandableLayout.setInterpolator(OvershootInterpolator())
        // Check for expansion state | scroll smoothly to clicked item
        holder.expandableLayout.setOnExpansionUpdateListener { expansionFraction, state ->
            run {
                if (state == ExpandableLayout.State.EXPANDING)
                    recyclerView.smoothScrollToPosition(position)
            }
        }
        // Handle arrow click (Item click)
        holder.arrowExpandImageView.setOnClickListener {
            if(openedItems[position] != null){
                holder.expandableLayout.collapse()
                openedItems.remove(position)
            }else{
                holder.expandableLayout.expand()
                openedItems[position] = true
            }
        }

    }

    override fun getItemCount() = data?.size ?: 0

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView =
            itemView.findViewById(R.id.home_recycler_view_item_list_title)

        val descriptionTextView: TextView =
            itemView.findViewById(R.id.home_recycler_view_item_list_description)

        val expandableLayout: ExpandableLayout =
            itemView.findViewById(R.id.main_recycler_view_expandable_layout)

        val arrowExpandImageView: ImageView =
            itemView.findViewById(R.id.home_recycler_view_item_list_arrow)

        /**
         * A [SpringAnimation] for this RecyclerView item. This animation is used to bring the item back
         * after the over-scroll effect.
         */
        val translationY: SpringAnimation = SpringAnimation(itemView, SpringAnimation.TRANSLATION_Y)
            .setSpring(
                SpringForce()
                    .setFinalPosition(0f)
                    .setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY)
                    .setStiffness(SpringForce.STIFFNESS_LOW)
            )
    }
}