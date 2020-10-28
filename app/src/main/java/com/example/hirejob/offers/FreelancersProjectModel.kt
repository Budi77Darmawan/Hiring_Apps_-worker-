package com.example.hirejob.offers

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
data class FreelancersProjectModel(
    val id_project: String?,
    val name_freelancers: String?,
    val image_freelancers: String?,
    val job_freelancers: String?,
): Parcelable