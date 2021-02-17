package com.davoh.laravelmyappointments.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.davoh.laravelmyappointments.R
import com.davoh.laravelmyappointments.model.Appointment
import kotlinx.android.synthetic.main.appointment_row.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AppointmentAdapter(private val appointments: ArrayList<Appointment>) : RecyclerView.Adapter<AppointmentAdapter.ViewHolder>(){



    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){


        fun bind(appointment: Appointment) = with(itemView){
                tvAppointmentId.text = context.getString(R.string.appointment_id, appointment.id)
                tvDoctorName.text = appointment.doctor.name
                tvScheduledDate.text = context.getString(R.string.appointment_date, appointment.scheduledDate)
                tvScheduledHour.text = context.getString(R.string.appointment_time, appointment.scheduledTime)
                tvStatus.text = appointment.status
                tvSpecialty.text = appointment.specialty.name
                tvDescription.text = appointment.description
                tvType.text = appointment.type

            val formatter = SimpleDateFormat("dd/MMMM/yyyy", Locale.getDefault())
            val calendar: Calendar = Calendar.getInstance()
            calendar.timeInMillis = appointment.createdAt

            tvCreatedAt.text = "Cita registrada el dia  ${formatter.format(calendar.time)} "

            btnShowDetails.setOnClickListener {

                //Para animacion
                TransitionManager.beginDelayedTransition(parent as ViewGroup, AutoTransition())


                if(linearLayoutDetails.visibility == View.VISIBLE){
                    linearLayoutDetails.visibility = View.GONE
                    btnShowDetails.setImageResource(R.drawable.ic_expand_more)
                }else{
                    linearLayoutDetails.visibility = View.VISIBLE
                    btnShowDetails.setImageResource(R.drawable.ic_expand_less)
                }

            }
        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.appointment_row, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(appointments[position])
    }

    override fun getItemCount(): Int {
        return appointments.size
    }
}