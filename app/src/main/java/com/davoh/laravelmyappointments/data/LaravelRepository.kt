package com.davoh.laravelmyappointments.data

import com.davoh.laravelmyappointments.api.NetworkDataSource
import com.davoh.laravelmyappointments.core.Resource
import com.davoh.laravelmyappointments.data.local.LocalDatasource
import com.davoh.laravelmyappointments.data.model.*
import com.davoh.laravelmyappointments.io.body.StoreAppointment
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject



class LaravelRepository @Inject constructor(private val networkDataSource: NetworkDataSource,
                                            private val localDatasource: LocalDatasource
)  {

      fun postLogin(email: String, password: String) = networkDataSource.postLogin(email, password)

      fun postToken(authHeader:String, deviceToken:String?) = networkDataSource.postToken(authHeader, deviceToken)

      fun postLogout(authHeader: String) = networkDataSource.postLogout(authHeader)

      suspend fun getAppointments(authHeader:String) =
            callbackFlow<Resource<List<Appointment>>>{
                  offer(getCachedAppointments())
                  networkDataSource.getAppointments(authHeader).collect{
                        when (it) {
                              is Resource.Success -> {
                                    for (appointment in it.data) {
                                          saveAppointment(appointment.asAppointmentEntity())
                                    }
                                    offer(getCachedAppointments())
                              }
                              is Resource.Failure -> {
                                    offer(getCachedAppointments())
                              }
                              else ->  offer(getCachedAppointments())
                        }
                  }
                  awaitClose { cancel() }
            }


      fun getSpecialties() = networkDataSource.getSpecialties()

      fun getDoctors(specialtyId: Int) = networkDataSource.getDoctors(specialtyId)

      fun getHours(doctorId: Int, date:String) = networkDataSource.getHours(doctorId,date)

      fun storeAppointment(authHeader:String, storeAppointment: StoreAppointment) =
            networkDataSource.storeAppointment(authHeader, storeAppointment)

     fun register(email:String,name:String,password:String,passwordConfirmation: String) =
         networkDataSource.register(email, name, password, passwordConfirmation)

      private suspend fun getCachedAppointments(): Resource<List<Appointment>> {
            return localDatasource.getCachedAppointments()
      }

      private suspend fun saveAppointment(appointment: AppointmentEntity) {
           return localDatasource.saveAppointment(appointment)
      }

     suspend fun deleteCachedAppointments(){
        return localDatasource.deleteCachedAppointments()
     }
}