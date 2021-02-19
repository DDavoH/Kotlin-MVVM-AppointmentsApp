package com.davoh.laravelmyappointments.api

import com.davoh.laravelmyappointments.io.response.LoginResponse
import com.davoh.laravelmyappointments.io.response.SimpleResponse
import com.davoh.laravelmyappointments.data.model.Appointment
import com.davoh.laravelmyappointments.data.model.Doctor
import com.davoh.laravelmyappointments.data.model.Schedule
import com.davoh.laravelmyappointments.data.model.Specialty
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface LaravelApiService {

    @GET("specialties")
    fun getSpecialties(): Call<ArrayList<Specialty>>

    @GET("specialties/{specialty}/doctors")
    fun getDoctors(@Path("specialty") specialtyId: Int) : Call<ArrayList<Doctor>>

    @GET("schedule/hours")
    fun getHours(@Query("doctor_id") doctorId:Int,
                 @Query("date") date:String) : Call<Schedule>

    @POST("login")
    fun postLogin(@Query("email") email:String,
                  @Query("password") password:String) : Call<LoginResponse>

    @POST("logout")
    fun postLogout(@Header("Authorization") authHeader:String) : Call<SimpleResponse>

    @GET("appointments")
    fun getAppointments(@Header("Authorization") authHeader:String) : Call<ArrayList<Appointment>>

    @POST("appointments")
    @Headers("Accept: application/json")
    fun storeAppointments(
        @Header("Authorization") authHeader:String,
        @Query("description") description:String,
        @Query("specialty_id") specialtyId:Int,
        @Query("doctor_id") doctorId:Int,
        @Query("scheduled_date") scheduledDate:String,
        @Query("scheduled_time") scheduledTime:String,
        @Query("type") type:String,
    ) : Call<SimpleResponse>

    @POST("register")
    @Headers("Accept: application/json")
    fun register(
        @Query("email") email:String,
        @Query("name") name:String,
        @Query("password") password:String,
        @Query("password_confirmation") passwordConfirmation: String
    ): Call<LoginResponse>

    @POST("fcm/token")
    @Headers("Accept: application/json") //Esto es para que no devuelva el html entero
    fun postToken(
        @Header("Authorization") authHeader:String,
        @Query("device_token") deviceToken:String?
    ): Call<SimpleResponse>

    companion object {
        private const val BASE_URL = "http://alexadavoh.servehttp.com/api/"

        fun create(): LaravelApiService {
            val logger = HttpLoggingInterceptor().apply { level = Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LaravelApiService::class.java)
        }
    }
}