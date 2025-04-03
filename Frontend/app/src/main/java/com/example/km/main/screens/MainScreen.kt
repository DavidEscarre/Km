package com.example.km.main.screens

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.km.GoogleMapsMap.GoogleMapScreen
import com.example.km.PuntGPSManagment.ui.viewmodels.PuntGPSViewModel
import com.example.km.RutaManagment.ui.viewmodels.RutaViewModel
import com.example.km.core.models.Ruta
import com.example.km.core.models.User
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(rutaViewModel: RutaViewModel, navController: NavController,   userState: State<User?>) {
    val context = LocalContext.current
    var rutaActiva by remember { mutableStateOf(false) }
    val puntsGPSViewModel: PuntGPSViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PuntGPSViewModel(context.applicationContext as Application) as T
        }
    })
    val locationList by puntsGPSViewModel.locationList.collectAsState()
    val scrollState = rememberScrollState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)  // Habilita el scroll
    ) {
        Box(
            modifier = Modifier
                .border(2.dp, Color.Black, RectangleShape)
                .fillMaxWidth()  // Asegura que no se desborde
                .height(600.dp)  // Altura razonable para el mapa
        ) {
            var hasPermission by remember { mutableStateOf(false) }

            if (!hasPermission) {
                RequestLocationPermission { hasPermission = true }
            }

            if (hasPermission) {
                GoogleMapScreen()
            }
        }

        Spacer(modifier = Modifier.height(21.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = {
                    rutaActiva = !rutaActiva


                }) {
                    Text(if (rutaActiva) "Aturar Ruta" else "Començar Ruta")
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)  // Espaciado para mejorar la apariencia
            ) {
                locationList.forEach { elemento ->
                    Text(
                        text = elemento.toString(),
                        fontSize = 18.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
    if(rutaActiva){

        IniciarRuta(rutaViewModel, puntsGPSViewModel, context, userState )
    }else{
        AturarRuta(rutaViewModel, puntsGPSViewModel, context, userState )
    }

}

@Composable
fun RequestLocationPermission(
    onPermissionGranted: () -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onPermissionGranted()
        } else {
            Toast.makeText(context, "Permiso de ubicación denegado", Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}


@SuppressLint("MissingPermission")
fun getCurrentLocation(context: Context, onLocationReceived: (LatLng) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        location?.let {
            onLocationReceived(LatLng(it.latitude, it.longitude))
        }
    }
}



@SuppressLint("CoroutineCreationDuringComposition")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun IniciarRuta(rutaViewModel: RutaViewModel, puntsGPSViewModel: PuntGPSViewModel, context: Context,  userState: State<User?>){

    val coroutineScope = rememberCoroutineScope()
    val dataInici = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val formattedDate = dataInici.format(formatter)
    val rutaIniciada by rutaViewModel.rutaIniciada.collectAsState()
    Log.d("USER_STATE1", "✅ USUARI ${userState}" )
    val ciclista: User = userState.value!!
    Log.d("USER_STATE2", "✅ USUARI ${ciclista}" )
    if(rutaIniciada == null){

      val ruta = Ruta(ciclista, dataInici, dataInici)
        //Log.d("ruta", "✅ ruta ${ruta.toString()}" )
        coroutineScope.launch {
            Log.d("INICIANDO RUTA AAAAAAA", "✅ USUARI ${ciclista}" )
           // rutaViewModel.iniciarRuta(ciclista,formattedDate, "", emptyList(), context,
            rutaViewModel.iniciarRuta(ruta, context,

            onSuccess = {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(
                            context,
                            "Ruta iniciada",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                },

                onError = { errorMessage ->
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(
                            context,
                            errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })

            puntsGPSViewModel.startLocationUpdates()
        }
    }


}


@SuppressLint("CoroutineCreationDuringComposition")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AturarRuta(rutaViewModel: RutaViewModel, puntsGPSViewModel: PuntGPSViewModel, context: Context,  userState: State<User?>) {

    puntsGPSViewModel.stopLocationUpdates()
    val dataFinal = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val formattedDate = dataFinal.format(formatter)
    val coroutineScope = rememberCoroutineScope()
    val puntGPSRutaList by puntsGPSViewModel.puntGPSRutaList.collectAsState()
    val rutaIniciada = rutaViewModel.rutaIniciada.collectAsState().value

    val ciclista: User? = userState.value

    if (ciclista == null) {
        Toast.makeText(context, "Error: Usuario no encontrado", Toast.LENGTH_SHORT).show()
        return
    }

    if (rutaIniciada == null) {
        Toast.makeText(context, "Error: Ruta no iniciada", Toast.LENGTH_SHORT).show()
        return
    }

    val ruta = Ruta(rutaIniciada.id, ciclista, rutaIniciada.dataInici, rutaIniciada.dataFinal, puntGPSRutaList)

    coroutineScope.launch {
        rutaViewModel.aturarRuta(ruta, context,
            onSuccess = {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(context, "Ruta finalizada", Toast.LENGTH_SHORT).show()
                }
            },
            onError = { errorMessage ->
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}

