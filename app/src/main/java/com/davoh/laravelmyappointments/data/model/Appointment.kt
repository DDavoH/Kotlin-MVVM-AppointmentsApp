package com.davoh.laravelmyappointments.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/*
"id": 301,
        "description": "es estudio de la quimica",
        "scheduled_date": "2021-02-04",
        "type": "Consulta",
        "created_at": "2021-01-29T19:30:30.000000Z",
        "status": "Reservada",
        "scheduled_time_12": "7:30 AM",
        "specialty": {
            "id": 3,
            "name": "Neurología"
        },
        "doctor": {
            "id": 2,
            "name": "Dr David Cruz Ramirez"
        }
 */
@Parcelize
data class Appointment (
        @SerializedName("id") val id: Int =0,
        @SerializedName("description") val description: String,
        @SerializedName("type") val type:String,
        @SerializedName("status") val status: String,
        @SerializedName("scheduled_date") val scheduledDate: String,
        @SerializedName("scheduled_time_12") val scheduledTime: String,
        @SerializedName("created_at_in_millis") val createdAt: Long,
        @SerializedName("specialty") val specialty:Specialty,
        @SerializedName("doctor") val doctor:Doctor
): Parcelable

@Entity(tableName = "appointmentTable")
data class AppointmentEntity(
        @PrimaryKey
        val id: String,
        @ColumnInfo(name = "appointment_description")
        val description: String,
        @ColumnInfo(name = "appointment_type")
        val type: String,
        @ColumnInfo(name = "appointment_status")
        val status:  String,
        @ColumnInfo(name = "appointment_scheduled_date")
        val scheduledDate:  String,
        @ColumnInfo(name = "appointment_scheduled_time_12")
        val scheduledTime:  String,
        @ColumnInfo(name = "appointment_created_at_in_millis")
        val createdAt:  Long,
        @ColumnInfo(name = "appointment_specialty")
        val specialty: Specialty,
        @ColumnInfo(name = "appointment_doctor")
        val doctor: Doctor,
)