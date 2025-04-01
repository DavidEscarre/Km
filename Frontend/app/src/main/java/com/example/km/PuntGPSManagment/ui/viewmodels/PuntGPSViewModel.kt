package com.example.km.PuntGPSManagment.ui.viewmodels

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.km.PuntGPSManagment.data.repositories.PuntGPSRepositoryImpl
import com.example.km.RutaManagment.ui.viewmodels.RutaViewModel
import com.example.km.core.models.PuntGPS
import com.example.km.core.models.Ruta
import com.google.android.gms.common.api.Response
import com.google.android.gms.common.api.Result
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class PuntGPSViewModel(application: Application) : AndroidViewModel(application) {

    val rutaViewModel: RutaViewModel = RutaViewModel()
    var rutaIniciada = rutaViewModel.rutaIniciada

    private val puntGPSRepo = PuntGPSRepositoryImpl()

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    private val _locationList = MutableStateFlow<List<LatLng>>(emptyList())
    val locationList: StateFlow<List<LatLng>> = _locationList

    private val _puntGPSRutaList = MutableStateFlow<List<PuntGPS>>(emptyList())
    val puntGPSRutaList: StateFlow<List<PuntGPS>> = _puntGPSRutaList

    private var locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000L)
        .setWaitForAccurateLocation(true)
        .setMinUpdateIntervalMillis(2000)
        .build()


    private val locationCallback = object : LocationCallback() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { location ->
                if(rutaIniciada.value != null){
                    _locationList.value = _locationList.value + (LatLng(location.latitude, location.longitude))

                    val dataMarcaTemps = LocalDateTime.now()
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    val formattedDate = dataMarcaTemps.format(formatter)

                    create(
                        PuntGPS(rutaIniciada.value, location.latitude.toLong(), location.longitude.toLong(), dataMarcaTemps),

                            onSuccess = { response ->
                            val puntGPSCreat = findById(response,onSuccess = {}, onError = {})
                                if(puntGPSCreat!=null){
                                    _puntGPSRutaList.value = _puntGPSRutaList.value + (puntGPSCreat)
                                }
                            },
                            onError = { errorMessage ->

                            })


                }

            }
        }
    }

    fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    fun create(puntGPS: PuntGPS, onSuccess: (Long) -> Unit, onError: (String) -> Unit ){
        viewModelScope.launch {
            try {

                val response = puntGPSRepo.createPuntGPS(puntGPS)

                if (response.isSuccessful) {

                    Log.d("PuntGPS", "✅ Punt GPS creat")
                    onSuccess(response.body()!!)

                } else {
                    Log.e("PuntGPS", "❌ Error al crear el punt GPS")
                    val errorBody = response.errorBody()?.string()
                    val errorMessage =
                        "❌ ${response.code()} - $errorBody"
                    onError(errorMessage)

                }
            }catch(e: Exception){
                e.printStackTrace()
            }

        }

    }

    fun findById(puntGPSId: Long, onSuccess: () -> Unit, onError: (String) -> Unit ): PuntGPS?{
        var res: PuntGPS? = null
        viewModelScope.launch {
            try {

                val response = puntGPSRepo.getPuntGPSById(puntGPSId)

                if (response.isSuccessful) {

                    Log.d("PuntGPS", "✅ Punt GPS trobat")
                    onSuccess()
                    res = response.body()
                } else {
                    res = null
                    Log.e("PuntGPS", "❌ Error al trobar el punt GPS per la id")
                    val errorBody = response.errorBody()?.string()
                    val errorMessage =
                        "❌ ${response.code()} - $errorBody"
                    onError(errorMessage)


                }
                res = response.body()
            }catch(e: Exception){
                res = null
                e.printStackTrace()
            }

        }
        return res
    }



}