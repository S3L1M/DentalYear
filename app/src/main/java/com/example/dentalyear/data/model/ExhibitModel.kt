package com.example.dentalyear.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ExhibitModel(
    var id: Int,
    var date: String,
    var slug: String,
    var link: String,
    @SerializedName("sponsor_name")
    var sponsorName: String,
    @SerializedName("sponsor_message")
    var sponsorMessage: String,
    @SerializedName("sponsor_link")
    var sponsorLink: String,
    var acf: ExhibitAcf
) : Parcelable

@Parcelize
data class ExhibitAcf(@SerializedName("sponsor_logo") var sponsorLogo: String) : Parcelable