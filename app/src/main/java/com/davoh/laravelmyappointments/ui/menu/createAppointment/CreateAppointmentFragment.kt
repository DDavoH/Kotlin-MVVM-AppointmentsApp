package com.davoh.laravelmyappointments.ui.menu.createAppointment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.davoh.laravelmyappointments.R
import com.davoh.laravelmyappointments.api.LaravelApiService
import com.davoh.laravelmyappointments.core.Resource
import com.davoh.laravelmyappointments.databinding.FragmentCreateAppointmentBinding
import com.davoh.laravelmyappointments.io.response.SimpleResponse
import com.davoh.laravelmyappointments.data.model.Doctor
import com.davoh.laravelmyappointments.data.model.Schedule
import com.davoh.laravelmyappointments.data.model.Specialty
import com.davoh.laravelmyappointments.io.body.StoreAppointment
import com.davoh.laravelmyappointments.ui.dialogs.LoadingDialog
import com.davoh.laravelmyappointments.ui.viewModels.CreateAppointmentViewModel
import com.davoh.laravelmyappointments.utils.PreferenceHelper
import com.davoh.laravelmyappointments.utils.PreferenceHelper.get
import com.davoh.laravelmyappointments.utils.disable
import com.davoh.laravelmyappointments.utils.disableIf
import com.davoh.laravelmyappointments.utils.toast
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

@AndroidEntryPoint
class CreateAppointmentFragment : Fragment() {

    private val viewModel: CreateAppointmentViewModel by viewModels()

    private var _binding: FragmentCreateAppointmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var loadingDialog: LoadingDialog

    //Public values
    private var _specialtyId by Delegates.notNull<Int>()
    private var _doctorId by Delegates.notNull<Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCreateAppointmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = LoadingDialog(requireActivity())

        binding.btnNextStep.setOnClickListener {
            if (binding.etDescription.text.toString().length < 3) {
                binding.etDescription.error = "La descripción es demasiado corta"
            } else {
                binding.cardFirstStep.visibility = View.GONE
                binding.cardSecondStep.visibility = View.VISIBLE
            }
        }

        binding.btnNextStep2.setOnClickListener {

            when {
                binding.etDate.text.toString().isEmpty() -> {
                    binding.etDate.error = "Es necesario seleccionar una fecha"
                }
                selectedTimeRadioBtn == null -> {
                    Snackbar.make(binding.mainLayout, "Es necesario seleccionar una hora", Snackbar.LENGTH_SHORT).show()
                }
                else -> {
                    showAppointmentDataToConfirm()
                    binding.cardSecondStep.visibility = View.GONE
                    binding.cardStepConfirmation.visibility = View.VISIBLE
                }
            }

        }

        binding.btnConfirmAppointment.setOnClickListener {
            performStoreAppointment()
        }

        spinnerSpecialties()
        spinnerDoctors()
        datePicker()
        listenDoctorsAndDateChanges()
        alertDialogExit()
    }

    private fun performStoreAppointment(){

        val preferences= PreferenceHelper.defaultPrefs(requireContext())
        val accessToken = preferences["accessToken",""]
        val authHeader = "Bearer $accessToken"

        val storeAppointment =
            StoreAppointment(
            binding.tvConfirmDescription.text.toString(),
            _specialtyId,
            _doctorId,
            binding.tvConfirmScheduledDate.text.toString(),
            binding.tvConfirmScheduledTime.text.toString(),
            binding.tvConfirmType.text.toString()
        )

        viewModel.storeAppointment(authHeader, storeAppointment).observe(viewLifecycleOwner){result->

            binding.btnConfirmAppointment.disableIf { result is Resource.Loading }

            when(result){
                is Resource.Loading->{
                    loadingDialog.startLoadingDialog()
                }
                is Resource.Success->{
                    loadingDialog.dismissDialog()
                    binding.btnConfirmAppointment.disable()
                    requireContext().toast("Cita registrada correctamente")
                    findNavController().navigateUp()
                }
                is Resource.Failure->{
                    loadingDialog.dismissDialog()
                    requireContext().toast("Ocurrio un error")
                }
            }
        }


    }

    //[SPECIALTIES SPINNER]
    private fun spinnerSpecialties() {
        loadSpecialtiesRetrofit()
    }

    private fun loadSpecialtiesRetrofit() {
        viewModel.getSpecialties().observe(viewLifecycleOwner){result->
            when(result){
                is Resource.Loading->{

                }
                is Resource.Success->{
                    var specialties= ArrayList<Specialty>()

                    if(result.data.size>0){
                        specialties = result.data
                    }else{
                        specialties.add(Specialty(555,"No se ha encontrado ninguna especialidad"))
                    }
                    showSpinnerSpecialties(specialties)
                }
                is Resource.Failure->{
                    Toast.makeText(
                        requireContext(),
                        "Ocurrio un problema al cargar las especialidades",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun showSpinnerSpecialties(specialtiesList: ArrayList<Specialty>){
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, specialtiesList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSpecialty.setAdapter(adapter)
    }
    //[SPECIALTIES SPINNER]

    //[DOCTORS SPINNER]}
    private fun spinnerDoctors(){
        //FirstListening SpecialtySpinner
        binding.spinnerSpecialty.onItemClickListener = AdapterView.OnItemClickListener {
            parent, view, position, id ->
            val specialtySelected = parent?.getItemAtPosition(position) as Specialty
            loadDoctorsRetrofit(specialtySelected.id)
            _specialtyId = specialtySelected.id
        }

    }

    private fun loadDoctorsRetrofit(doctorId: Int){
        viewModel.getDoctors(doctorId).observe(viewLifecycleOwner){result->
            when(result){
                is Resource.Loading->{

                }
                is Resource.Success->{
                    var doctors = ArrayList<Doctor>()

                    if (result.data.size>0) {
                        doctors = result.data
                    } else {
                        doctors.add(Doctor(555, "No se ha encontrado ningun doctor"))
                    }
                    Log.d(TAG, "onResponse: $doctors")

                    showSpinnerDoctors(doctors)
                }
                is Resource.Failure->{
                    Toast.makeText(
                        requireContext(),
                        "Ocurrio un problema al cargar los doctores",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun showSpinnerDoctors(doctorsList: ArrayList<Doctor>) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, doctorsList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDoctor.setAdapter(adapter)
    }
    //[DOCTORS SPINNER]}

    //[CALENDAR]
    private val selectedCalendar = Calendar.getInstance()

    private fun datePicker() {
        binding.etDate.setOnClickListener {

            val year = selectedCalendar.get(Calendar.YEAR)
            val month = selectedCalendar.get(Calendar.MONTH)
            val dayOfMonth = selectedCalendar.get(Calendar.DAY_OF_MONTH)

            val listener = DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->
                selectedCalendar.set(y, m, d)
                binding.etDate.setText(getString(
                        R.string.date_format,
                        y.toString(),
                        (m+1).twoDigits(),
                        d.twoDigits()
                ))
                binding.etDate.error = null
            }


            val datePickerDialog = DatePickerDialog(requireContext(), R.style.CalendarPicker, listener, year, month, dayOfMonth)
            val datePicker = datePickerDialog.datePicker
            datePicker.minDate = getFechaLongStart()
            datePicker.maxDate = getFechaLongEnd()
            datePickerDialog.show()
        }
    }

    //Esta es la fecha de inicio para la busqueda
    private fun getFechaLongStart(): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        return calendar.timeInMillis
    }

    //Esta es la fecha de termino para la busqueda
    private fun getFechaLongEnd(): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 29)
        return calendar.timeInMillis
    }

    private fun Int.twoDigits() = if (this >= 10) this.toString() else "0$this"
    //[CALENDAR]

    //[LISTEN]
    private fun listenDoctorsAndDateChanges(){
        var doctorGlobal = Doctor(1,"")

        binding.spinnerDoctor.onItemClickListener = AdapterView.OnItemClickListener {
            parent, view, position, id ->
            doctorGlobal = parent?.getItemAtPosition(position) as Doctor
            loadHours(doctorGlobal.id, binding.etDate.text.toString())
            _doctorId = doctorGlobal.id
        }

        binding.etDate.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                loadHours(doctorGlobal.id, binding.etDate.text.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    private fun loadHours(doctorId: Int, date:String){

        if(date.isEmpty()){
            return
        }

        viewModel.getHours(doctorId, date).observe(viewLifecycleOwner){result->
            when(result){
                is Resource.Loading->{

                }
                is Resource.Success->{
                    val schedule = result.data

                    schedule.let { scheduleHours ->
                        val intervals = scheduleHours.morning + scheduleHours.afternoon
                        val hours = ArrayList<String>()
                        intervals.forEach{ interval ->
                            hours.add(interval.start)
                        }
                        displayIntervalsRadioButtons(hours)
                    }
                }
                is Resource.Failure->{
                    requireContext().toast("No se han podido cargar las horas")
                }
            }
        }
    }
    //[LISTEN]

    //[RADIO BUTTONS - parent CALENDAR]
    private var selectedTimeRadioBtn: RadioButton? = null
    private fun displayIntervalsRadioButtons(hours: ArrayList<String>) {

        selectedTimeRadioBtn = null
        binding.radioGroupDateTime.removeAllViews()
        binding.radioGroupDateTime2.removeAllViews()
        var goToLeft = true

        if(hours.isEmpty()){
            binding.tvAlertHours.visibility = View.VISIBLE
            binding.tvAlertHours.text = "No hay horas disponibles para esa fecha"
            binding.tvAlertHours.setTextColor(ContextCompat.getColor(requireContext(), R.color.design_default_color_error))
            return
        }

        //TODO ELEGIR EL COLOR POR DEFECTO
        binding.tvAlertHours.visibility = View.GONE


        hours.forEach {
            val radioButton = MaterialRadioButton(requireContext(), null, R.attr.radioButtonStyle)
            radioButton.id = View.generateViewId()
            radioButton.text = it

            radioButton.setOnClickListener { view ->
                selectedTimeRadioBtn?.isChecked = false
                selectedTimeRadioBtn = view as RadioButton
                selectedTimeRadioBtn?.isChecked = true
            }

            if (goToLeft) {
                binding.radioGroupDateTime.addView(radioButton)
            } else {
                binding.radioGroupDateTime2.addView(radioButton)
            }
            goToLeft = !goToLeft

        }

    }
    //[RADIO BUTTONS - parent CALENDAR]

    private fun showAppointmentDataToConfirm() {
        binding.tvConfirmDescription.text = binding.etDescription.text.toString()
        binding.tvConfirmSpecialty.text = binding.spinnerSpecialty.text.toString()


        val selectedRadioBtnId = binding.radioGroupType.checkedRadioButtonId
        val selectedRadioType = binding.radioGroupType.findViewById<RadioButton>(selectedRadioBtnId)
        binding.tvConfirmType.text = selectedRadioType.text.toString()

        binding.tvConfirmDoctorName.text = binding.spinnerDoctor.text.toString()
        binding.tvConfirmScheduledDate.text = binding.etDate.text.toString()
        binding.tvConfirmScheduledTime.text = selectedTimeRadioBtn?.text.toString()
    }

    private fun alertDialogExit() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                when {
                    binding.cardStepConfirmation.visibility == View.VISIBLE -> {
                        binding.cardStepConfirmation.visibility = View.GONE
                        binding.cardSecondStep.visibility = View.VISIBLE
                    }
                    binding.cardSecondStep.visibility == View.VISIBLE -> {
                        binding.cardSecondStep.visibility = View.GONE
                        binding.cardFirstStep.visibility = View.VISIBLE
                    }
                    binding.cardFirstStep.visibility == View.VISIBLE -> {
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setTitle("¿Estas seguro que deseas salir?")
                        builder.setMessage("Si abandona el registro los datos que habia ingresado se perderán")
                        builder.setPositiveButton("Si, Salir") { i, dialog ->
                            findNavController().navigateUp()
                        }
                        builder.setNegativeButton("Continuar registro") { dialog, _ ->
                            dialog.dismiss()
                        }
                        val dialog = builder.create()
                        dialog.show()
                    }
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
companion object{
    private const val TAG = "CreateAppointmentFragme"
}
   

}