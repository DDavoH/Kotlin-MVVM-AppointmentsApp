package com.davoh.laravelmyappointments.ui.menu


import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import com.davoh.laravelmyappointments.R
import com.davoh.laravelmyappointments.api.LaravelApiService
import com.davoh.laravelmyappointments.core.Resource
import com.davoh.laravelmyappointments.databinding.ActivityMenuBinding
import com.davoh.laravelmyappointments.ui.viewModels.MenuViewModel
import com.davoh.laravelmyappointments.utils.PreferenceHelper
import com.davoh.laravelmyappointments.utils.PreferenceHelper.get
import com.davoh.laravelmyappointments.utils.toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class MenuActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private lateinit var binding: ActivityMenuBinding

    private val viewModel : MenuViewModel by viewModels()

    private val snackBar by lazy{
        Snackbar.make(binding.mainLayout, R.string.press_back_again, Snackbar.LENGTH_SHORT)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val storeToken = intent.getBooleanExtra("store_token",false)
        if(storeToken){
            storeFcmToken()
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        setSupportActionBar(binding.toolbar)

        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    private fun storeFcmToken(){
        val preferences = PreferenceHelper.defaultPrefs(this)
        val accessToken = preferences["accessToken",""]
        val authHeader = "Bearer $accessToken"

        //Firebase Cloud Messaging
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("MyFirebaseMsgService", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val deviceToken = task.result

            // Log and toast
            val msg = getString(R.string.msg_token_fmt, deviceToken)
            Log.d("MyFirebaseMsgService", msg)

            viewModel.postToken(authHeader, deviceToken).observe(this){ result->
                when(result){
                    is Resource.Loading->{

                    }
                    is Resource.Success->{
                        Log.d(TAG, "Token registrado correctamente")
                    }
                    is Resource.Failure->{
                        Log.d(TAG, "Hubo un error al registrar el token")
                    }
                }
            }

        })

    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
    }

    //Al presionar el back del sistema
    override fun onBackPressed() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        when(NavHostFragment.findNavController(navHostFragment).currentDestination?.id) {
            //Esto lo elimine porque ya esta en CreateAppointmentFragment.kt
            /*R.id.createAppointmentFragment -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("¿Estas seguro que deseas salir?")
                builder.setMessage("Si abandona el registro los datos que habia ingresado se perderán")
                builder.setPositiveButton("Si, Salir"){i,dialog->
                    navController.navigateUp()
                }
                builder.setNegativeButton("Continuar registro"){ dialog,_ ->
                    dialog.dismiss()
                }
                val dialog = builder.create()
                dialog.show()
            }*/
            R.id.menuFragment ->{
                if(snackBar.isShown){
                    finish()
                }else{
                    snackBar.show()
                }
            }
            else -> {
                super.onBackPressed()
            }
        }
    }

    //Al presionar el icono de back del sistema
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
         when(NavHostFragment.findNavController(navHostFragment).currentDestination?.id) {
            R.id.createAppointmentFragment -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("¿Estas seguro que deseas salir?")
                builder.setMessage("Si abandona el registro los datos que habia ingresado se perderán")
                builder.setPositiveButton("Si, Salir"){i,dialog->
                    navController.navigateUp()
                }
                builder.setNegativeButton("Continuar registro"){ dialog,_ ->
                    dialog.dismiss()
                }
                val dialog = builder.create()
                dialog.show()
                return true
            }
             R.id.appointmentsFragment->{
                 navController.navigateUp()
                 return false
             }
             R.id.appointmentDetailsFragment->{
                 navController.navigateUp()
                 return false
             }
            else -> {
               return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
            }
        }
    }

    companion object {
        private const val TAG = "okhttp"
    }

}