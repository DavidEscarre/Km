package com.example.km.GoogleMapsMap

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.km.PuntGPSManagment.ui.viewmodels.PuntGPSViewModel
import com.example.km.RutaManagment.ui.viewmodels.RutaViewModel
import com.example.km.main.screens.getCurrentLocation
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState



    @Composable
    fun GoogleMapScreen() {
        val context = LocalContext.current
        val cameraPositionState = rememberCameraPositionState()
        var userLocation by remember { mutableStateOf<LatLng?>(null) }
        val viewModel: PuntGPSViewModel = viewModel(factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PuntGPSViewModel(context.applicationContext as Application) as T
            }
        })

        val locationList by viewModel.locationList.collectAsState()
        LaunchedEffect(Unit) {
            getCurrentLocation(context) { location ->
                userLocation = location
                cameraPositionState.position = CameraPosition.fromLatLngZoom(location, 15f)
            }
        }

        GoogleMap(
           // modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            userLocation?.let { location ->
                Marker(state = MarkerState(position = location), title = "Mi Ubicación")
            }
            if (locationList.isNotEmpty()) {
                Polyline(points = locationList, color = Color.Blue, width = 8f)
            }
        }
    }

    @Preview
    @Composable
    fun PreviewMap() {
        GoogleMapScreen()
    }
