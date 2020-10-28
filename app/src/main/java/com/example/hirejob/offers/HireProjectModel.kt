package com.example.hirejob.offers

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HireProjectModel(
    val id_hire: String?,
    val id_project: String?,
    val nameRec: String?,
    val company_name: String?,
    val nameFree: String?,
    val imageFree: String?,
    val imageProject: String?,
    val projectName: String?,
    val projectDeadline: String?,
    val projectDesc: String?,
    val projectJob: String?,
    val message: String?,
    val price: String?,
    val statusConfirm: String?
): Parcelable