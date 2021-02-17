package com.davoh.laravelmyappointments.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.davoh.laravelmyappointments.R
import com.davoh.laravelmyappointments.databinding.ActivityMainBinding
import com.davoh.laravelmyappointments.io.response.LoginResponse
import com.davoh.laravelmyappointments.ui.menu.MenuActivity
import com.davoh.laravelmyappointments.ui.register.RegisterActivity
import com.davoh.laravelmyappointments.utils.PreferenceHelper
import com.davoh.laravelmyappointments.utils.PreferenceHelper.get
import com.davoh.laravelmyappointments.utils.PreferenceHelper.set
import com.davoh.laravelmyappointments.utils.toast
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding //First search by xml activity_main binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

       /* val preferences = getSharedPreferences("general", Context.MODE_PRIVATE)
        val session = preferences.getBoolean("active_session", false)
*/

        /*if(session){
            goMenuActivity()
        }*/


        val preferences = PreferenceHelper.defaultPrefs(this)
        if(preferences["accessToken",""].contains(".")){
            goMenuActivity()
        }

        binding.btnLogin.setOnClickListener {
            //validate with backend
            performLogin()
        }

        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            //finish()
        }

    }

    //[LOGIN]
    private val apiService: ApiService by lazy{
        ApiService.create()
    }
    private fun performLogin(){
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if(email.trim().isEmpty() || password.trim().isEmpty()){
            toast("Por favor ingrese un correo y contraseña")
            return
        }


       val call = apiService.postLogin(binding.etEmail.text.toString(), binding.etPassword.text.toString())
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse == null) {
                        //Toast.makeText(this@MainActivity, "Hubo un fallo al iniciar sesión", Toast.LENGTH_SHORT).show()
                        toast("Hubo un fallo al iniciar sesión")
                        return
                    }
                    if (loginResponse.success) {
                        createSessionPreference(loginResponse.accessToken)
                        toast("Bienvenido ${loginResponse.user.name}!")
                        goMenuActivity(true)
                    } else if (!loginResponse.success) {
                        toast("Las credenciales son incorrectas")
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                //Toast.makeText(this@MainActivity, "Hubo un fallo al iniciar sesión", Toast.LENGTH_SHORT).show()
                toast("Hubo un fallo en la conexión al servidor")
            }
        })
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