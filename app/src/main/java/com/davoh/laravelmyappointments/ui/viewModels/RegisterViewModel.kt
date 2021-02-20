package com.davoh.laravelmyappointments.ui.viewModels

import androidx.lifecycle.*
import com.davoh.laravelmyappointments.core.Resource
import com.davoh.laravelmyappointments.data.LaravelRepository
import com.davoh.laravelmyappointments.io.response.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repository: LaravelRepository): ViewModel() {

    fun register(email:String,name:String,password:String,passwordConfirmation: String) =
        liveData<Resource<LoginResponse>>(viewModelScope.coroutineContext + Dispatchers.IO){
            emit(Resource.Loading())
            try{
                val response = repository.register(email, name, password, passwordConfirmation).asLiveData()
                emitSource(response)
            }catch (e: Exception){
                emit(Resource.Failure(e))
            }
        }

}