package com.davoh.laravelmyappointments.io.response

import com.davoh.laravelmyappointments.data.model.User
import com.google.gson.annotations.SerializedName

data class LoginResponse (
        @field:SerializedName("success") val success:Boolean,
        @field:SerializedName("user") val user: User,
        @field:SerializedName("accessToken") val accessToken: String
        )