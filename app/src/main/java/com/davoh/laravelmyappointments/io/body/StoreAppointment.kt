package com.davoh.laravelmyappointments.io.body

import com.google.gson.annotations.SerializedName


data class StoreAppointment (
    @field:SerializedName("description") val description:String,
    @field:SerializedName("specialty_id") val specialtyId:Int,
    @field:SerializedName("doctor_id") val doctorId:Int,
    @field:SerializedName("scheduled_date") val scheduledDate:String,
    @field:SerializedName("scheduled_time") val scheduledTime:String,
    @field:SerializedName("type") val type:String
)