package com.example.hirejob.util.restapi.account

import com.google.gson.annotations.SerializedName

class RegisterResponse(val success: Boolean, val message: String?, val data: DataResult?) {

    data class DataResult (
        @SerializedName("name") val name: String?,
        @SerializedName("email") val email: String?
    )
}