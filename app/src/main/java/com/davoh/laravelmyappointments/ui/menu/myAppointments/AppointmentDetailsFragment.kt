package com.davoh.laravelmyappointments.ui.menu.myAppointments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.davoh.laravelmyappointments.R
import com.davoh.laravelmyappointments.data.model.Appointment
import com.davoh.laravelmyappointments.databinding.FragmentAppointmentDetailsBinding
import com.davoh.laravelmyappointments.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

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

        val appointment = args.appointment
        binding.tvDoctorName.text = appointment.doctor.name
        binding.tvSpecialty.text = appointment.specialty.name
        binding.tvScheduledDateAndHour.text = "${appointment.scheduledDate} A las: ${appointment.scheduledTime}"
        binding.tvDescription.text = appointment.description
        binding.tvType.text = appointment.type
        binding.tvAppointmentId.text = getString(R.string.appointment_id, appointment.id)

        val formatter = SimpleDateFormat("dd/MMMM/yyyy", Locale.getDefault())
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = appointment.createdAt
        binding.tvCreatedAt.text = "Cita registrada el dia ${formatter.format(calendar.time)}"

        binding.tvStatus.text = appointment.status
        when(appointment.status){
            "Reservada"->{
                binding.statusMiniCard.setBackgroundResource(R.color.orange_200)
            }
            "Confirmada"->{
                binding.statusMiniCard.setBackgroundResource(R.color.blue_200)
            }
            "Cancelada"->{
                binding.statusMiniCard.setBackgroundResource(R.color.red_200)
            }
            "Atendida"->{
                binding.statusMiniCard.setBackgroundResource(R.color.green_200)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}