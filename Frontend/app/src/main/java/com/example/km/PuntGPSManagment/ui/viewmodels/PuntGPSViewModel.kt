package com.example.km.PuntGPSManagment.ui.viewmodels

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.km.PuntGPSManagment.data.repositories.PuntGPSRepositoryImpl
import com.example.km.PuntGPSManagment.domain.usecases.tempsAturUseCase
import com.example.km.core.models.PuntGPS
//import com.example.km.core.utils.LocationService
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

@RequiresApi(Build.VERSION_CODES.O)
class PuntGPSViewModel(application: Application) : AndroidViewModel(application) {


    private val _precisioPunts = MutableStateFlow(5000L)
    val precisioPunts: StateFlow<Long> = _precisioPunts


    fun setPrecisio(valor: Long) {
        _precisioPunts.value = valor
        updateLocationRequest()
        Log.d("PGPSViewModel Precicio", "precicio: ${precisioPunts.value}")
    }


    private val tempsAturUseCase = tempsAturUseCase()

    private val _currentLocation = MutableStateFlow<LatLng?>(null)
    val currentLocation: StateFlow<LatLng?> = _currentLocation

    private val _aturat = MutableStateFlow<Boolean>(false)
    val aturat: StateFlow<Boolean> = _aturat


    val tempsAtur: StateFlow<Long> = tempsAturUseCase.tempsAturMs


    private val _tempsAturAcumulatRuta = MutableStateFlow<Long>(0L)
    val tempsAturAcumulatRuta: StateFlow<Long> = _tempsAturAcumulatRuta

    private val puntGPSRepo = PuntGPSRepositoryImpl()

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    private val _locationList = MutableStateFlow<List<LatLng>>(emptyList())
    val locationList: StateFlow<List<LatLng>> = _locationList

    private val _puntGPSRutaListActual = MutableStateFlow<List<PuntGPS>>(emptyList())
    val puntGPSRutaListActual: StateFlow<List<PuntGPS>> = _puntGPSRutaListActual

    private val _puntGPSRutaList = MutableStateFlow<List<PuntGPS>>(emptyList())
    val puntGPSRutaList: StateFlow<List<PuntGPS>> get() = _puntGPSRutaList

    private var locationRequest =
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, precisioPunts.value)
            .setWaitForAccurateLocation(true)
            .setMinUpdateIntervalMillis(2000)
            .build()

    private fun updateLocationRequest() {
        locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, _precisioPunts.value
        )
            .setWaitForAccurateLocation(true)
            .setMinUpdateIntervalMillis(2000)
            .build()
    }

    private val locationCallback = object : LocationCallback() {
        @SuppressLint("SuspiciousIndentation")
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { location ->
                val locationLatLng = LatLng(location.latitude, location.longitude)
                setCurrentLocation(locationLatLng)
                detectorAtur(locationLatLng)
                _locationList.value = _locationList.value + (locationLatLng)


                val dataMarcaTemps = LocalDateTime.now()
                Log.d("PGPSVM_LocationRes", "punt creat data: ${LocalDateTime.now().second}")
                val puntGPSCreat = PuntGPS(location.latitude, location.longitude, dataMarcaTemps)
                _puntGPSRutaListActual.value = _puntGPSRutaListActual.value + (puntGPSCreat)
                Log.d(
                    "PGPSVM_LocationRes",
                    "PungGPS Afeixit a _puntGPSRutaList, size: ${puntGPSRutaListActual.value}"
                )


            }
        }
    }

    private val locationCallbackSimple = object : LocationCallback() {
        @SuppressLint("SuspiciousIndentation")
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { location ->

                setCurrentLocation(LatLng(location.latitude, location.longitude))
            }
        }
    }

    fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            vuidarllistaPuntsGPS()
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    fun startLocationUpdatesSimple() {
        if (ActivityCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            vuidarllistaPuntsGPS()
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallbackSimple,
                Looper.getMainLooper()
            )
        }
    }

    fun setLocationListForeground(puntGPS: PuntGPS) {
        _puntGPSRutaListActual.value += puntGPS;
    }

    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    fun stopLocationUpdatesSimple() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    fun vuidarllistaPuntsGPS() {
        _locationList.value = emptyList()
        _puntGPSRutaListActual.value = emptyList()
    }

    fun setCurrentLocation(location: LatLng) {
        _currentLocation.value = location
    }

    fun create(puntGPS: PuntGPS, onSuccess: (Long?) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {

                val response = puntGPSRepo.createPuntGPS(puntGPS)

                if (response.isSuccessful) {

                    Log.d("PuntGPS", "✅ Punt GPS creat")
                    val puntId = response.body()
                    onSuccess(puntId)

                } else {
                    Log.e("PuntGPS", "❌ Error al crear el punt GPS")
                    val errorBody = response.errorBody()?.string()
                    val errorMessage =
                        "❌ ${response.code()} - $errorBody"
                    onError(errorMessage)

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    }

    fun fetchAllByRuteId(id: Long) {
        viewModelScope.launch {
            try {

                val response = puntGPSRepo.getPuntGPSByIdRuta(id)

                if (response.isSuccessful) {
                    _puntGPSRutaList.value = response.body()!!
                    Log.d("PuntGPS", "✅ Punt GPS trobat")
                } else {
                    _puntGPSRutaList.value = emptyList()
                    val errorBody = response.errorBody()?.string()
                    val errorMessage =
                        "❌ ${response.code()} - $errorBody"
                    Log.e("PuntGPS", "❌ Error al trobar el punt GPS per la id, $errorMessage")


                }
            } catch (e: Exception) {
                _puntGPSRutaList.value = emptyList()
                e.printStackTrace()
            }

        }
    }

    fun findById(puntGPSId: Long, onSuccess: () -> Unit, onError: (String) -> Unit): PuntGPS? {
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
            } catch (e: Exception) {
                res = null
                e.printStackTrace()
            }

        }
        return res
    }

    fun detectorAtur(location: LatLng) {
        if (locationList.value.size > 0) {
            val startP = locationList.value.last()
            val results = FloatArray(1)

            Location.distanceBetween(
                startP.latitude,
                startP.longitude,
                location.latitude,
                location.longitude,
                results
            )
            if (results[0].toDouble() < 1) { // Si la distancia del l'ultim punt es menor a 1 METRE

                _aturat.value = true
                tempsAturUseCase.start(viewModelScope)

            } else {
                _tempsAturAcumulatRuta.value += tempsAtur.value
                _aturat.value = false
                tempsAturUseCase.stop()
                tempsAturUseCase.reset()
            }
        }

    }

    fun ResetTempsAturAcumulat() {
        _tempsAturAcumulatRuta.value = 0
    }

    fun ResetTempsAtur() {
        tempsAturUseCase.stop()
        tempsAturUseCase.reset()
    }

    fun vuidarPuntsLLitaRuta() {
        _puntGPSRutaList.value = emptyList()
    }
}
    /**
     * Lanza el LocationService en primer plano.
     */
    /*
    fun startLocationService() {
        val ctx = getApplication<Application>()
        Intent(ctx, LocationService::class.java).also { intent ->
            intent.action = LocationService.ACTION_START
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ctx.startForegroundService(intent)
                LocationService().setPuntsViewModel(this)
            } else {
                ctx.startService(intent)
            }

        }


    }

    /**
     * Detiene el LocationService.
     */
    fun stopLocationService() {
        val ctx = getApplication<Application>()
        Intent(ctx, LocationService::class.java).also { intent ->
            intent.action = LocationService.ACTION_STOP
            ctx.startService(intent)
        }
    }

    /**
     * Envía al servicio un nuevo intervalo de precisión (en milisegundos).
     */
    fun updateServicePrecision(newIntervalMs: Long) {
        // Actualizamos también nuestro StateFlow interno
        setPrecisio(newIntervalMs)

        val ctx = getApplication<Application>()
        Intent(ctx, LocationService::class.java).also { intent ->
            intent.action = LocationService.ACTION_UPDATE_PRECISION
            intent.putExtra(LocationService.EXTRA_INTERVAL, newIntervalMs)
            ctx.startService(intent)
        }
    }

}*/