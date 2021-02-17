package com.davoh.laravelmyappointments.model

data class Schedule(
    val morning:ArrayList<HourInterval>, val afternoon:ArrayList<HourInterval>
)

