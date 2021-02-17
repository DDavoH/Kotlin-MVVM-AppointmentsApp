package com.davoh.laravelmyappointments.ui.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.davoh.laravelmyappointments.databinding.ActivityRegisterBinding
import com.davoh.laravelmyappointments.io.response.LoginResponse
import com.davoh.laravelmyappointments.ui.menu.MenuActivity
import com.davoh.laravelmyappointments.utils.PreferenceHelper
import com.davoh.laravelmyappointments.utils.PreferenceHelper.set
import com.davoh.laravelmyappointments.utils.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val apiService by lazy{
        ApiService.create()
    }

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
            val call = apiService.register(email,name,password,passwordConfirmed)

            call.enqueue(object: Callback<LoginResponse>{
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if(response.isSuccessful){
                        val loginResponse = response.body()
                        if (loginResponse == null) {
                            //Toast.makeText(this@MainActivity, "Hubo un fallo al iniciar sesión", Toast.LENGTH_SHORT).show()
                            toast("Hubo un fallo en los campos al registrar")
                            return
                        }
                        if (loginResponse.success) {
                            createSessionPreference(loginResponse.accessToken)
                            toast("Bienvenido ${loginResponse.user.name}!")
                            goMenuActivity()
                        }
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    toast("Hubo un fallo al registrar")
                    return
                }

            })
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
        startActivity(intent)
        finish()
    }


}