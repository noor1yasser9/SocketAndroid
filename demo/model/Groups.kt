package com.nurbk.ps.demochat.demo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Groups(
    var id: String = "",
    var name: String = "",
    var usersId: ArrayList<String>
):Parcelable