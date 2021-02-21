package com.davoh.laravelmyappointments.ui.menu.myAppointments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.davoh.laravelmyappointments.data.model.Appointment
import com.davoh.laravelmyappointments.databinding.FragmentAppointmentDetailsBinding
import com.davoh.laravelmyappointments.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppointmentDetailsFragment : Fragment() {

    private var _binding: FragmentAppointmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: AppointmentDetailsFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAppointmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireContext().toast("${args.appointment.id}")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}