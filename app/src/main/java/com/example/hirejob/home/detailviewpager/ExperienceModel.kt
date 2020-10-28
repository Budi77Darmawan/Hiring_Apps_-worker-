package com.example.hirejob.home.detailviewpager

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ExperienceModel (
    var id_exp: String = "",
    var id_account: String = "",
    var company: String = "",
    var job: String = "",
    var start: String = "",
    var end: String = "",
    var description: String = ""
) : Parcelable