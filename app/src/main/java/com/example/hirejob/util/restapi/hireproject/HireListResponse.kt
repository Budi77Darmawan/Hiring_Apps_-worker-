package com.example.hirejob.util.restapi.hireproject

import com.google.gson.annotations.SerializedName

class HireListResponse (val success: Boolean, val message: String?, val data: List<HireProject>?) {

    data class HireProject (
        @SerializedName("id_hire") val id_hire: String?,
        @SerializedName("id_project") val id_project: String?,
        @SerializedName("name_recruiter") val nameRec: String?,
        @SerializedName("companyName") val company_name: String?,
        @SerializedName("name_freelancer") val nameFree: String?,
        @SerializedName("image_free") val imageFree: String?,
        @SerializedName("image_project") val imageProject: String?,
        @SerializedName("project_name") val projectName: String?,
        @SerializedName("project_deadline") val projectDeadline: String?,
        @SerializedName("project_description") val projectDesc: String?,
        @SerializedName("projectJob") val projectJob: String?,
        @SerializedName("message") val message: String?,
        @SerializedName("price") val price: String?,
        @SerializedName("statusConfirm") val statusConfirm: String?
    )
}