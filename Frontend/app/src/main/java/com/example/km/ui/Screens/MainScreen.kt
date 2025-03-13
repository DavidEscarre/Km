package com.example.km.ui.Screens

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.km.GoogleMapsMap.GoogleMapScreen
import com.example.km.GoogleMapsMap.LocationViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng



@Composable
fun MainScreen() {
    val context = LocalContext.current
    var rutaActiva by remember { mutableStateOf(false) }
    val viewModel: LocationViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LocationViewModel(context.applicationContext as Application) as T
        }
    })
    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
            Box(modifier = Modifier.border(2.dp, Color.Black, RectangleShape).size(700.dp)){
                var hasPermission by remember { mutableStateOf(false) }

                if (!hasPermission) {
                    RequestLocationPermission { hasPermission = true }
                }

                if (hasPermission) {
                    // Ahora sí obtenemos la ubicación y mostramos el mapa
                    GoogleMapScreen()
                }
            }

        Spacer(modifier = Modifier.height(21.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = {rutaActiva = !rutaActiva} ) {
                Text(if(rutaActiva) "Aturar Ruta" else "Començar Ruta")
            }
        }
    }

    if(rutaActiva){
        viewModel.startLocationUpdates()
    }else{
        viewModel.stopLocationUpdates()
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