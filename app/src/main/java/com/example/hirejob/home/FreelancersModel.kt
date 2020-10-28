package com.example.hirejob.home

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FreelancersModel (
        val id_account: String?,
        var name: String?,
        val jobDesc: String?,
        val statusJob: String?,
        val cityAddress: String?,
        val description: String?,
        val image: String?,
        val skill: String?,
        val numberPhone: String?,
        val email: String?
): Parcelable