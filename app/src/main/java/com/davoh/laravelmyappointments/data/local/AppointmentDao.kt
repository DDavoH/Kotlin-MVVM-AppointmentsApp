package com.davoh.laravelmyappointments.data.local

import androidx.room.*
import com.davoh.laravelmyappointments.data.model.AppointmentEntity

@Dao
interface AppointmentDao {

    @Query("SELECT * FROM appointmentTable")
    suspend fun getAppointments(): List<AppointmentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAppointment(cocktail: AppointmentEntity)

    @Query("DELETE FROM appointmentTable")
    suspend fun deleteAppointments()

}