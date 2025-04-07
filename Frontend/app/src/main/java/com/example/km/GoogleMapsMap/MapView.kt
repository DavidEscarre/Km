package com.example.km.GoogleMapsMap

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
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
import com.example.km.R
//import com.example.km.PuntGPSManagment.ui.viewmodels.ViewModelsFactories.PuntGPSViewModelFactory
import com.example.km.RutaManagment.ui.viewmodels.RutaViewModel
import com.example.km.main.screens.getCurrentLocation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState


    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun GoogleMapScreen() {

        val context = LocalContext.current
        val cameraPositionState = rememberCameraPositionState()
        var userLocation by remember { mutableStateOf<LatLng?>(null) }

        val rutaViewModel: RutaViewModel = viewModel()
       /* val puntGPSViewModel: PuntGPSViewModel = viewModel(
            factory = PuntGPSViewModelFactory(rutaViewModel, context.applicationContext as Application)
        )*/
      /* val puntGPSViewModel: PuntGPSViewModel = viewModel(factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PuntGPSViewModel(rutaViewModel,context.applicationContext as Application) as T
            }
        })*/
        val puntGPSViewModel: PuntGPSViewModel = viewModel(factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PuntGPSViewModel(context.applicationContext as Application) as T
            }
        })

        val locationList by puntGPSViewModel.locationList.collectAsState()
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RouteViewMap(rutaId: Long, rutaViewModel: RutaViewModel, context: Context){

    val ruta = rutaViewModel.findById(rutaId)
    val cameraPositionState = rememberCameraPositionState()
    var StartLocation by remember { mutableStateOf<LatLng?>(null) }
    var EndLocation by remember { mutableStateOf<LatLng?>(null) }

    val iconPuntGPS = remember {
        BitmapDescriptorFactory.fromResource(R.drawable.ellipse_4)
    }

    var puntVisible by remember { mutableStateOf<LatLng?>(null) }

    if(ruta != null && ruta.puntsGPS.isNotEmpty()){
        val puntsGPSList = ruta.puntsGPS
        var listaDePuntos = emptyList<LatLng>()

        var primerPunt = puntsGPSList.first()
        var ultimPunt = puntsGPSList.last()

        puntsGPSList.forEach {
            if(primerPunt.id > it.id){
                primerPunt = it
            }
            if(ultimPunt.id < it.id){
                ultimPunt = it
            }
            listaDePuntos = listaDePuntos + LatLng(it.latitud.toDouble(),it.longitud.toDouble())
        }

        StartLocation = LatLng(primerPunt.latitud.toDouble(), primerPunt.longitud.toDouble())
        EndLocation = LatLng(ultimPunt.latitud.toDouble(), ultimPunt.longitud.toDouble())

        val boundsBuilder = LatLngBounds.builder()
        listaDePuntos.forEach { boundsBuilder.include(it) }
        val bounds = boundsBuilder.build()

        LaunchedEffect(listaDePuntos) {
            cameraPositionState.move(
                CameraUpdateFactory.newLatLngBounds(bounds,100)
            )
        }

        //cameraPositionState.position = CameraPosition.fromLatLngZoom(location, 15f)


        GoogleMap(
            // modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            StartLocation?.let { location ->
                Marker(state = MarkerState(position = location), title = "Start")
            }
            EndLocation?.let { location ->
                Marker(state = MarkerState(position = location), title = "End")
            }

            if (listaDePuntos.isNotEmpty()) {

                Polyline(points = listaDePuntos, color = Color.Blue, width = 8f)

                listaDePuntos.forEach{
                    Marker(
                        state = MarkerState(position = it),
                        icon = iconPuntGPS,
                        title = "PuntGPS: $it",
                        visible = if(puntVisible == it) true else false
                    )
                }
            }
        }
    }


}


@RequiresApi(Build.VERSION_CODES.O)
    @Preview
    @Composable
    fun PreviewMap() {
        GoogleMapScreen()
    }
