package com.davoh.laravelmyappointments.ui.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.davoh.laravelmyappointments.api.LaravelApiService
import com.davoh.laravelmyappointments.core.Resource
import com.davoh.laravelmyappointments.databinding.ActivityRegisterBinding
import com.davoh.laravelmyappointments.io.response.LoginResponse
import com.davoh.laravelmyappointments.ui.menu.MenuActivity
import com.davoh.laravelmyappointments.ui.viewModels.RegisterViewModel
import com.davoh.laravelmyappointments.utils.*
import com.davoh.laravelmyappointments.utils.PreferenceHelper.set
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnRegister.setOnClickListener {
            performRegister()
        }
    }

    private fun performRegister(){
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val passwordConfirmed = binding.etPasswordConfirmation.text.toString().trim()

        if(validateForm(name,email,password,passwordConfirmed)){

            viewModel.register(email,name,password,passwordConfirmed).observe(this){result->
                binding.progressBar.showIf { result is Resource.Loading }
                binding.btnRegister.disableIf { result is Resource.Loading }
                when(result){
                    is Resource.Loading->{

                    }
                    is Resource.Success->{
                        val loginResponse = result.data
                        if (loginResponse.success) {
                            binding.btnRegister.disable()
                            createSessionPreference(loginResponse.accessToken)
                            toast("Bienvenido ${loginResponse.user.name}!")
                            goMenuActivity()
                        }else{
                            toast("Hubo un fallo en los campos al registrar")
                            return@observe
                        }
                    }
                    is Resource.Failure->{
                        toast("Hubo un fallo en la conexión")
                    }
                }
            }
        }

    }

    private fun validateForm(name:String, email:String, password:String, passwordConfirmed:String):Boolean{
        if(name.isEmpty() || email.isEmpty()){
            return false
        }

        if(password.length<8){
            binding.etPassword.error = "La contraseña debe tener 8 caracteres minimo"
            toast("La contraseña debe tener 8 caracteres minimo")
            return false
        }

        if(passwordConfirmed.length<8){
            binding.etPasswordConfirmation.error = "La contraseña debe tener 8 caracteres minimo"
            toast("La contraseña debe tener 8 caracteres minimo")
            return false
        }

        if(password != passwordConfirmed){
            toast("Las contraseñas no son iguales")
            return false
        }

        return true
    }

    private fun createSessionPreference(accessToken:String){
        val preferences = PreferenceHelper.defaultPrefs(this)
        preferences["accessToken"] = accessToken
    }

    private fun goMenuActivity() {
        val intent = Intent(this, MenuActivity::class.java)
        intent.putExtra("store_token",true)
        startActivity(intent)
        finish()
    }


}