package com.example.hirejob.util.restapi.account

import com.google.gson.annotations.SerializedName

class UpdateFreelancersResponse(val success: Boolean, val message: String?, val data: Freelancers?) {

    data class Freelancers (
        @SerializedName("jobDesc") val job: String?,
        @SerializedName("statusJob") val status: String?,
        @SerializedName("description") val desc: String?,
        @SerializedName("workPlace") val workPlace : String?,
        @SerializedName("cityAddress") val cityAddress: String?,
        @SerializedName("image") val image: String?
    )
}