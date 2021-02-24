package com.davoh.laravelmyappointments.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.davoh.laravelmyappointments.data.model.AppointmentEntity

@Database(entities = [AppointmentEntity::class],version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appointmentDao(): AppointmentDao
}