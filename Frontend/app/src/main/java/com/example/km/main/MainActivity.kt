package com.example.km.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.km.PuntGPSManagment.ui.viewmodels.PuntGPSViewModel
import com.example.km.RutaManagment.ui.viewmodels.RecompensaViewModel
import com.example.km.RutaManagment.ui.viewmodels.RutaViewModel
import com.example.km.SistemaManagment.data.repositories.SistemaRepositoryImpl
import com.example.km.SistemaManagment.ui.viewmodels.SistemaViewModel
import com.example.km.UserManagment.ui.viewmodels.LoginViewModel
import com.example.km.UserManagment.ui.viewmodels.UserViewModel
import com.example.km.core.models.Sistema
import com.example.km.core.models.User
import com.example.km.main.screens.MainScreen
import com.example.km.core.ui.theme.KmTheme
import com.example.km.core.utils.FileLoggingTree


import com.example.km.navigation.AppNavigation
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapsSdkInitializedCallback
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : ComponentActivity(), OnMapsSdkInitializedCallback {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       Timber.plant(Timber.DebugTree())
        Timber.plant(FileLoggingTree(applicationContext))
        Timber.d("Timber Logs iniciado.")


        //MapsInitializer.initialize(applicationContext, MapsInitializer.Renderer.LATEST, this)
        setContent {
            KmTheme {
                val sistemaViewModel: SistemaViewModel = viewModel()
                val loginViewModel: LoginViewModel = viewModel()

                val user by loginViewModel.userState.collectAsState()

                // val sistema = getSistemaFromBackend(sistemaViewModel)
                val userViewModel: UserViewModel = viewModel()
                val navController: NavHostController = rememberNavController()
                val rutaViewModel: RutaViewModel = viewModel()
                val puntGPSViewModel: PuntGPSViewModel = viewModel()
                val recompensaViewModel: RecompensaViewModel = viewModel()
                //puntGPSViewModel.setPrecisio(sistema.precisioPunts)

                /*val puntGPSViewModel = ViewModelProvider(this, PuntGPSViewModelFactory(application))
                    .get(PuntGPSViewModel::class.java)*/


                // Aquí guardamos el usuario autenticado
                val userState = loginViewModel.userState.collectAsState()


                AppNavigation(recompensaViewModel, userViewModel, sistemaViewModel, puntGPSViewModel, rutaViewModel, navController, loginViewModel, userState)
            }
        }
        /*setContent {

            MainScreen()
        }*/
    }


    override fun onMapsSdkInitialized(renderer: MapsInitializer.Renderer) {
        Log.d("GoogleMap", "Renderer usado: $renderer")
    }


}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KmTheme {
        Greeting("Android")

    }
}
