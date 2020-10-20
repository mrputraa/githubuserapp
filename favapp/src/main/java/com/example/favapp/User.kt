package com.example.favapp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (
    var avatar: String?,
    var username: String?,
    var company: String?,
    var location: String?,
    var repository: String?,
    var follower: String?,
    var following: String?
) : Parcelable