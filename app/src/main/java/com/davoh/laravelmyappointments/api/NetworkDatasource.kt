package com.davoh.laravelmyappointments.api


import com.davoh.laravelmyappointments.core.Resource
import com.davoh.laravelmyappointments.data.model.Appointment
import com.davoh.laravelmyappointments.data.model.Doctor
import com.davoh.laravelmyappointments.data.model.Schedule
import com.davoh.laravelmyappointments.data.model.Specialty
import com.davoh.laravelmyappointments.io.response.LoginResponse
import com.davoh.laravelmyappointments.io.response.SimpleResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import retrofit2.*
import retrofit2.http.Header
import retrofit2.http.Query

@ExperimentalCoroutinesApi
class NetworkDataSource @Inject constructor(
    private val laravelApiService: LaravelApiService
) {

     fun postLogin(email: String, password: String): Flow<Resource<LoginResponse>> =
        callbackFlow<Resource<LoginResponse>>{
            try {
                offer(
                    Resource.Success(laravelApiService.postLogin(email, password).await())
                )
            } catch (e: Exception) {
                offer(Resource.Failure(e))
            }
            awaitClose { close() }
        }

    fun postToken(authHeader:String, deviceToken:String?): Flow<Resource<SimpleResponse>> =
        callbackFlow<Resource<SimpleResponse>> {
            try{
                offer(Resource.Success(laravelApiService.postToken(authHeader,deviceToken).await()))
            }catch (e: Exception){
                offer(Resource.Failure(e))
            }
            awaitClose { close() }
        }

    fun postLogout(authHeader:String): Flow<Resource<SimpleResponse>> =
        callbackFlow<Resource<SimpleResponse>>{
            try {
                offer(Resource.Success(laravelApiService.postLogout(authHeader).await()))
            }catch (e: Exception){
                offer(Resource.Failure(e))
            }
            awaitClose { close() }
        }

    fun getAppointments(authHeader:String): Flow<Resource<ArrayList<Appointment>>> =
        callbackFlow<Resource<ArrayList<Appointment>>>{
            try{
                offer(Resource.Success(laravelApiService.getAppointments(authHeader).await()))
            }catch (e:Exception){
                offer(Resource.Failure(e))
            }
            awaitClose { close() }
        }

    fun getSpecialties(): Flow<Resource<ArrayList<Specialty>>> =
        callbackFlow<Resource<ArrayList<Specialty>>> {
            try{
                offer(Resource.Success(laravelApiService.getSpecialties().await()))
            }catch (e: Exception){
                offer(Resource.Failure(e))
            }
            awaitClose { close() }
        }

    fun getDoctors(specialtyId: Int): Flow<Resource<ArrayList<Doctor>>> =
        callbackFlow<Resource<ArrayList<Doctor>>> {
            try {
                offer(Resource.Success(laravelApiService.getDoctors(specialtyId).await()))
            }catch (e: Exception){
                offer(Resource.Failure(e))
            }
            awaitClose{ close() }
        }

    fun getHours(doctorId: Int, date:String) : Flow<Resource<Schedule>> =
        callbackFlow<Resource<Schedule>>{
            try{
                offer(Resource.Success(laravelApiService.getHours(doctorId, date).await()))
            }catch (e: Exception){
                offer(Resource.Failure(e))
            }
            awaitClose { close() }
        }

    fun storeAppointment(authHeader:String,
                         description:String,
                         specialtyId:Int,
                         doctorId:Int,
                         scheduledDate:String,
                         scheduledTime:String,
                         type:String): Flow<Resource<SimpleResponse>> =
        callbackFlow<Resource<SimpleResponse>>{
            try{
                offer(
                    Resource.Success(laravelApiService.storeAppointment(
                        authHeader,
                        description,
                        specialtyId,
                        doctorId,
                        scheduledDate,
                        scheduledTime,
                        type).await())
                )
            }catch (e: Exception){
                offer(Resource.Failure(e))
            }
            awaitClose { close() }
        }

    fun register(email:String,name:String,password:String,passwordConfirmation: String): Flow<Resource<LoginResponse>> =
        callbackFlow<Resource<LoginResponse>>{
            try{
                offer(Resource.Success(laravelApiService.register(email, name, password, passwordConfirmation).await()))
            }catch (e: Exception){
                offer(Resource.Failure(e))
            }
            awaitClose { close() }
        }

}