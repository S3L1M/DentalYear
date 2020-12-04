package com.example.dentalyear.data.model

import com.google.gson.annotations.SerializedName

data class HomeModel(
    var id: Int,
    var link: String,
    @SerializedName("prompt_country")
    var promptCountry: String,
    @SerializedName("prompt_date")
    var promptDate: String,
    @SerializedName("todays_fun_holiday_title")
    var todaysFunHolidayTitle: String,
    var acf: HomeAcf,
)

data class HomeAcf(
    @SerializedName("how_to_celebrate")
    var howToCelebrateDesc: String,
    @SerializedName("daily_marketing_tip")
    var dailyMarketingTipDesc: String,
    @SerializedName("daily_post")
    var dailyPostsDesc: String,
    @SerializedName("how_to_maximize_post")
    var howToMaximizePostDesc: String,
    @SerializedName("weekly_marketing_exercises")
    var weeklyMarketingExercisesDesc: String,
    @SerializedName("marketing_trends_&_news_for_the_day")
    var marketingTrendsAndNewsForTheDayDesc: String,
    @SerializedName("this_date_in_history")
    var thisDateInHistoryDesc: String,
    @SerializedName("industry_events")
    var industryEventsDesc: String,
    @SerializedName("looking_ahead")
    var lookingAheadDesc: String,
    @SerializedName("share_with_team_member")
    var adOfTheMonthDesc: String,
)
