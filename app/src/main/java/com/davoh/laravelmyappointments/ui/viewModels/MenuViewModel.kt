package com.davoh.laravelmyappointments.ui.viewModels

import androidx.lifecycle.*
import com.davoh.laravelmyappointments.core.Resource
import com.davoh.laravelmyappointments.data.LaravelRepository
import com.davoh.laravelmyappointments.io.response.SimpleResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(private val repository: LaravelRepository) : ViewModel(){

    fun postToken(authHeader:String, deviceToken: String?): LiveData<Resource<SimpleResponse>> =
        liveData<Resource<SimpleResponse>>(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(Resource.Loading())
            try {
                val response = repository.postToken(authHeader, deviceToken).asLiveData()
                emitSource(response)
            } catch (e: Exception) {
                emit(Resource.Failure(e))
            }
        }
}