package com.davoh.laravelmyappointments.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Doctor(
    @ColumnInfo(name = "idDoctor") val id:Int,
    @ColumnInfo(name = "nameDoctor") val name:String
):Parcelable {

    override fun toString(): String {
        return name
    }
}
