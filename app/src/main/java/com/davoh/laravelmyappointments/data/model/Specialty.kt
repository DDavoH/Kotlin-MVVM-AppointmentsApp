package com.davoh.laravelmyappointments.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Specialty(
    @ColumnInfo(name = "idSpecialty") val id:Int,
    @ColumnInfo(name = "nameSpecialty") val name:String
):Parcelable{
    override fun toString(): String {
        return name
    }
}
