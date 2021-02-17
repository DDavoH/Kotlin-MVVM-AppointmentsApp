package com.davoh.laravelmyappointments.model

import com.google.gson.annotations.SerializedName

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
data class Appointment (
        val id: Int,
        val description: String,
        val type:String,
        val status: String,

        @SerializedName("scheduled_date") val scheduledDate: String,
        @SerializedName("scheduled_time_12") val scheduledTime: String,
        @SerializedName("created_at_in_millis") val createdAt: Long,
        val specialty:Specialty,
        val doctor:Doctor
)