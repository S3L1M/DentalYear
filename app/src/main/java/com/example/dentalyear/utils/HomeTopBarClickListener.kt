package com.example.dentalyear.utils

import android.widget.ImageView

interface HomeTopBarClickListener {
    fun datePickerClicked()

    fun leftArrowClicked(imageView: ImageView)

    fun rightArrowClicked(imageView: ImageView)
}