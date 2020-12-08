package com.example.dentalyear.utils

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
    }
}