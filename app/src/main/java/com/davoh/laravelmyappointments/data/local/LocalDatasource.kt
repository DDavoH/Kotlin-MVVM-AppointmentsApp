package com.davoh.laravelmyappointments.data.local

import com.davoh.laravelmyappointments.core.Resource
import com.davoh.laravelmyappointments.data.model.Appointment
import com.davoh.laravelmyappointments.data.model.AppointmentEntity
import com.davoh.laravelmyappointments.data.model.asAppointmentList
import javax.inject.Inject


class LocalDatasource @Inject constructor(private val appointmentDao: AppointmentDao){

    suspend fun saveAppointment(appointment: AppointmentEntity) {
        appointmentDao.saveAppointment(appointment)
    }

    suspend fun getCachedAppointments(): Resource<List<Appointment>> {
        return Resource.Success(appointmentDao.getAppointments().asAppointmentList())
    }

    suspend fun deleteCachedAppointments(){
        appointmentDao.deleteAppointments()
    }

}