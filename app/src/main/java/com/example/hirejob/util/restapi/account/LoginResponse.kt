package com.example.hirejob.util.restapi.account

import com.google.gson.annotations.SerializedName

class LoginResponse(val success: Boolean, val message: String?, val data: DataResult?) {

    data class DataResult (
        @SerializedName("idAccount") val id: String?,
        @SerializedName("name") val name: String?,
        @SerializedName("roleAccount") val role: String?,
        @SerializedName("email") val email: String?,
        @SerializedName("status") val status: String?,
        @SerializedName("token") val token: String?
    )
}