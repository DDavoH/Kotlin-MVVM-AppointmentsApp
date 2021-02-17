package com.davoh.laravelmyappointments.data.model

data class User (
        val id: Int,
        val name: String,
        val email: String,
        val dni:String,
        val address: String,
        val phone: String,
        val role:String
        )