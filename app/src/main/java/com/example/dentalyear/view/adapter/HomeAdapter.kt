package com.example.dentalyear.view.adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.recyclerview.widget.RecyclerView
import com.example.dentalyear.R
import com.example.dentalyear.data.model.HomeData
import net.cachapa.expandablelayout.ExpandableLayout

class HomeAdapter(
    private val context: Context,
    private val data: List<HomeData>?,
    private val recyclerView: RecyclerView
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TOP_BAR_SECTION = 0
        private const val DEFAULT_SECTION = 1
        private const val BOTTOM_BAR_SECTION = 2
    }

    private var openedItems: MutableMap<Int, Boolean> = mutableMapOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TOP_BAR_SECTION -> {
                TopBarSectionViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.recycler_view_main_top_list_item, parent, false)
                )
            }
            BOTTOM_BAR_SECTION -> {
                BottomBarSectionViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.recycler_view_main_bottom_list_item, parent, false)
                )
            }
            else -> {
                MainViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.recycler_view_main_list_item, parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            DEFAULT_SECTION -> bindDefaultSection(holder as MainViewHolder, position)
        }
    }

    private fun bindDefaultSection(holder: MainViewHolder, position: Int) {
        val itemColor: Int = chooseBackgroundColor(position)
        val itemBackground: Int = chooseBackgroundColor(position + 1)
        val shapeDrawable = holder.itemContainer[0].background as GradientDrawable
        shapeDrawable.color = ContextCompat.getColorStateList(context, itemColor)
        holder.itemContainer.setBackgroundColor(context.resources.getColor(itemBackground))

        if (position <= (data?.size) as Int) {
            // Set item title
            holder.titleTextView.text = "${data?.get(position-1)?.title}"
            // Set item description (Expanded Text)
            holder.descriptionTextView.text = "${data?.get(position-1)?.description}"
            holder.expandableLayout.setInterpolator(OvershootInterpolator())
            // Check for expansion state | scroll smoothly to clicked item
            holder.expandableLayout.setOnExpansionUpdateListener { _, state ->
                run {
                    if (state == ExpandableLayout.State.EXPANDING)
                        recyclerView.smoothScrollToPosition(position-1)
                }
            }
        }

        // Handle arrow click (Item click)
        holder.arrowExpandImageView.setOnClickListener {
            if (openedItems[position] != null) {
                holder.expandableLayout.collapse()
                openedItems.remove(position)
            } else {
                holder.expandableLayout.expand()
                openedItems[position] = true
            }
        }
    }

    private fun chooseBackgroundColor(position: Int): Int {
        return when (position) {
            1 -> R.color.colorHowToCelebrate
            2 -> R.color.colorDailyMarketingWeb
            3 -> R.color.colorDailyPosts
            4 -> R.color.colorHowToMaximizePost
            5 -> R.color.colorWeeklyMarketingExercise
            6 -> R.color.colorMarketingTrendsAndNews
            7 -> R.color.colorAdOfTheMonth
            8 -> R.color.colorThisDateInHistory
            9 -> R.color.colorIndustryEvents
            10 -> R.color.colorLookingAhead
            else -> R.color.colorAppBackground
        }
    }

    private fun bindTopBarSection(holder: TopBarSectionViewHolder) {

    }

    override fun getItemCount() = (data?.size) as Int + 2

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TOP_BAR_SECTION
            11 -> BOTTOM_BAR_SECTION
            else -> DEFAULT_SECTION
        }
    }

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemContainer: ConstraintLayout =
            itemView.findViewById(R.id.home_recycler_view_item_container)

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

    class TopBarSectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class BottomBarSectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}