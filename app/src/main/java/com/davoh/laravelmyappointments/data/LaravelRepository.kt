package com.davoh.laravelmyappointments.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.davoh.laravelmyappointments.api.LaravelApiService
import com.davoh.laravelmyappointments.api.NetworkDataSource
import com.davoh.laravelmyappointments.core.Resource
import com.davoh.laravelmyappointments.io.response.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class LaravelRepository @Inject constructor(private val networkDataSource: NetworkDataSource)  {

      fun postLogin(email: String, password: String) = networkDataSource.postLogin(email, password)

      fun postToken(authHeader:String, deviceToken:String?) = networkDataSource.postToken(authHeader, deviceToken)

      fun postLogout(authHeader: String) = networkDataSource.postLogout(authHeader)


}