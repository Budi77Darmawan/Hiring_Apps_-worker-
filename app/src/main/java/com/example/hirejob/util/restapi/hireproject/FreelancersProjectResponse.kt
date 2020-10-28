package com.example.hirejob.util.restapi.hireproject

import com.google.gson.annotations.SerializedName

class FreelancersProjectResponse(val success: Boolean, val message: String?, val data: List<HireProject>?) {

    data class HireProject (
        @SerializedName("id_project") val id_project: String?,
        @SerializedName("name_freelancers") val nameFree: String?,
        @SerializedName("image_freelancers") val imageFree: String?,
        @SerializedName("job_freelancers") val jobFree: String?
    )
}