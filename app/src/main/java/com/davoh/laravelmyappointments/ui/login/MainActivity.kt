package com.davoh.laravelmyappointments.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.davoh.laravelmyappointments.R
import com.davoh.laravelmyappointments.api.LaravelApiService
import com.davoh.laravelmyappointments.core.Resource
import com.davoh.laravelmyappointments.databinding.ActivityMainBinding
import com.davoh.laravelmyappointments.io.response.LoginResponse
import com.davoh.laravelmyappointments.ui.menu.MenuActivity
import com.davoh.laravelmyappointments.ui.register.RegisterActivity
import com.davoh.laravelmyappointments.ui.viewModels.LoginViewModel
import com.davoh.laravelmyappointments.utils.*
import com.davoh.laravelmyappointments.utils.PreferenceHelper.get
import com.davoh.laravelmyappointments.utils.PreferenceHelper.set
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding //First search by xml activity_main binding

    private val viewModel : LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val preferences = PreferenceHelper.defaultPrefs(this)
        if(preferences["accessToken",""].contains(".")){
            goMenuActivity()
        }

        binding.btnLogin.setOnClickListener {
            performLogin()
        }

        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    //[LOGIN]
    private fun performLogin(){
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if(email.isEmpty() || password.isEmpty()){
            toast("Por favor ingrese un correo y contraseña")
            return
        }

        viewModel.postLogin(email,password).observe(this){ result ->
            binding.progressBar.showIf { result is Resource.Loading }
            binding.btnLogin.disableIf { result is Resource.Loading }

            when(result){
                is Resource.Loading->{
                    toast("Cargando...")
                }
                is Resource.Success->{
                    if(result.data.success){
                        binding.btnLogin.disable()
                        createSessionPreference(result.data.accessToken)
                        toast("Bienvenido ${result.data.user.name}! con Hilt!")
                        goMenuActivity(true)
                    }else if (!result.data.success) {
                        toast("Las credenciales son incorrectas")
                    }
                }
                is Resource.Failure->{
                    //binding.btnLogin.isEnabled = true
                    toast("Hubo un erro de conexión")
                }
            }
        }
    }
    //[LOGIN]

    private fun createSessionPreference(jwt:String) {
        val preferences = PreferenceHelper.defaultPrefs(this)
        preferences["accessToken"] = jwt
    }

    private fun goMenuActivity(isUserInput : Boolean = false) {
        val intent = Intent(this, MenuActivity::class.java)

        if(isUserInput){
            intent.putExtra("store_token",true)
        }

        startActivity(intent)
        finish()
    }

    private val snackBar by lazy{
        Snackbar.make(binding.mainLayout, R.string.press_back_again,Snackbar.LENGTH_SHORT)
    }
    override fun onBackPressed() {
        if(snackBar.isShown){
            super.onBackPressed()
        }else{
            snackBar.show()
        }
    }

}