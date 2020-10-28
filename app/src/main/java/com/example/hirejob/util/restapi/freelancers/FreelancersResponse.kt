package com.example.hirejob.util.restapi.freelancers

import com.google.gson.annotations.SerializedName

class FreelancersResponse(val success: Boolean, val message: String?, val data: List<DataResult>?) {

    data class DataResult (
        @SerializedName("id_account") val id_freelancer: String?,
        @SerializedName("name") val name: String?,
        @SerializedName("jobDesc") val job: String?,
        @SerializedName("statusJob") val status: String?,
        @SerializedName("cityAddress") val city: String?,
        @SerializedName("description") val description: String?,
        @SerializedName("skill") val skill: String?,
        @SerializedName("image") val image: String?,
        @SerializedName("email") val email: String?,
        @SerializedName("numberPhone") val numberPhone: String?
    )
}