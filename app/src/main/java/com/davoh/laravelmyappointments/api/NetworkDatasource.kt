package com.davoh.laravelmyappointments.api


import com.davoh.laravelmyappointments.core.Resource
import com.davoh.laravelmyappointments.data.model.Appointment
import com.davoh.laravelmyappointments.data.model.Doctor
import com.davoh.laravelmyappointments.data.model.Schedule
import com.davoh.laravelmyappointments.data.model.Specialty
import com.davoh.laravelmyappointments.io.body.StoreAppointment
import com.davoh.laravelmyappointments.io.response.LoginResponse
import com.davoh.laravelmyappointments.io.response.SimpleResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import retrofit2.*


@ExperimentalCoroutinesApi
class NetworkDataSource @Inject constructor(
    private val laravelApiService: LaravelApiService
) {

     fun postLogin(email: String, password: String) =
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

    fun postToken(authHeader:String, deviceToken:String?) =
        callbackFlow<Resource<SimpleResponse>> {
            try{
                offer(Resource.Success(laravelApiService.postToken(authHeader,deviceToken).await()))
            }catch (e: Exception){
                offer(Resource.Failure(e))
            }
            awaitClose { close() }
        }

    fun postLogout(authHeader:String) =
        callbackFlow<Resource<SimpleResponse>>{
            try {
                offer(Resource.Success(laravelApiService.postLogout(authHeader).await()))
            }catch (e: Exception){
                offer(Resource.Failure(e))
            }
            awaitClose { close() }
        }

   fun getAppointments(authHeader:String) =
        callbackFlow<Resource<List<Appointment>>>{
            try{
                offer(Resource.Success(laravelApiService.getAppointments(authHeader).await()))
            }catch (e:Exception){
                offer(Resource.Failure(e))
            }
            awaitClose { close() }
        }

    fun getSpecialties() =
        callbackFlow<Resource<ArrayList<Specialty>>> {
            try{
                offer(Resource.Success(laravelApiService.getSpecialties().await()))
            }catch (e: Exception){
                offer(Resource.Failure(e))
            }
            awaitClose { close() }
        }

    fun getDoctors(specialtyId: Int) =
        callbackFlow<Resource<ArrayList<Doctor>>> {
            try {
                offer(Resource.Success(laravelApiService.getDoctors(specialtyId).await()))
            }catch (e: Exception){
                offer(Resource.Failure(e))
            }
            awaitClose{ close() }
        }

    fun getHours(doctorId: Int, date:String) =
        callbackFlow<Resource<Schedule>>{
            try{
                offer(Resource.Success(laravelApiService.getHours(doctorId, date).await()))
            }catch (e: Exception){
                offer(Resource.Failure(e))
            }
            awaitClose { close() }
        }

    fun storeAppointment(authHeader:String, storeAppointment: StoreAppointment) =
        callbackFlow<Resource<SimpleResponse>>{
            try{
                offer(Resource.Success(laravelApiService.storeAppointment(authHeader, storeAppointment).await()))
            }catch (e: Exception){
                offer(Resource.Failure(e))
            }
            awaitClose { close() }
        }

    fun register(email:String,name:String,password:String,passwordConfirmation: String) =
        callbackFlow<Resource<LoginResponse>>{
            try{
                offer(Resource.Success(laravelApiService.register(email, name, password, passwordConfirmation).await()))
            }catch (e: Exception){
                offer(Resource.Failure(e))
            }
            awaitClose { close() }
        }

}