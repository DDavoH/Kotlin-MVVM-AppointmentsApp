package com.davoh.laravelmyappointments.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Specialty(
    val id:Int, val name:String
):Parcelable{
    override fun toString(): String {
        return name
    }
}
