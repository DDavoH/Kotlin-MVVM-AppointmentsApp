package com.davoh.laravelmyappointments.ui.menu.myAppointments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.davoh.laravelmyappointments.R
import com.davoh.laravelmyappointments.adapters.AppointmentAdapter
import com.davoh.laravelmyappointments.api.LaravelApiService
import com.davoh.laravelmyappointments.core.Resource
import com.davoh.laravelmyappointments.databinding.FragmentAppointmentsBinding
import com.davoh.laravelmyappointments.data.model.Appointment
import com.davoh.laravelmyappointments.ui.viewModels.AppointmentsViewModel
import com.davoh.laravelmyappointments.utils.PreferenceHelper
import com.davoh.laravelmyappointments.utils.PreferenceHelper.get
import com.davoh.laravelmyappointments.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class AppointmentsFragment : Fragment() {

    private var _binding: FragmentAppointmentsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AppointmentsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAppointmentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadAppointmentsRetrofit()
    }

    private fun loadAppointmentsRetrofit(){
        val preferences= PreferenceHelper.defaultPrefs(requireContext())
        val accessToken = preferences["accessToken",""]

        viewModel.getAppointments("Bearer $accessToken").observe(viewLifecycleOwner){result->
            when(result){
                is Resource.Loading->{

                }
                is Resource.Success->{
                    val appointments = result.data
                    recyclerViewAppointments(appointments)
                }
                is Resource.Failure->{
                    requireContext().toast("Hubo un error de conexión")
                }
            }
        }
    }

    private fun recyclerViewAppointments(appointments: ArrayList<Appointment>){
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        val adapter = AppointmentAdapter(appointments)
        binding.rv.adapter = adapter

        adapter.setOnItemClickListener(object: AppointmentAdapter.OnItemClickListener{
            override fun onIntemClick(appointment: Appointment) {
                findNavController().navigate(AppointmentsFragmentDirections.actionAppointmentsFragmentToAppointmentDetailsFragment(appointment))
                //findNavController().navigate(R.id.action_appointmentsFragment_to_appointmentDetailsFragment)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}