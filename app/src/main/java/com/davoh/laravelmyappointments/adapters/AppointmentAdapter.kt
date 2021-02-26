package com.davoh.laravelmyappointments.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.davoh.laravelmyappointments.R
import com.davoh.laravelmyappointments.data.model.Appointment
import com.davoh.laravelmyappointments.databinding.AppointmentRowBinding
import kotlinx.android.synthetic.main.appointment_row.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AppointmentAdapter(private val appointments: List<Appointment>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var listener: OnItemClickListener? = null

    inner class ViewHolder(private val binding: AppointmentRowBinding): RecyclerView.ViewHolder(binding.root){


        fun bind(appointment: Appointment){

            binding.tvAppointmentId.text =  binding.root.context.getString(R.string.appointment_id, appointment.id)
            binding.tvDoctorName.text = appointment.doctor.name
            binding.tvScheduledDate.text =  binding.root.context.getString(R.string.appointment_date, appointment.scheduledDate)
            binding.tvScheduledHour.text = binding.root.context.getString(R.string.appointment_time, appointment.scheduledTime)
            binding.tvStatus.text = appointment.status
            binding.tvSpecialty.text = appointment.specialty.name
            binding.tvDescription.text = appointment.description
            binding.tvType.text = appointment.type

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

            binding.btnShowDetails.setOnClickListener {

                //Para animacion
                TransitionManager.beginDelayedTransition(binding.root.parent as ViewGroup, AutoTransition())


                if(binding.linearLayoutDetails.visibility == View.VISIBLE){
                    binding.linearLayoutDetails.visibility = View.GONE
                    binding.btnShowDetails.setImageResource(R.drawable.ic_expand_more)
                }else{
                    binding.linearLayoutDetails.visibility = View.VISIBLE
                    binding.btnShowDetails.setImageResource(R.drawable.ic_expand_less)
                }

            }

            val formatter = SimpleDateFormat("dd/MMMM/yyyy", Locale.getDefault())
            val calendar: Calendar = Calendar.getInstance()
            calendar.timeInMillis = appointment.createdAt

            binding.tvCreatedAt.text = "Cita registrada el dia  ${formatter.format(calendar.time)} "

            binding.cardViewRow.setOnClickListener {
                listener?.onIntemClick(appointment)
            }
        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AppointmentRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(appointments[position])
    }

    override fun getItemCount(): Int {
        return appointments.size
    }

    interface OnItemClickListener {
        fun onIntemClick(appointment: Appointment)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}