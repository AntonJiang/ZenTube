package com.tohacking.distractionfreeyoutube.repository.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GoogleUserInfo(
    val email: String = "",
    val family_name: String = "",
    val gender: String = "",
    val given_name: String = "",
    val hd: String = "",
    val id: String = "",
    val link: String = "",
    val locale: String = "",
    val name: String = "",
    val picture: String = "",
    @Json(name = "verified_email")
    val verifiedEmail: Boolean = false
) : Parcelable