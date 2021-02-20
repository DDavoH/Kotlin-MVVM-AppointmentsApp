package com.davoh.laravelmyappointments.ui.menu

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.davoh.laravelmyappointments.R
import com.davoh.laravelmyappointments.api.LaravelApiService
import com.davoh.laravelmyappointments.core.Resource
import com.davoh.laravelmyappointments.databinding.FragmentMenuBinding
import com.davoh.laravelmyappointments.ui.dialogs.LoadingDialog
import com.davoh.laravelmyappointments.ui.login.MainActivity
import com.davoh.laravelmyappointments.ui.viewModels.MenuViewModel
import com.davoh.laravelmyappointments.utils.*
import com.davoh.laravelmyappointments.utils.PreferenceHelper.set
import com.davoh.laravelmyappointments.utils.PreferenceHelper.get
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MenuViewModel by viewModels()

    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingDialog = LoadingDialog(requireActivity())

        binding.btnRegisterAppointment.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_createAppointmentFragment)
        }

        binding.btnMyAppointments.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_appointmentsFragment)
        }

        binding.btnCloseSession.setOnClickListener {
            performLogout()
        }
    }


    //[LOGOUT]
    private fun performLogout(){
        val preferences = PreferenceHelper.defaultPrefs(requireContext())
        val jwt = preferences["accessToken",""]
        closeSessionInLaravel(jwt)
    }

    //despues cierro la session para no aceptas ningun token
    private fun closeSessionInLaravel(jwt: String){
        viewModel.postLogout("Bearer $jwt").observe(viewLifecycleOwner){result->
            binding.btnCloseSession.disableIf { result is Resource.Loading }
            when (result){
                is Resource.Loading -> {
                    loadingDialog.startLoadingDialog()
                }
                is Resource.Success->{
                    loadingDialog.dismissDialog()
                    if(result.data.success){
                        binding.btnCloseSession.disable()
                        clearSessionPreference()
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                }
                is Resource.Failure->{
                    loadingDialog.dismissDialog()
                    requireContext().toast("Hubo un erro de conexión")
                }
            }
        }
    }

    private fun clearSessionPreference() {
        /*val preferences = requireContext().getSharedPreferences("general", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean("active_session",false)
        editor.apply()*/

        val preferences = PreferenceHelper.defaultPrefs(requireContext())
        preferences["accessToken"] = ""
    }
    //[LOGOUT]

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}