package com.davoh.laravelmyappointments.ui.viewModels

import androidx.lifecycle.*
import com.davoh.laravelmyappointments.core.Resource
import com.davoh.laravelmyappointments.data.LaravelRepository
import com.davoh.laravelmyappointments.data.model.Appointment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class AppointmentsViewModel @Inject constructor(private val repository: LaravelRepository): ViewModel() {

    fun getAppointments(authHeader: String): LiveData<Resource<ArrayList<Appointment>>> =
        liveData<Resource<ArrayList<Appointment>>>(viewModelScope.coroutineContext + Dispatchers.IO){
            emit(Resource.Loading())
            try {
                val response = repository.getAppointments(authHeader).asLiveData()
                emitSource(response)
            } catch (e: Exception) {
                emit(Resource.Failure(e))
            }
        }

}