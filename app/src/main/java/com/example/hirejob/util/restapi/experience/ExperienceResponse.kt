package com.example.hirejob.util.restapi.experience

import com.google.gson.annotations.SerializedName

class ExperienceResponse (val success: Boolean, val message: String?, val data: List<Exp>?) {

    data class Exp (
        @SerializedName("id_exp") val id_exp: String?,
        @SerializedName("id_account") val id_account: String?,
        @SerializedName("companyName") val company_name: String?,
        @SerializedName("position") val position: String?,
        @SerializedName("start") val start: String?,
        @SerializedName("end") val end: String?,
        @SerializedName("description") val description: String?
    )
}