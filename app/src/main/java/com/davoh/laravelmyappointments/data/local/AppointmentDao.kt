package com.davoh.laravelmyappointments.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.davoh.laravelmyappointments.data.model.AppointmentEntity

@Dao
interface AppointmentDao {

    @Query("SELECT * FROM appointmentTable")
    suspend fun getAppointments(): List<AppointmentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAppointment(cocktail: AppointmentEntity)

}