package com.davoh.laravelmyappointments.api


import com.davoh.laravelmyappointments.core.Resource
import com.davoh.laravelmyappointments.data.model.Appointment
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

}