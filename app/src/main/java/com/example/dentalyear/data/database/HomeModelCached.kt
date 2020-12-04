package com.example.dentalyear.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.dentalyear.data.model.HomeAcf
import com.example.dentalyear.data.model.HomeModel

@Entity(tableName = "home_data_table")
data class HomeModelCached(
    @PrimaryKey
    var id: Int,
    var link: String,
    var promptCountry: String,
    var promptDate: String,
    var todaysFunHolidayTitle: String,
    var howToCelebrateDesc: String,
    var dailyMarketingTipDesc: String,
    var dailyPostsDesc: String,
    var howToMaximizePostDesc: String,
    var weeklyMarketingExercisesDesc: String,
    var marketingTrendsAndNewsForTheDayDesc: String,
    var adOfTheMonthDesc: String,
    var thisDateInHistoryDesc: String,
    var industryEventsDesc: String,
    var lookingAheadDesc: String
) {
    fun getAcf(): HomeAcf {
        return HomeAcf(
            howToCelebrateDesc = howToCelebrateDesc,
            dailyMarketingTipDesc = dailyMarketingTipDesc,
            dailyPostsDesc = dailyPostsDesc,
            howToMaximizePostDesc = howToMaximizePostDesc,
            weeklyMarketingExercisesDesc = weeklyMarketingExercisesDesc,
            marketingTrendsAndNewsForTheDayDesc = marketingTrendsAndNewsForTheDayDesc,
            adOfTheMonthDesc = adOfTheMonthDesc,
            thisDateInHistoryDesc = thisDateInHistoryDesc,
            industryEventsDesc = industryEventsDesc,
            lookingAheadDesc = lookingAheadDesc,
        )
    }
}


fun List<HomeModel>.asDatabaseModel(): List<HomeModelCached> {
    return map {
        HomeModelCached(
            it.id,
            it.link,
            it.promptCountry,
            it.promptDate,
            it.todaysFunHolidayTitle,
            it.acf.howToCelebrateDesc,
            it.acf.dailyMarketingTipDesc,
            it.acf.dailyPostsDesc,
            it.acf.howToMaximizePostDesc,
            it.acf.weeklyMarketingExercisesDesc,
            it.acf.marketingTrendsAndNewsForTheDayDesc,
            it.acf.adOfTheMonthDesc,
            it.acf.thisDateInHistoryDesc,
            it.acf.industryEventsDesc,
            it.acf.lookingAheadDesc
        )
    }
}

fun List<HomeModelCached>.asDataModel(): List<HomeModel> {
    return map {
        HomeModel(
            it.id,
            it.link,
            it.promptCountry,
            it.promptDate,
            it.todaysFunHolidayTitle,
            it.getAcf(),
        )
    }
}