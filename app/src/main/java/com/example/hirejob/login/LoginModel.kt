package com.example.hirejob.login

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginModel (
    val id: String?,
    val name: String?,
    val email: String?,
    val token: String?
): Parcelable