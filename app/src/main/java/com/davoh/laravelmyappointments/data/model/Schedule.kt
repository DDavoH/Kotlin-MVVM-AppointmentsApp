package com.davoh.laravelmyappointments.data.model

data class Schedule(
    val morning:ArrayList<HourInterval>, val afternoon:ArrayList<HourInterval>
)

