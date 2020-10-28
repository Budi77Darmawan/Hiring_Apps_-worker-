package com.example.hirejob.util.restapi.portfolio

import com.google.gson.annotations.SerializedName

class UpdatePortfolioResponse(val success: Boolean, val message: String?, val data: Exp?) {
    data class Exp (
        @SerializedName("name") val name: String?,
        @SerializedName("image") val image: String?,
        @SerializedName("linkRepo") val linkRepo: String?,
        @SerializedName("typePorto") val typePorto: String?,
        @SerializedName("description") val description: String?
    )
}