package com.example.dentalyear.view.adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.example.dentalyear.R
import com.example.dentalyear.data.model.HomeModel
import com.example.dentalyear.utils.HomeTopBarClickListener
import com.example.dentalyear.utils.Utility
import net.cachapa.expandablelayout.ExpandableLayout

class HomeAdapter(
    private val context: Context,
    private val homeTopBarClickListener: HomeTopBarClickListener,
    private var data: HomeModel? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var dayDate: String = ""
    private var monthDayDate: String = ""
    private var TYPE: String = ""


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
                        .inflate(R.layout.recycler_view_home_top_list_item, parent, false)
                )
            }
            BOTTOM_BAR_SECTION -> {
                BottomBarSectionViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.recycler_view_home_bottom_list_item, parent, false)
                )
            }
            else -> {
                MainViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.recycler_view_home_list_item, parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            DEFAULT_SECTION -> bindDefaultSection(holder as MainViewHolder, position)
            TOP_BAR_SECTION -> bindTopBarSection(holder as TopBarSectionViewHolder)
        }
    }

    private fun bindDefaultSection(holder: MainViewHolder, position: Int) {
        val itemColor: Int = chooseBackgroundColor(position)
        val itemBackground: Int = chooseBackgroundColor(position + 1)
        val shapeDrawable = holder.itemContainer[0].background as GradientDrawable
        shapeDrawable.color = ContextCompat.getColorStateList(context, itemColor)
        holder.itemContainer.setBackgroundColor(context.resources.getColor(itemBackground))

        if (position <= 10) {
            setItemTitleDescription(holder, position)
            holder.expandableLayout.setInterpolator(OvershootInterpolator())
        }

        // Handle item click
        holder.itemContainer.setOnClickListener {
            if (openedItems[position] != null) {
                holder.expandableLayout.collapse()
                openedItems.remove(position)
                holder.arrowExpandImageView.animation = AnimationUtils.loadAnimation(
                    context,
                    R.anim.rotate_clock_wise_1
                )
            } else {
                holder.expandableLayout.expand()
                openedItems[position] = true
                holder.arrowExpandImageView.animation = AnimationUtils.loadAnimation(
                    context,
                    R.anim.rotate_clock_wise
                )
            }
        }
    }

    private fun setItemTitleDescription(holder: MainViewHolder, position: Int) {
        when (position) {
            1 -> {
                holder.titleTextView.text =
                    holder.itemView.resources.getString(R.string.how_to_celebrate)
                holder.descriptionTextView.text = data?.acf?.howToCelebrateDesc
                holder.promptItemImageView.setImageDrawable(holder.itemView.resources.getDrawable(R.drawable.celebrate))
                holder.expandableItemImage.setImageDrawable(holder.itemView.resources.getDrawable(R.drawable.doctor_male))
            }
            2 -> {
                holder.titleTextView.text =
                    holder.itemView.resources.getString(R.string.daily_marketing_tip)
                holder.descriptionTextView.text = data?.acf?.dailyMarketingTipDesc
                holder.promptItemImageView.setImageDrawable(holder.itemView.resources.getDrawable(R.drawable.idea_lamp))
                holder.expandableItemImage.setImageDrawable(holder.itemView.resources.getDrawable(R.drawable.tree_of_icons))
            }
            3 -> {
                holder.titleTextView.text = holder.itemView.resources.getString(R.string.daily_post)
                holder.descriptionTextView.text = data?.acf?.dailyPostsDesc
                holder.promptItemImageView.setImageDrawable(holder.itemView.resources.getDrawable(R.drawable.paomedia_small_flat_post_it))
                holder.expandableItemImage.setImageDrawable(holder.itemView.resources.getDrawable(R.drawable.doctor_male_2))
            }
            4 -> {
                holder.titleTextView.text =
                    holder.itemView.resources.getString(R.string.how_to_maximize_post)
                holder.descriptionTextView.text = data?.acf?.howToMaximizePostDesc
                holder.promptItemImageView.setImageDrawable(holder.itemView.resources.getDrawable(R.drawable.rocket))
                holder.expandableItemImage.setImageDrawable(holder.itemView.resources.getDrawable(R.drawable.rocket_with_blue_bg))
            }
            5 -> {
                holder.titleTextView.text =
                    holder.itemView.resources.getString(R.string.weekly_marketing_exercises)
                holder.descriptionTextView.text = data?.acf?.weeklyMarketingExercisesDesc
                holder.promptItemImageView.setImageDrawable(holder.itemView.resources.getDrawable(R.drawable.calendar_trans))
                holder.expandableItemImage.setImageDrawable(holder.itemView.resources.getDrawable(R.drawable.doctor_female_2))
            }
            6 -> {
                holder.titleTextView.text =
                    holder.itemView.resources.getString(R.string.marketing_trends_news_for_the_day)
                holder.descriptionTextView.text = data?.acf?.marketingTrendsAndNewsForTheDayDesc
                holder.promptItemImageView.setImageDrawable(holder.itemView.resources.getDrawable(R.drawable.stock))
                holder.expandableItemImage.setImageDrawable(holder.itemView.resources.getDrawable(R.drawable.analysis))
            }
            7 -> {
                holder.titleTextView.text =
                    holder.itemView.resources.getString(R.string.ad_of_the_month)
                holder.descriptionTextView.text = data?.acf?.adOfTheMonthDesc
                holder.promptItemImageView.setImageDrawable(holder.itemView.resources.getDrawable(R.drawable.play_button))
                holder.expandableItemImage.setImageDrawable(holder.itemView.resources.getDrawable(R.drawable.voice))
            }
            8 -> {
                holder.titleTextView.text =
                    holder.itemView.resources.getString(R.string.this_date_in_history)
                holder.descriptionTextView.text = data?.acf?.thisDateInHistoryDesc
                holder.promptItemImageView.setImageDrawable(holder.itemView.resources.getDrawable(R.drawable.list_of_items))
                holder.expandableItemImage.setImageDrawable(holder.itemView.resources.getDrawable(R.drawable.blue_book))
            }
            9 -> {
                holder.titleTextView.text =
                    holder.itemView.resources.getString(R.string.industry_events)
                holder.descriptionTextView.text = data?.acf?.industryEventsDesc
                holder.promptItemImageView.setImageDrawable(holder.itemView.resources.getDrawable(R.drawable.time_and_date))
                holder.expandableItemImage.setImageDrawable(holder.itemView.resources.getDrawable(R.drawable.doctor_female))
            }
            10 -> {
                holder.titleTextView.text =
                    holder.itemView.resources.getString(R.string.looking_ahead)
                holder.descriptionTextView.text = data?.acf?.lookingAheadDesc
                holder.promptItemImageView.setImageDrawable(holder.itemView.resources.getDrawable(R.drawable.arrow_up))
                holder.expandableItemImage.setImageDrawable(holder.itemView.resources.getDrawable(R.drawable.doctor_female_3))
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
        holder.titleTextView.text = data?.todaysFunHolidayTitle
        when (TYPE) {
            Utility.UNITED_STATES -> {
                holder.countryImageView.setImageDrawable(holder.itemView.resources.getDrawable(R.drawable.usa_logo))
            }
            Utility.CANADA -> {
                holder.countryImageView.setImageDrawable(holder.itemView.resources.getDrawable(R.drawable.analysis))
            }
            Utility.AUSTRALIA -> {
                holder.countryImageView.setImageDrawable(holder.itemView.resources.getDrawable(R.drawable.doctor_female_3))
            }
        }
        holder.datePickerImageView.setOnClickListener {
            homeTopBarClickListener.datePickerClicked()
        }
        holder.rightArrowImageView.setOnClickListener {
            holder.leftArrowImageView.visibility = View.VISIBLE
            homeTopBarClickListener.rightArrowClicked(holder.rightArrowImageView)
        }
        holder.leftArrowImageView.setOnClickListener {
            homeTopBarClickListener.leftArrowClicked(holder.leftArrowImageView)
        }
        holder.dayDateTextView.text = dayDate
        holder.monthDayDateTextView.text = monthDayDate
    }

    override fun getItemCount(): Int {
        return if (data == null)
            0
        else
            12
    }

    fun setData(data: HomeModel, TYPE: String) {
        this.data = data
        this.TYPE = TYPE
        notifyDataSetChanged()
    }

    fun setCurrentDate(dayDate: String, monthDayDate: String) {
        this.dayDate = dayDate
        this.monthDayDate = monthDayDate
        notifyDataSetChanged()
    }

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

        val expandableItemImage: ImageView =
            itemView.findViewById(R.id.home_recycler_view_item_list_description_image)

        val expandableLayout: ExpandableLayout =
            itemView.findViewById(R.id.main_recycler_view_expandable_layout)

        val arrowExpandImageView: ImageView =
            itemView.findViewById(R.id.home_recycler_view_item_list_arrow)

        val promptItemImageView: ImageView =
            itemView.findViewById(R.id.home_recycler_view_item_list_image)
    }

    class TopBarSectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView =
            itemView.findViewById(R.id.home_recycler_view_top_item_title_2)

        val datePickerImageView: ImageView =
            itemView.findViewById(R.id.home_recycler_view_top_item_date_picker)

        val rightArrowImageView: ImageView =
            itemView.findViewById(R.id.home_recycler_view_top_item_arrow_right_icon)

        val leftArrowImageView: ImageView =
            itemView.findViewById(R.id.home_recycler_view_top_item_arrow_left_icon)

        val dayDateTextView: TextView =
            itemView.findViewById(R.id.home_recycler_view_top_item_day_date)

        val monthDayDateTextView: TextView =
            itemView.findViewById(R.id.home_recycler_view_top_item_month_day_num_date)

        val countryImageView: ImageView =
            itemView.findViewById(R.id.home_recycler_view_top_item_country_logo)
    }

    class BottomBarSectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}