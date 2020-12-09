package com.example.dentalyear.utils

import java.text.SimpleDateFormat
import java.util.*

interface Utility {
    companion object {
        const val BASE_URL = "https://dentalyear.com/wp-json/wp/v2/"
        const val SPONSOR_LINK = "https://dentalyear.com/sponsors/"
        const val UNITED_STATES = "united states"
        const val AUSTRALIA = "australia"
        const val CANADA = "canada"
        const val NOT_DOWNLOADED = 0
        const val DOWNLOADING = 1
        const val DOWNLOADED = 2
        const val NOTE_NOTES = 0
        const val NOTE_GOALS = 1
        const val NOTE_EDITING_MODE = 0
        const val NOTE_CREATING_MODE = 1
        fun formatDate(date: Date, format: String = "yyyyMMdd"): String {
            return SimpleDateFormat(format).format(date)
        }
    }
}