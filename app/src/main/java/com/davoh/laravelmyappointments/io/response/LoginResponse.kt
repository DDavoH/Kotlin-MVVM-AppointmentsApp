package com.davoh.laravelmyappointments.io.response

import com.davoh.laravelmyappointments.data.model.User

data class LoginResponse (
        val success:Boolean,
        val user: User,
        val accessToken: String
        )