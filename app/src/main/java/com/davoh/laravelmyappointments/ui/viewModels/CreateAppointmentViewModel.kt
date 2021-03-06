package com.davoh.laravelmyappointments.ui.viewModels

import androidx.lifecycle.*
import com.davoh.laravelmyappointments.core.Resource
import com.davoh.laravelmyappointments.data.LaravelRepository
import com.davoh.laravelmyappointments.data.model.Doctor
import com.davoh.laravelmyappointments.data.model.Schedule
import com.davoh.laravelmyappointments.data.model.Specialty
import com.davoh.laravelmyappointments.io.body.StoreAppointment
import com.davoh.laravelmyappointments.io.response.SimpleResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class CreateAppointmentViewModel @Inject constructor(private val repository: LaravelRepository): ViewModel(){

    fun getSpecialties() =
        liveData<Resource<ArrayList<Specialty>>>(viewModelScope.coroutineContext + Dispatchers.IO){
            emit(Resource.Loading())
            try {
                val response = repository.getSpecialties().asLiveData()
                emitSource(response)
            }   catch (e: Exception) {
                emit(Resource.Failure(e))
            }
        }

    fun getDoctors(specialtyId: Int) =
        liveData<Resource<ArrayList<Doctor>>>(viewModelScope.coroutineContext + Dispatchers.IO){
            emit(Resource.Loading())
            try{
                val response = repository.getDoctors(specialtyId).asLiveData()
                emitSource(response)
            }catch (e: Exception){
                emit(Resource.Failure(e))
            }
        }

    fun getHours(doctorId: Int, date:String) =
        liveData<Resource<Schedule>>(viewModelScope.coroutineContext + Dispatchers.IO){
            emit(Resource.Loading())
            try{
                val response = repository.getHours(doctorId, date).asLiveData()
                emitSource(response)
            }catch (e: Exception){
                emit(Resource.Failure(e))
            }
        }

    fun storeAppointment(authHeader:String, storeAppointment: StoreAppointment) =
        liveData<Resource<SimpleResponse>>(viewModelScope.coroutineContext + Dispatchers.IO){
            emit(Resource.Loading())
            try{
                val response = repository.storeAppointment(authHeader, storeAppointment).asLiveData()
                emitSource(response)
            }catch (e: Exception){
                emit(Resource.Failure(e))
            }
        }

}