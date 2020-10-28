package com.example.hirejob.home.detailviewpager

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PortfolioModel(
    var id_portfolio: String = "",
    var id_freelancer: String?,
    var name: String?,
    var image: String?,
    var description: String?,
    var link_repo: String?,
    var type_portfolio: String?
): Parcelable