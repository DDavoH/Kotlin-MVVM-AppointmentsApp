package com.davoh.laravelmyappointments.api

import androidx.lifecycle.MutableLiveData
import com.davoh.laravelmyappointments.core.Resource
import com.davoh.laravelmyappointments.io.response.LoginResponse
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await

@ExperimentalCoroutinesApi
class NetworkDataSource @Inject constructor(
    private val laravelApiService: LaravelApiService
) {

     fun postLoginn(email: String, password: String): Flow<Resource<LoginResponse>> =
        callbackFlow {
            offer(
                Resource.Success(laravelApiService.postLogin(email, password).await())
            )
            awaitClose { close() }
        }

}